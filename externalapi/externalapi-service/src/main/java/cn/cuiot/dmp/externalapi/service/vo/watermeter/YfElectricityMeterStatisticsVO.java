package cn.cuiot.dmp.externalapi.service.vo.watermeter;


import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 智慧物联-宇泛电表-同步天水表用量数据
 * </p>
 *
 * @author xiaotao
 * @since 2024-10-11
 */
@Data
@Accessors(chain = true)
public class YfElectricityMeterStatisticsVO implements Serializable {

    private static final long serialVersionUID = 302942754365153780L;


    private Long id;

    /**
     * 电表编码id
     */
    private Long meterId;

    /**
     * 设备名称
     */
    @Excel(name = "设备名称", orderNum = "0", width = 20)
    private String deviceName;

    /**
     * 设备序列号
     */
    @Excel(name = "设备序列号", orderNum = "1", width = 20)
    private String deviceNo;

    /**
     * 楼盘名称
     */
    @Excel(name = "所属楼盘", orderNum = "2", width = 20)
    private String buildingName;

    /**
     * 数据记录时间
     */
    private LocalDateTime recordTime;

    /**
     * 设备用量实时用量
     */
    @Excel(name = "实时数据", orderNum = "3", width = 20)
    private BigDecimal amount;

    /**
     * 设备总用量
     */
    @Excel(name = "总用量", orderNum = "4", width = 20)
    private BigDecimal amountTotal;



}
