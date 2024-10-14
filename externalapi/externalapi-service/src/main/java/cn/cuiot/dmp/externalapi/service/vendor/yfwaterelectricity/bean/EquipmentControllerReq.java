package cn.cuiot.dmp.externalapi.service.vendor.yfwaterelectricity.bean;

import cn.cuiot.dmp.common.anno.BigDecimalFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 宇泛电表 设备请求参数
 *
 * @author xiaotao
 * @date 2024/8/21 14:13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class EquipmentControllerReq implements Serializable {

    private static final long serialVersionUID = 2166531178005441847L;


    /**
     * 扩展字段
     */
    private Addition addition;

    /**
     * 设备序列号
     */
    private String deviceNo;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 设备来源，电表source=311012
     */
    private String source;


    /**
     * 阀控指令信息
     *
     * @author gxp
     * @date 2024/8/26 11:28
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(toBuilder = true)
    public static class Addition implements Serializable {

        private static final long serialVersionUID = -7782077165963638391L;

        /**
         * 倍率，固定为1.0
         */

        @BigDecimalFormat("#.#")
        private BigDecimal beilv;

        /**
         * 固定为1
         */
        private Integer closeData;

        /**
         * 费率，固定为1
         */
        private Integer feilvType;

        /**
         * 采集器编号，电表采集器编号和表地址(设备序列号不
         * 一致，需由厂商提供
         */
        private String ip;

        /**
         * 1是NB设备，固定为1
         */
        private Integer isNb;

        /**
         * 抄表项，该设备采集哪些数据，比如电表的读数、电
         * 压、电流、功率；固定为"108,1235,1296,32,60"
         */
        private String items;

        /**
         * 固定为"级别1"
         */
        private String level1;

        /**
         * 固定为"级别2"
         */
        private String level2;

        /**
         * 固定为"级别3"
         */
        private String level3;

        /**
         * 固定为"级别4"
         */
        private String level4;

        /**
         * 表计类型，电表固定为"2"
         */
        private String meterTypeId;

        /**
         * 1旧的，2新的，固定为2
         */
        private Integer nbDianxinYuming;

        /**
         * 采集器端口，固定为"10001"
         */
        private String port;

        /**
         * 单价，固定为1.2
         */
        @BigDecimalFormat("#.#")
        private BigDecimal price;

        /**
         * 费率 1 单价，固定为1.2
         */
        @BigDecimalFormat("#.#")
        private BigDecimal price1;

        /**
         * 费率 2 单价，固定为1.8
         */
        @BigDecimalFormat("#.#")
        private BigDecimal price2;

        /**
         * 费率 3 单价，固定为1.8
         */
        @BigDecimalFormat("#.#")
        private BigDecimal price3;

        /**
         * 费率 4 单价，固定为1.2
         */
        @BigDecimalFormat("#.#")
        private BigDecimal price4;

        /**
         * 表计型号，电表固定为15
         */
        private Integer ptId;

        /**
         * 固定为"用户名"
         */
        private String userName;

    }

}