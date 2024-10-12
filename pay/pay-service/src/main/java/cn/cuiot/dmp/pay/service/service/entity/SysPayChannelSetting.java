package cn.cuiot.dmp.pay.service.service.entity;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import cn.cuiot.dmp.base.infrastructure.persistence.handler.EncryptPhoneTypeHandler;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.pay.service.service.dto.PayChannelSettingDto;
import cn.cuiot.dmp.pay.service.service.enums.PayChannelEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;


/**
 * <p>
 * 支付渠道配置表
 *
 * </p>
 *
 * @author wuyongchong
 * @since 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "tb_pay_channel_setting", autoResultMap = true)
public class SysPayChannelSetting extends YjBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("id")
    private Long id;

    /**
     * 企业ID
     */
    private Long orgId;

    /**
     * 支付渠道名称
     */
    private String name;

    /**
     * 小程序appId
     */
    private String appId;


    /**
     * 商户号
     */
    @TableField(typeHandler = EncryptPhoneTypeHandler.class)
    private String payMchId;

    /**
     * 商户名称名称
     */
    private String payMchName;

    /**
     * 支付手续费
     */
    private BigDecimal charge;


    /**
     * 对应的文本型支付秘钥
     */
    @TableField(typeHandler = EncryptPhoneTypeHandler.class)
    private String settingConfig;


    /**
     * 文件类型私钥
     */
    private byte[] privateKeyBlob;


    /**
     * 文件类型公钥
     */
    private byte[] publicKeyBlob;


    /**
     * 文件类型秘钥1（备用）
     */
    private byte[] blob1;


    /**
     * 文件类型秘钥2（备用）
     */
    private byte[] blob2;


    /**
     * 状态：0-停用，1-启用
     */
    private Byte status;
    /**
     * 支付渠道
     */
    private Integer payChannel;
    /**
     * 商户模式
     */
    private Byte mchType;

    /**
     * 更新基础信息
     *
     * @param settingDto
     */
    public void update(PayChannelSettingDto settingDto) {
        this.status = EntityConstants.ENABLED;
        this.payChannel = settingDto.getPayChannel();
        this.mchType = settingDto.getMchType();
        this.orgId = LoginInfoHolder.getCurrentOrgId();
        this.appId = settingDto.getAppId();
        this.payMchId = settingDto.getPayMchId();
        this.payMchName = settingDto.getPayMchName();
        this.charge = settingDto.getCharge();
        if (Objects.isNull(this.privateKeyBlob) && PayChannelEnum.isWeChat(this.getPayChannel()) && Objects.isNull(settingDto.getPrivateKeyFile())) {
            //如果是微信支付，且证书从来都没配置过，本次必须上传
            throw new BusinessException(ResultCode.REQ_PARAM_ERROR, "证书不能为空");
        }
        if (null != settingDto.getPrivateKeyFile()) {
            this.privateKeyBlob = fileToByte(settingDto.getPrivateKeyFile());
        }
        if (null != settingDto.getPublicKeyFile()) {
            this.publicKeyBlob = fileToByte(settingDto.getPublicKeyFile());
        }
        this.settingConfig = settingDto.getSettingConfig();
    }
    private byte[] fileToByte(MultipartFile file) {
        try {
            byte[] caFile = file.getBytes();
            return caFile;
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(ResultCode.REQ_PARAM_ERROR, "证书格式不正确，转换失败");
        }
    }
}
