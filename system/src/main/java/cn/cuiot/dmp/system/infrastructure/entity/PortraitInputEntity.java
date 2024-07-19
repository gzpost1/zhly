package cn.cuiot.dmp.system.infrastructure.entity;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import cn.cuiot.dmp.base.infrastructure.persistence.mapper.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 人像录入
 * @author pengjian
 * @since 2024-07-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "tb_portrait_input", autoResultMap = true)
public class PortraitInputEntity extends YjBaseEntity {


    /**
     * 主键id
     */
    @TableId("id")
    private Long id;


    /**
     * url
     */
    private String url;

    /**
     * 名称
     */
    private String name;
    /**
     * 手机号
     */
    private String phone;


    /**
     * 卡号
     */
    private String cardNo;


    /**
     * 身份证号
     */
    private String idCardNo;


    /**
     * 密码
     */
    private String password;


    /**
     * 备注
     */
    private String tag;





}
