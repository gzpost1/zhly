package cn.cuiot.dmp.common.bean.external;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 水表（宇泛）-对接系统参数
 *
 * @date 2024/8/22 9:27
 * @author gxp
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class YFWaterMeterBO extends ExternalapiBaseStatusBO {


    /**
     * projectGuid
     */
    private String projectGuid;
}
