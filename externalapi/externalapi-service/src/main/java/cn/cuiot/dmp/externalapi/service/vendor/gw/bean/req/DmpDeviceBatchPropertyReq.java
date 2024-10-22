package cn.cuiot.dmp.externalapi.service.vendor.gw.bean.req;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: zc
 * @Date: 2024-09-06
 */
@Data
public class DmpDeviceBatchPropertyReq implements Serializable {

    private static final long serialVersionUID = -8860178496997816827L;

    /**
     * 产品的productKey。创建产品时，雁飞·格物DMP平台为该产品颁发的全局唯一标识。
     */
    private String productKey;

    /**
     * 设备的deviceKey。
     */
    private List<String> deviceKey;

    /**
     * 雁飞·格物DMP平台唯一标识码。说明 如果传入该参数，则无需传入productKey和deviceKey。iotId作为设备唯一标识码，
     * 和productKey与deviceKey组合是一一对应的关系。如果您同时传入iotId和productKey与deviceKey组合，则以iotId为准。iotId需属于所选产品。
     */
    private List<String> iotId;

    /**
     * 准备设置的属性信息，数据格式为 JSON String。设置属性格式为属性标识符key:属性值value，多个属性用英文逗号隔开。
     * 比如，设置中央温控器的设备开关属性：
     * 属性标识符为switch的开关属性，数据类型为bool，设置值为0（关）；
     * 那么，"items": "{\"switch\":0}"。
     */
    private String items;

    /**
     * 优先命令（true），只在账户是商用账户时有效。
     * true:优先命令，会优先下发，低时延。
     * false:非优先命令，普通队列。
     */
    private Boolean priorityCommand;

}
