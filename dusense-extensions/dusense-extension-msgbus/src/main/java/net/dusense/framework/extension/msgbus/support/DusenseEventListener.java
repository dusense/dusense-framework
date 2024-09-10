package net.dusense.framework.extension.msgbus.support;

import net.dreamlu.mica.auto.annotation.AutoListener;
import net.dusense.framework.extension.msgbus.DusenseEvent;
import org.springframework.context.ApplicationListener;

/**
 * @author [ saily ]
 * @version V3.0
 * @email sailyfirm@gmail.com
 * @date 2022/09/25
 */
@AutoListener
public class DusenseEventListener implements ApplicationListener<DusenseEvent> {

    @Override
    public void onApplicationEvent(DusenseEvent event) {
        // 处理接收到的事件
        System.out.println("接收到事件：" + event.getSource());
    }
}
