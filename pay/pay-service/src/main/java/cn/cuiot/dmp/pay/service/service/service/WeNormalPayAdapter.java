package cn.cuiot.dmp.pay.service.service.service;

import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.pay.service.service.config.CommunityPayProperties;
import cn.cuiot.dmp.pay.service.service.config.NormalWeChatConfig;
import cn.cuiot.dmp.pay.service.service.dto.CloseOrderParam;
import cn.cuiot.dmp.pay.service.service.dto.CreateOrderParam;
import cn.cuiot.dmp.pay.service.service.dto.UnifiedOrderResponseDto;
import cn.cuiot.dmp.pay.service.service.dto.WePayOrderQuery;
import cn.cuiot.dmp.pay.service.service.entity.SysPayChannelSetting;
import cn.cuiot.dmp.pay.service.service.enums.AppPayMethodEnum;
import cn.cuiot.dmp.pay.service.service.enums.PayChannelEnum;
import cn.cuiot.dmp.pay.service.service.enums.WxOrderConstant;
import cn.cuiot.dmp.pay.service.service.normal.NormalPayHttpService;
import cn.cuiot.dmp.pay.service.service.vo.CreateOrderAggregate;
import cn.cuiot.dmp.pay.service.service.vo.PayOrderQueryAggregate;
import cn.cuiot.dmp.pay.service.service.vo.PayOrderQueryParam;
import com.chinaunicom.yunjingtech.httpclient.auth.AutoUpdateCertificatesVerifier;
import com.chinaunicom.yunjingtech.httpclient.auth.PrivateKeySigner;
import com.chinaunicom.yunjingtech.httpclient.auth.WechatPay2Credentials;
import com.chinaunicom.yunjingtech.httpclient.bean.PayerInfoEntity;
import com.chinaunicom.yunjingtech.httpclient.bean.pay.NormalAmountEntity;
import com.chinaunicom.yunjingtech.httpclient.bean.pay.SceneInfoEntity;
import com.chinaunicom.yunjingtech.httpclient.bean.pay.SettleInfoEntity;
import com.chinaunicom.yunjingtech.httpclient.bean.pay.request.normal.NormalOrderReq;
import com.chinaunicom.yunjingtech.httpclient.bean.pay.response.normal.NormalQueryOrderResp;
import com.chinaunicom.yunjingtech.httpclient.util.PemUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 普通商户支付模式
 *
 * @author huqian
 * @date 2024-03-12
 */
@Slf4j
@Service
public class WeNormalPayAdapter implements IPayBaseInterface {


    @Autowired
    private NormalWeChatConfig weChatConfig;

    @Autowired
    @Qualifier("NormalCertificatesVerifier")
    private AutoUpdateCertificatesVerifier verifier;

    @Autowired
    private CommunityPayProperties communityPayProperties;

    @Autowired
    private NormalPayHttpService normalPayHttpService;

    @Value("${app.appId:}")
    private String appId;

    @Override
    public void initSetting(SysPayChannelSetting payChannelSetting) {
        weChatConfig.setPayNotify(payChannelSetting.getPayNotify().replace("${domain.url}", communityPayProperties.getUrl()));
        /*weChatConfig.setRefundNotify(payChannelSetting.getRefundNotify().replace("${domain.url}",
                 communityPayProperties.getUrl()));*/
        weChatConfig.setAppId(appId);
        //配置了全局为普通商户模式
        if(Objects.isNull(payChannelSetting.getPayMchId())){
            log.warn("微信普通商户模式注册失败，原因：未配置商户支付号");
            return;
        }
        NormalWeChatConfig config = JsonUtil.readValue(payChannelSetting.getSettingConfig(), NormalWeChatConfig.class);
        weChatConfig.setApiV3key(config.getApiV3key());
        weChatConfig.setPrivateKey(payChannelSetting.getPrivateKeyBlob());
        weChatConfig.setMchSerialNo(config.getMchSerialNo());
        weChatConfig.setPayMchId(payChannelSetting.getPayMchId());
        weChatConfig.setPayMchName(payChannelSetting.getPayMchName());

        PrivateKey privateKey = PemUtil.loadPrivateKey(new ByteArrayInputStream(weChatConfig.getPrivateKey()));
        verifier.setVerifier(new WechatPay2Credentials(weChatConfig.getPayMchId(),
                        new PrivateKeySigner(weChatConfig.getMchSerialNo(), privateKey)),
                weChatConfig.getApiV3key().getBytes());
    }



    @Override
    public String mark() {
        return PayChannelEnum.WECHAT_NORMAL.getMark().getPayMark();
    }

    /**
     * 普通商户单笔预下单
     *
     * @param unifiedOrderRequestDto 请求参数
     * @return
     */
    @Override
    public CreateOrderAggregate prePay(CreateOrderParam unifiedOrderRequestDto) {
        CreateOrderParam.SubOrderItem subOrderItem = unifiedOrderRequestDto.getSubOrderItems().get(0);
        Date expire = DateTimeUtil.localDateTimeToDate(LocalDateTime.now().plusMinutes(30));
        NormalOrderReq request = NormalOrderReq.builder()
                .appId(StringUtils.isEmpty(unifiedOrderRequestDto.getAppId()) ? weChatConfig.getAppId()
                        : unifiedOrderRequestDto.getAppId())
                .mchId(StringUtils.isEmpty(subOrderItem.getPayMchId())? weChatConfig.getPayMchId():subOrderItem.getPayMchId())
                .outTradeNo(unifiedOrderRequestDto.getOrderId())
                .description(subOrderItem.getProductName().length() > 20 ? subOrderItem.getProductName().substring(0,
                        17) + "..." :
                        subOrderItem.getProductName())
                .timeExpire(DateTimeUtil.dateToString(expire, "yyyy-MM-dd'T'HH:mm:ssZZZZZ"))
                .attach(subOrderItem.getAttach())
                .goodsTag(subOrderItem.getGoodsTag())
                .amount(NormalAmountEntity.builder().totalAmount(subOrderItem.getTotalFee()).currency("CNY").build())
                .payer(PayerInfoEntity.builder().openId(unifiedOrderRequestDto.getOpenId()).build())
                .sceneInfo(SceneInfoEntity.builder().payerClientIp(unifiedOrderRequestDto.getSpbillCreateIp()).build())
                .settleInfo(SettleInfoEntity.builder().profitSharing(subOrderItem.getSettleInfo().getProfitSharing()).build())
                .build();

        UnifiedOrderResponseDto responseDto = normalPayHttpService.payOrder(request, unifiedOrderRequestDto.getTradeType());
        return buildPrePayReturn(unifiedOrderRequestDto.getOrderId(), responseDto);
    }

    /**
     * 组装预下单返回结果
     *
     * @param orderId     商户平台订单号
     * @param responseDto 微信预下单返回结果
     */
    private CreateOrderAggregate buildPrePayReturn(String orderId, UnifiedOrderResponseDto responseDto) {
        CreateOrderAggregate aggregate = new CreateOrderAggregate();
        aggregate.setOrderId(orderId);
        aggregate.setPrepayId(orderId.toString());
        aggregate.setUnifiedOrderSignRsp(CreateOrderAggregate.UnifiedOrderSignRsp.builder()
                .appId(responseDto.getAppId())
                .timeStamp(responseDto.getTimeStamp())
                .nonceStr(responseDto.getNonceStr())
                .pkg(responseDto.getPkg())
                .signType(responseDto.getSignType())
                .paySign(responseDto.getPaySign())
                .build());
        return aggregate;
    }

    /**
     * 单笔查询
     */
    @Override
    public PayOrderQueryAggregate orderQuery(PayOrderQueryParam request) {
        WePayOrderQuery orderQuery = new WePayOrderQuery();
        orderQuery.setOutTradeNo(String.valueOf(request.getOrderId()));
        orderQuery.setPayMchId(request.getPayMchId());
        NormalQueryOrderResp orderResp = normalPayHttpService.queryOrder(orderQuery);

        // 组装返回结果
        return PayOrderQueryAggregate.builder()
                .orderId(orderResp.getOutTradeNo())
                .payMchId(orderResp.getMchId())
                .payMethod(AppPayMethodEnum.WECHAT.getPayMethod())
                .payOrderId(orderResp.getTransactionId())
                .status(WxOrderConstant.WePayOrderStatus.getYjStatus(orderResp.getTradeState()))
                .payCompleteTime(DateTimeUtil.stringToDate(orderResp.getSuccessTime(), "yyyy-MM-dd'T'HH:mm:ss"))
                .subOrderItems(Collections.singletonList(PayOrderQueryAggregate.SubOrderItem.builder()
                        // 平台子订单号
                        .subOrderId(orderResp.getOutTradeNo())
                        // 微信支付子单号
                        .paySubOrderId(orderResp.getTransactionId())
                        .totalFee(orderResp.getAmount() != null ? orderResp.getAmount().getTotalAmount() : null)
                        .payerAmount(orderResp.getAmount() != null ? orderResp.getAmount().getPayerAmount() : null)
                        .payMchId(orderResp.getMchId())
                        .thirdStatus(orderResp.getTradeState())
                        .status(WxOrderConstant.WePayOrderStatus.getYjStatus(orderResp.getTradeState()))
                        .promotionDetail(orderResp.getPromotionDetail())
                        .build()))
                .build();
    }

    /**
     * 普通商户单笔关单
     */
    @Override
    public IdmResDTO closeOrder(CloseOrderParam request) {

        normalPayHttpService.closeOrder(request.getOrderId());
        return IdmResDTO.success(true);
    }
}
