package cn.cuiot.dmp.pay.service.service.config;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.pay.service.service.entity.SysPayChannelSetting;
import cn.cuiot.dmp.pay.service.service.vo.WeChatConfigVo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chinaunicom.yunjingtech.httpclient.WechatPayHttpClientBuilder;
import com.chinaunicom.yunjingtech.httpclient.auth.AutoUpdateCertificatesVerifier;
import com.chinaunicom.yunjingtech.httpclient.auth.WechatPay2Validator;
import com.chinaunicom.yunjingtech.httpclient.bean.BaseReq;
import com.chinaunicom.yunjingtech.httpclient.util.PemUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

/**
 * @author huq
 * @ClassName HttpClient
 * @Date 2021/6/17 16:35
 **/
@Slf4j
@Data
public class PayHttpClient {
    public static final int SUCCESS_CODE_NO_RESPONSE = 204;
    public static final int SUCCESS_CODE = 200;
    public static final int CODE_404 = 404;



    /**
     * post请求
     *
     * @param url
     * @param query
     * @param err
     * @param <M>
     * @return
     */
    public <M extends BaseReq> String requestHttpPostObj(String url, M query, String err,
                                                         AutoUpdateCertificatesVerifier verifier, SysPayChannelSetting paySetting) {
        try {
            CloseableHttpClient httpClient = initHttpClient(verifier, paySetting);

            //接口URL
            HttpPost httpPost = new HttpPost(url);
            initHttpPost(query, httpPost, verifier);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            String responseStr = EntityUtils.toString(response.getEntity());
            JSONObject json = JSON.parseObject(responseStr);
            log.info("response" + responseStr);
            if (SUCCESS_CODE == response.getStatusLine().getStatusCode()) {
                return responseStr;
            } else if (CODE_404 == response.getStatusLine().getStatusCode()) {
                throw new BusinessException(ResultCode.WECHAT_PAY_NOT_FOUND);
            } else {
                throw new BusinessException(ResultCode.WECHAT_PAY_RESULT_ERROR, json.get("message").toString());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.warn(err + ": " + ex.getMessage());
            if (ex instanceof BusinessException) {
                throw (BusinessException) ex;
            } else {
                throw new BusinessException(ResultCode.WECHAT_PAY_INTERFACE_ERROR, err);
            }
        }
    }


    /**
     * 功能描述: 调用微信post方法，无返回
     *
     * @param url
     * @param query
     * @param err
     * @param verifier
     * @return void
     * @author huq
     * @date 2021/7/27 9:47
     **/
    public <M extends BaseReq> void requestHttpPostNoReturn(String url, M query, String err,
                                                            AutoUpdateCertificatesVerifier verifier, SysPayChannelSetting paySetting
                                                            ) {
        try {
            CloseableHttpClient httpClient = initHttpClient(verifier, paySetting);
            //接口URL
            HttpPost httpPost = new HttpPost(url);
            initHttpPost(query, httpPost, verifier);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            String responseStr = response.getEntity() != null ? EntityUtils.toString(response.getEntity()) : "{}";
            JSONObject json = JSON.parseObject(responseStr);
            log.info("pay order response" + responseStr);
            if (SUCCESS_CODE_NO_RESPONSE == response.getStatusLine().getStatusCode()) {
                return;
            } else {
                throw new BusinessException(ResultCode.WECHAT_PAY_RESULT_ERROR, json.get("message").toString());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.warn(err + ": " + ex.getMessage());
            if (ex instanceof BusinessException) {
                throw new BusinessException(ResultCode.WECHAT_PAY_RESULT_ERROR, ex.getMessage());
            } else {
                throw new BusinessException(ResultCode.WECHAT_PAY_INTERFACE_ERROR, err);
            }
        }
    }


    /**
     * 调用微信get方法
     *
     * @param url
     * @param err
     * @param verifier
     * @return
     */
    public String requestHttpGetObj(String url, String err, AutoUpdateCertificatesVerifier verifier, SysPayChannelSetting paySetting) {
        try {
            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("Accept", "application/json");
            CloseableHttpClient client = initHttpClient(verifier, paySetting);
            CloseableHttpResponse response = client.execute(httpGet);
            String responseStr = EntityUtils.toString(response.getEntity());
            log.info("applyment response" + responseStr);
            if (SUCCESS_CODE == response.getStatusLine().getStatusCode()) {
                return responseStr;
            } else if (CODE_404 == response.getStatusLine().getStatusCode()) {
                throw new BusinessException(ResultCode.WECHAT_PAY_NOT_FOUND);
            } else {
                JSONObject json = JSON.parseObject(responseStr);
                String codeKey = "code";

                if ("NO_STATEMENT_EXIST".equals(json.get(codeKey).toString())) {
                    throw new BusinessException(ResultCode.APPLYMENT_IMG_UPLOAD_FAIL,
                            json.get("message").toString());
                } else {
                    throw new BusinessException(ResultCode.APPLYMENT_QUERY_FAIL, json.get("message").toString());
                }

            }

        } catch (Exception ex) {
            ex.printStackTrace();
            log.warn(err + ": " + ex.getMessage());
            if (ex instanceof BusinessException) {
                throw new BusinessException(((BusinessException) ex).getErrorCode(), ex.getMessage());
            } else {
                throw new BusinessException(ResultCode.WECHAT_PAY_INTERFACE_ERROR, err);
            }
        }

    }


    /**
     * 初始化httpPost
     */
    public void initHttpPost(BaseReq params, HttpPost httpPost, AutoUpdateCertificatesVerifier verifier) throws Exception {
        if (verifier == null) {
            throw new BusinessException(ResultCode.SIGN_IS_FAIL, "微信证书更新失败，请检查支付参数是否正确");
        }
        HashMap<BigInteger, X509Certificate> certificateHashMap = verifier.getCert();
        X509Certificate certificate = null;
        for (BigInteger key : certificateHashMap.keySet()) {
            X509Certificate item = certificateHashMap.get(key);
            //获取一个最新的
            if (certificate == null || item.getNotAfter().after(certificate.getNotAfter())) {
                certificate = item;
            }
        }
        String reqdata = params.toString(certificate);
        InputStream stream = new ByteArrayInputStream(reqdata.getBytes("utf-8"));
        InputStreamEntity reqEntity = new InputStreamEntity(stream);
        reqEntity.setContentType("application/json");
        httpPost.setEntity(reqEntity);
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Wechatpay-Serial", certificate.getSerialNumber().toString(16).toUpperCase());
    }

    /**
     * 初始化HttpClient
     */
    public CloseableHttpClient initHttpClient(AutoUpdateCertificatesVerifier verifier, SysPayChannelSetting paySetting) {

        PrivateKey privateKey = PemUtil.loadPrivateKey(new ByteArrayInputStream(paySetting.getPrivateKeyBlob()));
        WeChatConfigVo config = JsonUtil.readValue(paySetting.getSettingConfig(), WeChatConfigVo.class);
        CloseableHttpClient httpClient = WechatPayHttpClientBuilder.create()
                .withMerchant(paySetting.getPayMchId(), config.getSerialNo(), privateKey)
                .withValidator(new WechatPay2Validator(verifier)).build();
        return httpClient;
    }



    /**
     * 对象转查询(驼峰转下划线）
     *
     * @param obj
     * @param <M>
     * @return
     */
    private <M extends BaseReq> String objToQuery(M obj) {
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(obj));
        Set<String> keySet = JSON.parseObject(JSON.toJSONString(obj)).keySet();
        StringBuilder kvStrings = new StringBuilder();
        for (String key : keySet) {
            Object object = jsonObject.get(key);
            if (Objects.isNull(object)) {
                continue;
            }
            kvStrings.append(String.format("%s=%s&", key, object.toString()));
        }
        return kvStrings.toString().substring(0, kvStrings.length() - 1);
    }

}
