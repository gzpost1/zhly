package cn.cuiot.dmp.pay.service.service.service;

import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.pay.service.service.entity.SysPayChannelSetting;
import cn.cuiot.dmp.pay.service.service.vo.WeChatConfigVo;
import com.chinaunicom.yunjingtech.httpclient.auth.AutoUpdateCertificatesVerifier;
import com.chinaunicom.yunjingtech.httpclient.auth.PrivateKeySigner;
import com.chinaunicom.yunjingtech.httpclient.auth.WechatPay2Credentials;
import com.chinaunicom.yunjingtech.httpclient.util.PemUtil;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.security.PrivateKey;
import java.util.HashMap;

/**
 * @author huq
 * @ClassName CertificatesVerifierCacheService
 * @Date 2021/7/20 11:10
 **/
@Service
public class CertificatesVerifierCacheService {

    private HashMap<String, AutoUpdateCertificatesVerifier> certificatesVerifierHashMap = new HashMap<>();

    public AutoUpdateCertificatesVerifier refresh(SysPayChannelSetting setting) {
        AutoUpdateCertificatesVerifier verifier = getAutoUpdateCertificatesVerifier(setting);
        certificatesVerifierHashMap.put(setting.getPayMchId(), verifier);
        return verifier;
    }

    public AutoUpdateCertificatesVerifier getAutoUpdateCertificatesVerifierByPayMchId(SysPayChannelSetting setting) {
        AutoUpdateCertificatesVerifier verifier = certificatesVerifierHashMap.get(setting.getPayMchId());
        if (verifier == null) {
            verifier = refresh(setting);
        }
        return verifier;
    }

    private AutoUpdateCertificatesVerifier getAutoUpdateCertificatesVerifier(SysPayChannelSetting setting) {
        WeChatConfigVo config = JsonUtil.readValue(setting.getSettingConfig(), WeChatConfigVo.class);
        AutoUpdateCertificatesVerifier verifier = new AutoUpdateCertificatesVerifier();
        PrivateKey privateKey = PemUtil.loadPrivateKey(new ByteArrayInputStream(setting.getPrivateKeyBlob()));
        verifier.setVerifier(new WechatPay2Credentials(setting.getPayMchId(),
                        new PrivateKeySigner(config.getSerialNo(), privateKey)),
                config.getApiV3key().getBytes());
        return verifier;
    }
}
