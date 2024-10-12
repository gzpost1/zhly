package cn.cuiot.dmp.externalapi.service.vendor.yfwaterelectricity.bean;

import cn.cuiot.dmp.common.constant.ErrorCode;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.externalapi.service.vendor.watermeter.constant.WaterMeterConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 宇泛水电表阀响应
 *
 * @date 2024/8/21 14:00
 * @author xiaotao
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class MeterCommandControlResp<T> implements Serializable {

    private static final long serialVersionUID = -6210571360260264923L;

    private String result;

    private String code;

    private String msg;

    private Boolean success;

    private T data;

    public MeterCommandControlResp(String code, String msg){
        this.msg = msg;
        this.code = code;
    }

    public static <T> MeterCommandControlResp<T> error(ErrorCode errorCode) {
        return new MeterCommandControlResp<T>(errorCode.getCode(), errorCode.getMessage());
    }

}
