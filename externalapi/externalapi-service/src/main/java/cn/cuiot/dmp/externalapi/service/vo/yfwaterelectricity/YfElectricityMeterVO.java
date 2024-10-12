package cn.cuiot.dmp.externalapi.service.vo.yfwaterelectricity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 智慧物联-宇泛电表
 * </p>
 *
 * @author xiaotao
 * @since 2024-10-10
 */
@Data
@Accessors(chain = true)
public class YfElectricityMeterVO implements Serializable {

    private static final long serialVersionUID = 8587196495165978707L;

    /**
     * 电表编码
     */
    @Excel(name = "设备ID", orderNum = "0", width = 20)
    private Long id;

    /**
     * 企业id
     */
    private Long companyId;

    /**
     * 设备名称
     */
    @Excel(name = "设备名称", orderNum = "1", width = 20)
    private String name;

    /**
     * 设备序列号
     */
    @Excel(name = "设备序列号", orderNum = "2", width = 20)
    private String deviceNo;


    /**
     * 楼盘id，主键
     */
    private Long buildingId;

    /**
     * 楼盘名称
     */
    @Excel(name = "楼盘名称", orderNum = "3", width = 20)
    private String buildingName;

    /**
     * 采集器编号，电表采集器编号和表地址 设备序列号不一致，需由厂商提供
     */
    @Excel(name = "采集器编号", orderNum = "4", width = 20)
    private String ip;


    /**
     * 设备来源，电表source=311012
     */
    private String source;


    /**
     * 抄表项，该设备采集哪些数据 108,1235,1296,32,60
     */
    @Excel(name = "抄表项", orderNum = "5", width = 20)
    private String items;

    /**
     * 表计类型，电表固定为"2"
     */
    @Excel(name = "表计类型", orderNum = "6", width = 20)
    private Integer meterTypeId;

    /**
     * 表计型号，电表固定为15
     */
    @Excel(name = "表计型号", orderNum = "7", width = 20)
    private Integer ptId;

    /**
     * 倍率，固定为1.0
     */
    @Excel(name = "倍率", orderNum = "8", width = 20)
    private BigDecimal beilv;


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
     * 固定为"用户名"
     */
    private String userName;

    /**
     * 采集器端口，固定为"10001"
     */
    private String port;


    /**
     * 单价，固定为1.2
     */
    @Excel(name = "单价", orderNum = "9", width = 20)
    private BigDecimal price;

    /**
     * 费率 1 单价，固定为1.2
     */
    @Excel(name = "费率1单价", orderNum = "10", width = 20)
    private BigDecimal price1;

    /**
     * 费率 2 单价，固定为1.8
     */
    @Excel(name = "费率2单价", orderNum = "11", width = 20)
    private BigDecimal price2;

    /**
     * 费率 3 单价，固定为1.8
     */
    @Excel(name = "费率3单价", orderNum = "12", width = 20)
    private BigDecimal price3;

    /**
     * 费率 4 单价，固定为1.2
     */
    @Excel(name = "费率4单价", orderNum = "13", width = 20)
    private BigDecimal price4;

    /**
     * 费率，固定为1
     */
    @Excel(name = "费率", orderNum = "14", width = 20)
    private Integer feilvType;

    /**
     * 固定为1
     */
    private Integer closeData;

    /**
     * 1是NB设备，固定为1
     */
    private Integer isNb;

    /**
     * 1旧的，2新的，固定为2
     */
    private Integer nbDianxinYuming;

    /**
     * 停启用状态（0停用，1启用）
     */
    private Boolean status;


}
