package cn.cuiot.dmp.externalapi.service.entity.park;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author pengjian
 * @since 2024-07-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_foot_plate_info")
public class FootPlateInfoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("id")
    private Long id;


    /**
     * 平台名称
     */
    private String platformName;


}
