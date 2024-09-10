package net.dusense.framework.extension.msgbus;

import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.bus.event.RemoteApplicationEvent;

/**
 * namespace:object#relation@subject videos:cat.mp4#view@felix
 *
 * @author [ saily ]
 * @version V3.0
 * @email sailyfirm@gmail.com
 * @date 2022/09/23
 */
@Setter
@Getter
public class DusenseEvent extends RemoteApplicationEvent {

    private String channel;

    public DusenseEvent(Object source, String originService, String destination) {
        super(source, originService, destination);
    }
}
