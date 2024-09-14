package cn.cuiot.dmp.externalapi.service.vendor.gw.bean.req;

import lombok.Data;

/**
 * 设备-调用设备服务req
 *
 * @Author: zc
 * @Date: 2024-09-09
 */
@Data
public class InvokeDeviceServiceReq {

    /**
     * 【必选】
     * 要调用的服务标识符。
     * 设备的服务key，可在控制台中设备所属的产品的功能定义中查看标识符定义
     */
    private String key;

    /**
     * 【必选】
     * 准备调用的服务入参信息，数据格式为 JSON String。调用服务格式为参数标识符key:参数值value，多个参数用英文逗号隔开。
     * 比如，"arguments":"{ \"param \":1}"。
     * 入参数为空时，请填入 arguments ={}。
     */
    private String arguments = "{}";

    /**
     * 【可选】
     * 产品的productKey。创建产品时，雁飞·格物DMP平台为该产品颁发的全局唯一标识。
     */
    private String productKey = "";

    /**
     * 【可选】
     * 设备的deviceKey，由用户在添加设备时指定但需要满足该账户下唯一。
     */
    private String deviceKey = "";

    /**
     * 【可选】
     * 雁飞·格物DMP平台唯一标识码。
     * 说明 如果传入该参数，则无需传入productKey和deviceKey。iotId作为设备唯一标识码，和productKey与deviceKey组合是一一对应的关系。如果您同时传入iotId和productKey与deviceKey组合，则以iotId为准。
     */
    private String iotId;

    /**
     * 【必选】
     * 同步调用为优先命令。
     * 异步调用，优先命令只有账户是商用账户时有效
     * true:优先命令，会优先下发，低时延
     * false:非优先命令，排队下发
     * 同步调用为优先命令
     */
    private String priorityCommand = "true";
}
