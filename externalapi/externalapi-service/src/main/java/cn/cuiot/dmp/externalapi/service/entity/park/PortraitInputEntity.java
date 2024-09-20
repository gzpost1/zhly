package cn.cuiot.dmp.externalapi.service.entity.park;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import cn.cuiot.dmp.base.infrastructure.persistence.mapper.BaseEntity;
import cn.cuiot.dmp.externalapi.service.query.PortraitAccessDto;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

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
     * 人员分组id
     */
    private Long personGroupId;

    /**
     * 分组名称
     */
    @TableField(exist = false)
    private String personGroupName;

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

    /**
     * 权限信息
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<PortraitAccessDto> portraitAccess;

    /**
     * 人像创建主体编码
     */
    private String admitGuid;

    /**
     * 企业信息
     */
    private Long companyId;
    /**
     * 人像标识
     */
    private String faceGuid;
}
