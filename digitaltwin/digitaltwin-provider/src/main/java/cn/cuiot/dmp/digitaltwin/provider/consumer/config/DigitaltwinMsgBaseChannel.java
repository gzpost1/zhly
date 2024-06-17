package cn.cuiot.dmp.digitaltwin.provider.consumer.config;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * 接收消息队列名称
 *
 * @Author: zc
 * @Date: 2024-06-14
 */
public interface DigitaltwinMsgBaseChannel {
    /**
     * 格物消防-设备信息
     */
    String GWFIREFIGHTDEVICEINPUT = "gwFirefightDeviceInput";

    /**
     * 格物消防-设备状态
     */
    String GWFIREFIGHTDEVICESTATUSINPUT = "gwFirefightDeviceStatusInput";

    /**
     * 格物消防-实时报警
     */
    String GWFIREFIGHTREALTIMEALARMINPUT = "gwFirefightRealTimeAlarmInput";

    /**
     * 格物消防-报警确认
     */
    String GWFIREFIGHTALARMCONFIRMATIONINPUT = "gwFirefightAlarmConfirmationInput";

    /**
     * 格物消防-设备监控
     */
    String GWFIREFIGHTDEVICEMONITORINPUT = "gwFirefightDeviceMonitorInput";

    @Input(GWFIREFIGHTDEVICEINPUT)
    SubscribableChannel gwFirefightDeviceInput();

    @Input(GWFIREFIGHTDEVICESTATUSINPUT)
    SubscribableChannel gwFirefightDeviceStatusInput();

    @Input(GWFIREFIGHTREALTIMEALARMINPUT)
    SubscribableChannel gwFirefightRealTimeAlarmInput();

    @Input(GWFIREFIGHTALARMCONFIRMATIONINPUT)
    SubscribableChannel gwFirefightAlarmConfirmationInput();

    @Input(GWFIREFIGHTDEVICEMONITORINPUT)
    SubscribableChannel gwFirefightDeviceMonitorInput();
}
