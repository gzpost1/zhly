package cn.cuiot.dmp.externalapi.service.entity.hik;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 海康-人员信息
 *
 * @Author: zc
 * @Date: 2024-10-09
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("tb_haikang_person")
public class HikPersonEntity extends YjBaseEntity {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 企业id
     */
    private Long companyId;

    /**
     * 人员名称，1~32个字符；不能包含特殊字符
     */
    private String personName;

    /**
     * 性别，1：男；2：女；0：未知
     */
    private String gender;

    /**
     * 所属组织标识，必须是已存在组织
     */
    private String orgIndexCode;

    /**
     * 证件类型
     */
    private String certificateType;

    /**
     * 证件号码，1-20位数字字母
     */
    private String certificateNo;

    /**
     * 手机号，1-20位数字
     */
    private String phoneNo;

    /**
     * 工号，1-32个字符
     */
    private String jobNo;

    /**
     * 人员照片
     */
    private String faceData;

    /**
     * 人员照片状态（0:未添加，1:已添加）
     */
    private Byte faceDataStatus;

    /**
     * 权限有效期类型（0:长期有效；1:自定义有效期）
     */
    private Byte validityType;

    /**
     * 自定义有效期-开始时间
     */
    private LocalDateTime beginTime;

    /**
     * 自定义有效期-结束时间
     */
    private LocalDateTime endTime;
}
