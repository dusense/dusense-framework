# 消息总线逻辑 默认是redis做为分布式消息总线

消息驱动处理逻辑，默认是redis 来作为消息驱动处理
可以支持更多的逻辑

1、out-生产者、in-消费者 
2、消费函数如果叫做abc()，则你的默认destination=abc-in-0，想要修改，参考配置文件，记得两个-，不要以为是注释 
3、如果只有java.util.function.[Supplier/function/Customer]类型的单个bean，则不需要指定definition，如果你有多个，则需要指定，使用分号隔离 
4、如果你想做连接处理，definition使用|分隔 
5、单个Bean可以被自动发现并装载，最后附上官网原文

```
## 生产者
spring.cloud.function.definition=<functionDefinition>
## 消费处理的逻辑
spring.cloud.stream.bindings.<channelName>.destination=<destinationName>


```
```
spring.cloud.stream.bindings.<channelName>.destination=<destinationName>
spring.cloud.stream.bindings.<channelName>.contentType=<contentType>
spring.cloud.stream.bindings.<channelName>.binder=<binderType>
spring.cloud.stream.bindings.<channelName>.group=<group>
spring.cloud.stream.bindings.<channelName>.defaultCodecType=<defaultCodecType>
spring.cloud.stream.bindings.<channelName>.dltChannel=<dltChannel>

spring.cloud.stream.bindings.<channelName>.consumer.headerMode=<headerMode>
spring.cloud.stream.bindings.<channelName>.consumer.partitioned=true
spring.cloud.stream.bindings.<channelName>.consumer.instanceCount=3
spring.cloud.stream.bindings.<channelName>.consumer.instanceIndex=0
spring.cloud.stream.bindings.<channelName>.consumer.concurrency=10
spring.cloud.stream.bindings.<channelName>.consumer.maxAttempts=3

spring.cloud.stream.bindings.<channelName>.producer.partitionKeyExpression=headers['partitionKey']
spring.cloud.stream.bindings.<channelName>.producer.partitionCount=3
spring.cloud.stream.bindings.<channelName>.producer.requirePartitionKey=true
spring.cloud.stream.bindings.<channelName>.producer.headerMode=raw
spring.cloud.stream.bindings.<channelName>.producer.transactional=true

spring.cloud.stream.defaultBinder=<defaultBinder>
spring.cloud.stream.defaultContentType=<defaultContentType>
spring.cloud.stream.defaultHeaderMode=<defaultHeaderMode>
spring.cloud.stream.defaultProducerType=<defaultProducerType>
spring.cloud.stream.defaultConsumerType=<defaultConsumerType>
spring.cloud.stream.defaultCodecType=<defaultCodecType>
spring.cloud.stream.defaultEncoding=<defaultEncoding>
spring.cloud.stream.defaultGroup=<defaultGroup>
spring.cloud.stream.defaultMaxAttempts=<defaultMaxAttempts>
spring.cloud.stream.defaultPartitionKeyExpression=<defaultPartitionKeyExpression>
spring.cloud.stream.defaultPartitionCount=<defaultPartitionCount>
spring.cloud.stream.defaultRequiredGroup=<defaultRequiredGroup>
spring.cloud.stream.defaultRequiredPartitionKey=<defaultRequiredPartitionKey>
spring.cloud.stream.defaultRequiredPartitionKeyExpression=<defaultRequiredPartitionKeyExpression>

spring.cloud.function.definition=<functionDefinition>

```