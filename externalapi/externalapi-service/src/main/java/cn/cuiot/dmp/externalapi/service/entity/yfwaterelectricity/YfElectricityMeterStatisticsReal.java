package cn.cuiot.dmp.externalapi.service.entity.yfwaterelectricity;

import java.math.BigDecimal;
import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 智慧物联-宇泛电表-同步实时水表用量数据
 * </p>
 *
 * @author xiaotao
 * @since 2024-10-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_yf_electricity_meter_statistics_real")
public class YfElectricityMeterStatisticsReal extends YjBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 编码
     */
    @TableId("id")
    private Long id;

    /**
     * 电表编码id
     */
    @TableField("meter_id")
    private Long meterId;

    /**
     * 设备序列号
     */
    @TableField("device_no")
    private String deviceNo;

    /**
     * 数据记录时间
     */
    @TableField("record_time")
    private LocalDateTime recordTime;

    /**
     * 设备用量
     */
    @TableField("amount")
    private BigDecimal amount;

    /**
     * 停启用状态（0停用，1启用）
     */
    @TableField("status")
    private Boolean status;


}
