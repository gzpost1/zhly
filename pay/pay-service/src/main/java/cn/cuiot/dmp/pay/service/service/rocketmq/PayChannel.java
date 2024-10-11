package cn.cuiot.dmp.pay.service.service.rocketmq;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface PayChannel {

    String PAYSUCCESSOUTPUT = "paySuccessOutput";

    @Output(PAYSUCCESSOUTPUT)
    MessageChannel paySucessOutputOutput();

}
