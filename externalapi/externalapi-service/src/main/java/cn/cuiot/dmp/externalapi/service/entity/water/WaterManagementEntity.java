package cn.cuiot.dmp.externalapi.service.entity.water;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 水表管理
 * @author pengjian
 * @since 2024-09-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_water_management")
public class WaterManagementEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId("id")
    private String id;
    /**
     * 水表imei
     */
    @Excel(name = "水表imei", orderNum = "3", width = 20)
    private String wsImei;


    /**
     * 水表imsi
     */
    @Excel(name = "水表imsi", orderNum = "2", width = 20)
    private String wsImsi;


    /**
     * 信号质量
     */
    @Excel(name = "信号质量", orderNum = "5", width = 20)
    private String wsCsq;


    /**
     * 电池电压
     */
    @Excel(name = "电池电压", orderNum = "6", width = 20)
    private String wsBatteryvoltage;


    /**
     * 累计水量
     */
    @Excel(name = "累计水量", orderNum = "4", width = 20)
    private String wsCumulativeamount;


    /**
     * 阀门状态
     */
    @Excel(name = "阀门状态", orderNum = "7", width = 20)
    private String valveStatus;


    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Excel(name = "上报时间",orderNum = "8",  width = 20,exportFormat = "yyyy-MM-dd HH:mm:ss")
    private String createDate;


    /**
     * 更新时间
     */
    private Date updateTime;


    /**
     * 更新人
     */
    private Long updateUser;

    /**
     * 企业id
     */
    private Long companyId;

    /**
     * 楼盘id
     */
    private Long communityId;

    /**
     * 楼盘名称
     */
    @TableField(exist = false)
    @Excel(name = "所属楼盘", orderNum = "1", width = 20)
    private String communityName;

    /**
     * 水表名称
     */
    @Excel(name = "水表名称", orderNum = "0", width = 20)
    private String waterName;

}
