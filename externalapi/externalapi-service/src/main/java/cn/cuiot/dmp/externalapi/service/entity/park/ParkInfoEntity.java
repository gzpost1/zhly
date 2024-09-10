package cn.cuiot.dmp.externalapi.service.entity.park;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 停车场数据
 * @author pengjian
 * @since 2024-09-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_park_info")
public class ParkInfoEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 车场id
     */
    @TableId("park_id")
    private Integer parkId;

    /**
     * 车场名称
     */
    private String parkName;

    /**
     * 总车位数
     */
    private Integer totalSpaceNum;


    /**
     * 楼盘id
     */
    private Long communityId;

    /**
     * 楼盘名称
     */
    @TableField(exist = false)
    private String communityName;


    /**
     * 空闲车位数
     */
    private Integer freeSpaceNum;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date  updateTime;

    /**
     * 更新人
     */
    private Long updateUser;

    /**
     * 企业id
     */
    private Long companyId;

    /**
     * 已用车位
     */
    private Integer usedSpaceNum;
}
