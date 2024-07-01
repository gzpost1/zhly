package cn.cuiot.dmp.digitaltwin.provider.rocketmq;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface DigitaltwinMsgChannel {
    /**
     * 格物消防-设备信息
     */
    String GWFIREFIGHTDEVICEOUTPUT = "gwFirefightDeviceOutput";

    /**
     * 格物消防-设备状态
     */
    String GWFIREFIGHTDEVICESTATUSOUTPUT = "gwFirefightDeviceStatusOutput";

    /**
     * 格物消防-实时报警
     */
    String GWFIREFIGHTREALTIMEALARMOUTPUT = "gwFirefightRealTimeAlarmOutput";

    /**
     * 格物消防-报警确认
     */
    String GWFIREFIGHTALARMCONFIRMATIONOUTPUT = "gwFirefightAlarmConfirmationOutput";

    /**
     * 格物消防-设备监控
     */
    String GWFIREFIGHTDEVICEMONITOROUTPUT = "gwFirefightDeviceMonitorOutput";

    @Output(GWFIREFIGHTDEVICEOUTPUT)
    MessageChannel gwFirefightDeviceOutput();

    @Output(GWFIREFIGHTDEVICESTATUSOUTPUT)
    MessageChannel gwFirefightDeviceStatusOutput();

    @Output(GWFIREFIGHTREALTIMEALARMOUTPUT)
    MessageChannel gwFirefightRealTimeAlarmOutput();

    @Output(GWFIREFIGHTALARMCONFIRMATIONOUTPUT)
    MessageChannel gwFirefightAlarmConfirmationOutput();

    @Output(GWFIREFIGHTDEVICEMONITOROUTPUT)
    MessageChannel gwFirefightDeviceMonitorOutput();
}
