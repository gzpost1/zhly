package cn.cuiot.dmp.externalapi.service.query.gw;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 格物门禁 人员管理
 *
 * @Author: zc
 * @Date: 2024-09-07
 */
@Data
public class GwEntranceGuardPersonCreateDto {
    /**
     * 人员姓名
     */
    @NotBlank(message = "请输入人员姓名")
    private String name;

    /**
     * 性别（1男，2女）
     */
    @NotNull(message = "请选择性别")
    private Byte sex;

    /**
     * 手机号
     */
    @NotBlank(message = "请输入手机号")
    private String phone;

    /**
     * 楼盘id
     */
    @NotNull(message = "请选择所属楼盘")
    private Long buildingId;

    /**
     * 分组id
     */
    @NotNull(message = "请选择人员分组")
    private Long personGroupId;

    /**
     * 时效类型（0：永久；1：自定义时效）
     */
    @NotNull(message = "请选择有效期类型")
    private Byte prescriptionType;

    /**
     * 时效开始日期
     */
    private Date prescriptionBeginDate;

    /**
     * 时效结束日期
     */
    private Date prescriptionEndDate;

    /**
     * 身份证号
     */
    private String idCardNo;

    /**
     * ic卡号
     */
    private String icCardNo;

    /**
     * 密码
     */
    private String password;

    /**
     * 人员照片
     */
    private String image;

    /**
     * 备注
     */
    private String deviceSecret;

    /**
     * 临时密钥id
     */
    private String kid;
}
