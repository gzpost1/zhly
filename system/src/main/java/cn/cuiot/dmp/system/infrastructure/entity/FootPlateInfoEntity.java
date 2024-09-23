package cn.cuiot.dmp.system.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
