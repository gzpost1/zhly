package cn.cuiot.dmp.externalapi.service.entity.gw;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 联通格物门禁-人员管理(GwEntranceGuardPersonEntity)实体类
 *
 * @Author: zc
 * @Date: 2024-09-07
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("tb_gw_entrance_guard_person")
public class GwEntranceGuardPersonEntity extends YjBaseEntity {
    private static final long serialVersionUID = 801527900202039486L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 企业ID
     */
    @TableField(value = "company_id")
    private Long companyId;

    /**
     * 部门ID
     */
    @TableField(value = "dept_id")
    private Long deptId;

    /**
     * 人员姓名
     */
    @TableField(value = "`name`")
    private String name;

    /**
     * 性别（1男，2女）
     */
    @TableField(value = "sex")
    private Byte sex;

    /**
     * 手机号
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 楼盘id
     */
    @TableField(value = "building_id")
    private Long buildingId;

    /**
     * 分组id
     */
    @TableField(value = "person_group_id")
    private Long personGroupId;

    /**
     * 时效类型（0：永久；1：自定义时效）
     */
    @TableField(value = "prescription_type")
    private Byte prescriptionType;

    /**
     * 时效开始日期
     */
    @TableField(value = "prescription_begin_date")
    private Date prescriptionBeginDate;

    /**
     * 时效结束日期
     */
    @TableField(value = "prescription_end_date")
    private Date prescriptionEndDate;

    /**
     * 身份证号
     */
    @TableField(value = "id_card_no")
    private String idCardNo;

    /**
     * ic卡号
     */
    @TableField(value = "ic_card_no")
    private String icCardNo;

    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 人员照片
     */
    @TableField(value = "image")
    private String image;

    /**
     * 备注
     */
    @TableField(value = "device_secret")
    private String deviceSecret;

    /**
     * 授权 0-未授权；1-已授权
     */
    @TableField(value = "authorize")
    private Byte authorize;
}
