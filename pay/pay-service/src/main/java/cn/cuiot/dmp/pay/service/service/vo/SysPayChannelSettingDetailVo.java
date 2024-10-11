package cn.cuiot.dmp.pay.service.service.vo;

import cn.cuiot.dmp.common.utils.BeanMapper;
import cn.cuiot.dmp.pay.service.service.entity.SysPayChannelSetting;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 返回前端详情
 *
 * @author huq
 * @ClassName SysPayChannelSettingVo
 * @Date 2024/1/18 10:23
 **/
@Data
public class SysPayChannelSettingDetailVo implements Serializable {

    /**
     * 服务商支付号
     */
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
    private String settingConfig;


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
     * 是否已上传私钥证书
     */
    private Boolean priUpload = false;
    /**
     * 是否已上传公钥证书
     */
    private Boolean pubUpload = false;
    /**
     * 构造返回
     *
     * @param setting
     * @return
     */
    public static SysPayChannelSettingDetailVo toBuilder(SysPayChannelSetting setting, Integer payChannel) {
        SysPayChannelSettingDetailVo detailVo;
        if (null == setting) {
            detailVo = new SysPayChannelSettingDetailVo();
            detailVo.setPayChannel(payChannel);
            return detailVo;
        }
        detailVo = BeanMapper.map(setting, SysPayChannelSettingDetailVo.class);
        detailVo.setPriUpload(setting.getPrivateKeyBlob() != null && setting.getPrivateKeyBlob().length > 0);
        detailVo.setPubUpload(setting.getPublicKeyBlob() != null && setting.getPublicKeyBlob().length > 0);
        return detailVo;
    }
}
