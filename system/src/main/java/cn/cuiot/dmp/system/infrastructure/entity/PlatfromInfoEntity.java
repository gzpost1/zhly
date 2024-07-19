package cn.cuiot.dmp.system.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author pengjian
 * @since 2024-07-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_platfrom_info")
public class PlatfromInfoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId("id")
    private Long id;


    /**
     * 平台类型
     */
    private String platfromType;


    /**
     * 平台名称
     */
    private String platfromName;


    /**
     * 序号
     */
    private Integer sort;


    /**
     * AppKey
     */
    private String appKey;


    /**
     * AppSecret
     */
    private String appSecret;


    /**
     * projectGuid
     */
    private String projectGuid;



}
