package cn.cuiot.dmp.externalapi.service.entity.yfwaterelectricity;

import java.math.BigDecimal;
import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 智慧物联-宇泛电表
 * </p>
 *
 * @author xiaotao
 * @since 2024-10-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_yf_electricity_meter")
public class YfElectricityMeter extends YjBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 电表编码
     */
    @TableId("id")
    private Long id;


    /**
     * 企业id
     */
    @TableField("company_id")
    private Long companyId;

    /**
     * 设备名称
     */
    @TableField("name")
    private String name;

    /**
     * 楼盘id，主键
     */
    @TableField("building_id")
    private Long buildingId;

    /**
     * 设备来源，电表source=311012
     */
    @TableField("source")
    private String source;

    /**
     * 设备序列号
     */
    @TableField("device_no")
    private String deviceNo;

    /**
     * 抄表项，该设备采集哪些数据 108,1235,1296,32,60
     */
    @TableField("items")
    private String items;

    /**
     * 表计类型，电表固定为"2"
     */
    @TableField("meter_type_id")
    private Integer meterTypeId;

    /**
     * 表计型号，电表固定为15
     */
    @TableField("pt_id")
    private Integer ptId;

    /**
     * 采集器编号，电表采集器编号和表地址 设备序列号不一致，需由厂商提供 
     */
    @TableField("ip")
    private String ip;

    /**
     * 固定为"级别1"
     */
    @TableField("level1")
    private String level1;

    /**
     * 固定为"级别2"
     */
    @TableField("level2")
    private String level2;

    /**
     * 固定为"级别3"
     */
    @TableField("level3")
    private String level3;

    /**
     * 固定为"级别4"
     */
    @TableField("level4")
    private String level4;

    /**
     * 固定为"用户名"
     */
    @TableField("user_name")
    private String userName;

    /**
     * 采集器端口，固定为"10001"
     */
    @TableField("port")
    private String port;

    /**
     * 倍率，固定为1.0
     */
    @TableField("beilv")
    private BigDecimal beilv;

    /**
     * 单价，固定为1.2
     */
    @TableField("price")
    private BigDecimal price;

    /**
     * 费率 1 单价，固定为1.2
     */
    @TableField("price1")
    private BigDecimal price1;

    /**
     * 费率 2 单价，固定为1.8
     */
    @TableField("price2")
    private BigDecimal price2;

    /**
     * 费率 3 单价，固定为1.8
     */
    @TableField("price3")
    private BigDecimal price3;

    /**
     * 费率 4 单价，固定为1.2
     */
    @TableField("price4")
    private BigDecimal price4;

    /**
     * 费率，固定为1
     */
    @TableField("feilv_type")
    private Integer feilvType;

    /**
     * 固定为1
     */
    @TableField("close_data")
    private Integer closeData;

    /**
     * 1是NB设备，固定为1
     */
    @TableField("is_nb")
    private Integer isNb;

    /**
     * 1旧的，2新的，固定为2
     */
    @TableField("nb_dianxin_yuming")
    private Integer nbDianxinYuming;

    /**
     * 停启用状态（0停用，1启用）
     */
    @TableField("status")
    private Byte status;


}
