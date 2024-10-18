package cn.cuiot.dmp.pay.service.service.service;

import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
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
import com.chinaunicom.yunjingtech.httpclient.bean.PayerInfoEntity;
import com.chinaunicom.yunjingtech.httpclient.bean.pay.NormalAmountEntity;
import com.chinaunicom.yunjingtech.httpclient.bean.pay.SceneInfoEntity;
import com.chinaunicom.yunjingtech.httpclient.bean.pay.SettleInfoEntity;
import com.chinaunicom.yunjingtech.httpclient.bean.pay.request.normal.NormalOrderReq;
import com.chinaunicom.yunjingtech.httpclient.bean.pay.response.normal.NormalQueryOrderResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private NormalPayHttpService normalPayHttpService;

    @Autowired
    private SysPayChannelSettingService settingService;

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
        SysPayChannelSetting paySetting = settingService.getPaySettingByOrgId(unifiedOrderRequestDto.getOrgId());
        Date expire = DateTimeUtil.localDateTimeToDate(LocalDateTime.now().plusMinutes(30));
        NormalOrderReq request = NormalOrderReq.builder()
                .appId(StringUtils.isEmpty(unifiedOrderRequestDto.getAppId()) ? paySetting.getAppId()
                        : unifiedOrderRequestDto.getAppId())
                .mchId(StringUtils.isEmpty(unifiedOrderRequestDto.getPayMchId())? paySetting.getPayMchId():unifiedOrderRequestDto.getPayMchId())
                .outTradeNo(unifiedOrderRequestDto.getOutOrderId())
                .description(unifiedOrderRequestDto.getProductName().length() > 20 ? unifiedOrderRequestDto.getProductName().substring(0,
                        17) + "..." :unifiedOrderRequestDto.getProductName())
                .timeExpire(DateTimeUtil.dateToString(expire, "yyyy-MM-dd'T'HH:mm:ssZZZZZ"))
                .attach(unifiedOrderRequestDto.getAttach())
                .goodsTag(unifiedOrderRequestDto.getGoodsTag())
                .amount(NormalAmountEntity.builder().totalAmount(unifiedOrderRequestDto.getTotalFee()).currency("CNY").build())
                .payer(PayerInfoEntity.builder().openId(unifiedOrderRequestDto.getOpenId()).build())
                .sceneInfo(SceneInfoEntity.builder().payerClientIp(unifiedOrderRequestDto.getSpbillCreateIp()).build())
                .settleInfo(SettleInfoEntity.builder().profitSharing(Boolean.FALSE).build())
                .attach(unifiedOrderRequestDto.getBusinessType().toString())
                .build();

        UnifiedOrderResponseDto responseDto = normalPayHttpService.payOrder(request,paySetting, unifiedOrderRequestDto.getTradeType());
        return buildPrePayReturn(unifiedOrderRequestDto.getOutOrderId(), responseDto);
    }

    /**
     * 组装预下单返回结果
     *
     * @param orderId     商户平台订单号
     * @param responseDto 微信预下单返回结果
     */
    private CreateOrderAggregate buildPrePayReturn(String orderId, UnifiedOrderResponseDto responseDto) {
        CreateOrderAggregate aggregate = new CreateOrderAggregate();
        aggregate.setOutOrderId(orderId);
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
        SysPayChannelSetting paySetting = settingService.getPaySettingByOrgId(request.getOrgId());
        WePayOrderQuery orderQuery = new WePayOrderQuery();
        orderQuery.setOutTradeNo(request.getOutOrderId());
        orderQuery.setPayMchId(request.getPayMchId());
        NormalQueryOrderResp orderResp = normalPayHttpService.queryOrder(orderQuery,paySetting);

        // 组装返回结果
        return PayOrderQueryAggregate.builder()
                .outOrderId(orderResp.getOutTradeNo())
                .payMchId(orderResp.getMchId())
                .payMethod(AppPayMethodEnum.WECHAT.getPayMethod())
                .payOrderId(orderResp.getTransactionId())
                .status(WxOrderConstant.WePayOrderStatus.getYjStatus(orderResp.getTradeState()))
                .payCompleteTime(DateTimeUtil.stringToDate(orderResp.getSuccessTime(), "yyyy-MM-dd'T'HH:mm:ss"))
                .totalFee(orderResp.getAmount() != null ? orderResp.getAmount().getTotalAmount() : null)
                .payMchId(orderResp.getMchId())
                .thirdStatus(orderResp.getTradeState())
                .promotionDetail(orderResp.getPromotionDetail())
                .attach(orderResp.getAttach())
                .build();
    }

    /**
     * 普通商户单笔关单
     */
    @Override
    public IdmResDTO closeOrder(CloseOrderParam request) {
        SysPayChannelSetting paySetting = settingService.getPaySettingByOrgId(request.getOrgId());
        normalPayHttpService.closeOrder(request.getOutOrderId(),paySetting);
        return IdmResDTO.success(true);
    }
}
