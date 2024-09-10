package net.dusense.framework.extension.msgbus.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.cloud.stream.binder.*;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.http.MediaType;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.endpoint.EventDrivenConsumer;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.handler.AbstractMessageHandler;
import org.springframework.integration.handler.AbstractReplyProducingMessageHandler;
import org.springframework.integration.redis.inbound.RedisQueueMessageDrivenEndpoint;
import org.springframework.integration.redis.outbound.RedisQueueOutboundChannelAdapter;
import org.springframework.messaging.*;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * A {@link org.springframework.cloud.stream.binder.Binder} implementation backed by Redis.
 *
 * @author Mark Fisher
 * @author Gary Russell
 * @author David Turanski
 * @author Jennifer Hickey
 */
@Slf4j
public class RedisMessageChannelBinder
        extends AbstractBinder<MessageChannel, ConsumerProperties, ProducerProperties> {

    private static final String ERROR_HEADER = "errorKey";

    static final String CONSUMER_GROUPS_KEY_PREFIX = "groups.";

    private static final SpelExpressionParser parser = new SpelExpressionParser();

    private final String[] headersToMap;

    private final RedisOperations<String, String> redisOperations;

    private final RedisConnectionFactory connectionFactory;

    private final RedisQueueOutboundChannelAdapter errorAdapter;

    public RedisMessageChannelBinder(RedisConnectionFactory connectionFactory) {
        this(connectionFactory, new String[0]);
    }

    public RedisMessageChannelBinder(
            RedisConnectionFactory connectionFactory, String... headersToMap) {
        Assert.notNull(connectionFactory, "connectionFactory must not be null");
        this.connectionFactory = connectionFactory;
        StringRedisTemplate template = new StringRedisTemplate(connectionFactory);
        template.afterPropertiesSet();
        this.redisOperations = template;
        if (headersToMap != null && headersToMap.length > 0) {
            String[] combinedHeadersToMap =
                    Arrays.copyOfRange(
                            BinderHeaders.STANDARD_HEADERS,
                            0,
                            BinderHeaders.STANDARD_HEADERS.length + headersToMap.length);
            System.arraycopy(
                    headersToMap,
                    0,
                    combinedHeadersToMap,
                    BinderHeaders.STANDARD_HEADERS.length,
                    headersToMap.length);
            this.headersToMap = combinedHeadersToMap;
        } else {
            this.headersToMap = BinderHeaders.STANDARD_HEADERS;
        }
        this.errorAdapter =
                new RedisQueueOutboundChannelAdapter(
                        parser.parseExpression("headers['" + ERROR_HEADER + "']"),
                        connectionFactory);
    }

    @Override
    public void onInit() {
        this.errorAdapter.setIntegrationEvaluationContext(this.getEvaluationContext());
        this.errorAdapter.setBeanFactory(getBeanFactory());
        this.errorAdapter.afterPropertiesSet();
    }

    @Override
    protected Binding<MessageChannel> doBindConsumer(
            final String name,
            String group,
            MessageChannel moduleInputChannel,
            ConsumerProperties properties) {
        if (!StringUtils.hasText(group)) {
            group = "anonymous." + UUID.randomUUID().toString();
        }
        String queueName = groupedName(name, group);
        if (properties.isPartitioned()) {
            queueName += "-" + properties.getInstanceIndex();
        }
        MessageProducerSupport adapter = createInboundAdapter(properties, queueName);
        return doRegisterConsumer(name, group, queueName, moduleInputChannel, adapter, properties);
    }

    private MessageProducerSupport createInboundAdapter(
            ConsumerProperties accessor, String queueName) {
        MessageProducerSupport adapter;
        int concurrency = accessor.getConcurrency();
        concurrency = concurrency > 0 ? concurrency : 1;
        if (concurrency == 1) {
            RedisQueueMessageDrivenEndpoint single =
                    new RedisQueueMessageDrivenEndpoint(queueName, this.connectionFactory);
            single.setBeanFactory(getBeanFactory());
            single.setSerializer(null);
            adapter = single;
        } else {
            adapter = new CompositeRedisQueueMessageDrivenEndpoint(queueName, concurrency);
        }
        return adapter;
    }

    private Binding<MessageChannel> doRegisterConsumer(
            String bindingName,
            String group,
            String channelName,
            MessageChannel moduleInputChannel,
            MessageProducerSupport adapter,
            final ConsumerProperties properties) {
        DirectChannel bridgeToModuleChannel = new DirectChannel();
        bridgeToModuleChannel.setBeanFactory(this.getBeanFactory());
        bridgeToModuleChannel.setBeanName(channelName + ".bridge");
        MessageChannel bridgeInputChannel =
                addRetryIfNeeded(channelName, bridgeToModuleChannel, properties);
        adapter.setOutputChannel(bridgeInputChannel);
        adapter.setBeanName("inbound." + channelName);
        adapter.afterPropertiesSet();
        DefaultBinding<MessageChannel> consumerBinding =
                new DefaultBinding<MessageChannel>(
                        bindingName, group, moduleInputChannel, adapter) {

                    @Override
                    protected void afterUnbind() {
                        String key =
                                RedisMessageChannelBinder.CONSUMER_GROUPS_KEY_PREFIX + getName();
                        RedisMessageChannelBinder.this
                                .redisOperations
                                .boundZSetOps(key)
                                .incrementScore(getGroup(), -1);
                    }
                };
        ReceivingHandler convertingBridge = new ReceivingHandler(properties);
        convertingBridge.setOutputChannel(moduleInputChannel);
        convertingBridge.setBeanName(channelName + ".bridge.handler");
        convertingBridge.afterPropertiesSet();
        bridgeToModuleChannel.subscribe(convertingBridge);
        this.redisOperations
                .boundZSetOps(CONSUMER_GROUPS_KEY_PREFIX + bindingName)
                .incrementScore(group, 1);
        adapter.start();
        return consumerBinding;
    }

    /**
     * If retry is enabled, wrap the bridge channel in another that will invoke send() within the
     * scope of a retry template.
     *
     * @param name The name.
     * @param bridgeToModuleChannel The channel.
     * @param properties The properties.
     * @return The channel, or a wrapper.
     */
    private MessageChannel addRetryIfNeeded(
            final String name,
            final DirectChannel bridgeToModuleChannel,
            ConsumerProperties properties) {
        final RetryTemplate retryTemplate = buildRetryTemplate(properties);
        if (retryTemplate == null) {
            return bridgeToModuleChannel;
        } else {
            DirectChannel channel =
                    new DirectChannel() {

                        @Override
                        protected boolean doSend(final Message<?> message, final long timeout) {
                            try {
                                return retryTemplate.execute(
                                        new RetryCallback<Boolean, Exception>() {

                                            @Override
                                            public Boolean doWithRetry(RetryContext context)
                                                    throws Exception {
                                                return bridgeToModuleChannel.send(message, timeout);
                                            }
                                        },
                                        new RecoveryCallback<Boolean>() {

                                            /** Send the failed message to 'ERRORS:[name]'. */
                                            @Override
                                            public Boolean recover(RetryContext context)
                                                    throws Exception {
                                                log.error(
                                                        "Failed to deliver message; retries exhausted; message sent to queue 'ERRORS:"
                                                                + name
                                                                + "' ",
                                                        context.getLastThrowable());
                                                errorAdapter.handleMessage(
                                                        getMessageBuilderFactory()
                                                                .fromMessage(message)
                                                                .setHeader(
                                                                        ERROR_HEADER,
                                                                        "ERRORS:" + name)
                                                                .build());
                                                return true;
                                            }
                                        });
                            } catch (Exception e) {
                                log.error("Failed to deliver message", e);
                                return false;
                            }
                        }
                    };
            channel.setBeanName(name + ".bridge");
            return channel;
        }
    }

    @Override
    protected Binding<MessageChannel> doBindProducer(
            final String name, MessageChannel moduleOutputChannel, ProducerProperties properties) {
        Assert.isInstanceOf(SubscribableChannel.class, moduleOutputChannel);
        return doRegisterProducer(name, moduleOutputChannel, properties);
    }

    private RedisQueueOutboundChannelAdapter createProducerEndpoint(
            String name, ProducerProperties properties) {
        RedisQueueOutboundChannelAdapter queue;
        if (!properties.isPartitioned()) {
            queue = new RedisQueueOutboundChannelAdapter(name, this.connectionFactory);
        } else {
            queue =
                    new RedisQueueOutboundChannelAdapter(
                            parser.parseExpression(name), this.connectionFactory);
        }
        queue.setIntegrationEvaluationContext(this.getEvaluationContext());
        queue.setBeanFactory(this.getBeanFactory());
        queue.afterPropertiesSet();
        return queue;
    }

    private Binding<MessageChannel> doRegisterProducer(
            final String name, MessageChannel moduleOutputChannel, ProducerProperties properties) {
        Assert.isInstanceOf(SubscribableChannel.class, moduleOutputChannel);
        MessageHandler handler = new SendingHandler(name, properties);
        EventDrivenConsumer consumer =
                new EventDrivenConsumer((SubscribableChannel) moduleOutputChannel, handler);
        consumer.setBeanFactory(this.getBeanFactory());
        consumer.setBeanName("outbound." + name);
        consumer.afterPropertiesSet();
        DefaultBinding<MessageChannel> producerBinding =
                new DefaultBinding<>(name, null, moduleOutputChannel, consumer);
        String[] requiredGroups = properties.getRequiredGroups();
        if (!ObjectUtils.isEmpty(requiredGroups)) {
            for (String group : requiredGroups) {
                this.redisOperations
                        .boundZSetOps(CONSUMER_GROUPS_KEY_PREFIX + name)
                        .incrementScore(group, 1);
            }
        }
        consumer.start();
        return producerBinding;
    }

    private class SendingHandler extends AbstractMessageHandler {

        private final String bindingName;

        private final ProducerProperties producerProperties;

        private final Map<String, RedisQueueOutboundChannelAdapter> adapters = new HashMap<>();

        private final PartitionHandler partitionHandler;

        private SendingHandler(String bindingName, ProducerProperties producerProperties) {
            this.bindingName = bindingName;
            this.producerProperties = producerProperties;
            ConfigurableListableBeanFactory beanFactory =
                    RedisMessageChannelBinder.this.getBeanFactory();
            this.setBeanFactory(beanFactory);
            // this.partitionHandler = new PartitionHandler(beanFactory, getEvaluationContext(),
            // partitionSelector, producerProperties);
            this.partitionHandler =
                    new PartitionHandler(getEvaluationContext(), producerProperties, beanFactory);

            refreshChannelAdapters();
        }

        @Override
        protected void handleMessageInternal(Message<?> message) {
            MessageValues transformed = new MessageValues(message);

            if (producerProperties.isPartitioned()) {
                transformed.put(
                        BinderHeaders.PARTITION_HEADER,
                        this.partitionHandler.determinePartition(message));
            }
            byte[] messageToSend = null;
            if (HeaderMode.embeddedHeaders.equals(producerProperties.getHeaderMode())) {
                messageToSend =
                        EmbeddedHeaderUtils.embedHeaders(
                                transformed, RedisMessageChannelBinder.this.headersToMap);
            } else {
                Object contentType = message.getHeaders().get(MessageHeaders.CONTENT_TYPE);
                if (contentType != null
                        && !contentType.equals(MediaType.APPLICATION_OCTET_STREAM_VALUE)) {
                    logger.error(
                            "Raw mode supports only "
                                    + MediaType.APPLICATION_OCTET_STREAM_VALUE
                                    + " content type"
                                    + message.getPayload().getClass());
                }
                if (message.getPayload() instanceof byte[]) {
                    messageToSend = (byte[]) message.getPayload();
                } else {
                    logger.error(
                            "Raw mode supports only byte[] payloads but value sent was of type "
                                    + message.getPayload().getClass());
                }
            }

            if (messageToSend != null) {
                refreshChannelAdapters();
                for (RedisQueueOutboundChannelAdapter adapter : adapters.values()) {
                    adapter.handleMessage(
                            (MessageBuilder.withPayload(messageToSend)
                                    .copyHeaders(transformed)
                                    .build()));
                }
            }
        }

        private void refreshChannelAdapters() {
            Set<String> groups =
                    redisOperations
                            .boundZSetOps(CONSUMER_GROUPS_KEY_PREFIX + bindingName)
                            .rangeByScore(1, Double.MAX_VALUE);
            for (String group : groups) {
                if (!adapters.containsKey(group)) {
                    String channel = String.format("%s.%s", this.bindingName, group);
                    adapters.put(group, createProducerEndpoint(channel, producerProperties));
                }
            }
        }
    }

    private class ReceivingHandler extends AbstractReplyProducingMessageHandler {

        private final ConsumerProperties consumerProperties;

        public ReceivingHandler(ConsumerProperties consumerProperties) {
            super();
            this.consumerProperties = consumerProperties;
            this.setBeanFactory(RedisMessageChannelBinder.this.getBeanFactory());
        }

        @SuppressWarnings("unchecked")
        @Override
        protected Object handleRequestMessage(Message<?> requestMessage) {
            if (HeaderMode.embeddedHeaders.equals(consumerProperties.getHeaderMode())) {
                return new MessageValues(requestMessage).toMessage(getMessageBuilderFactory());
            } else {
                return requestMessage;
            }
        }

        @Override
        protected boolean shouldCopyRequestHeaders() {
            // prevent returned message from being copied in superclass
            return false;
        }
    }

    /** Provides concurrency by creating a list of message-driven endpoints. */
    private class CompositeRedisQueueMessageDrivenEndpoint extends MessageProducerSupport {

        private final List<RedisQueueMessageDrivenEndpoint> consumers =
                new ArrayList<RedisQueueMessageDrivenEndpoint>();

        public CompositeRedisQueueMessageDrivenEndpoint(String queueName, int concurrency) {
            for (int i = 0; i < concurrency; i++) {
                RedisQueueMessageDrivenEndpoint adapter =
                        new RedisQueueMessageDrivenEndpoint(queueName, connectionFactory);
                adapter.setBeanFactory(RedisMessageChannelBinder.this.getBeanFactory());
                adapter.setSerializer(null);
                adapter.setBeanName("inbound." + queueName + "." + i);
                this.consumers.add(adapter);
            }
            this.setBeanFactory(RedisMessageChannelBinder.this.getBeanFactory());
        }

        @Override
        protected void onInit() {
            for (RedisQueueMessageDrivenEndpoint consumer : consumers) {
                consumer.afterPropertiesSet();
            }
        }

        @Override
        protected void doStart() {
            for (RedisQueueMessageDrivenEndpoint consumer : consumers) {
                consumer.start();
            }
        }

        @Override
        protected void doStop() {
            for (RedisQueueMessageDrivenEndpoint consumer : consumers) {
                consumer.stop();
            }
        }

        @Override
        public void setOutputChannel(MessageChannel outputChannel) {
            for (RedisQueueMessageDrivenEndpoint consumer : consumers) {
                consumer.setOutputChannel(outputChannel);
            }
        }

        @Override
        public void setErrorChannel(MessageChannel errorChannel) {
            for (RedisQueueMessageDrivenEndpoint consumer : consumers) {
                consumer.setErrorChannel(errorChannel);
            }
        }
    }
}
