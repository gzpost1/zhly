package cn.cuiot.dmp.externalapi.service.vendor.yfwaterelectricity.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 宇泛设备 指令请求
 *
 * @author xiaotao
 * @date 2024/8/21 14:13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class EquipmentCommandControllerReq  implements Serializable {


    private static final long serialVersionUID = -8685860922401936038L;

    private String deviceNo;

    private String source;

}
