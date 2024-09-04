package cn.cuiot.dmp.externalapi.service.vendor.park.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;

/**
 *
 * @author pengjian
 * @since 2024-09-03
 */
@Getter
@Setter
public class ParkInfoVo implements Serializable {

    @NotNull(message = "id不能为空")
    private Long id;
    /**
    * 车场名称
    */
    @Length(max = 100,message = "车场名称长度必须小于100位")
    private String parkName;

    /**
    * 车场id
    */
    private Integer parkId;

    /**
    * 总车位数
    */
    private Integer totalSpaceNum;

    /**
    * 楼盘id
    */
    private Long communityId;

    /**
    * 空闲车位数
    */
    private Integer freeSpaceNum;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
