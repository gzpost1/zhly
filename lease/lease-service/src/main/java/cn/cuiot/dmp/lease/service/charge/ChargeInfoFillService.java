package cn.cuiot.dmp.lease.service.charge;

import cn.cuiot.dmp.base.infrastructure.dto.req.CommonOptionSettingReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.CommonOptionSettingRspDTO;
import cn.cuiot.dmp.lease.dto.charge.*;
import cn.cuiot.dmp.lease.entity.charge.TbChargeStandard;
import cn.cuiot.dmp.lease.feign.SystemToFlowService;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Description 填充记录的相关信息
 * @Date 2024/7/2 15:15
 * @Created by libo
 */
@Service
public class ChargeInfoFillService {
    @Autowired
    private SystemToFlowService systemToFlowService;
    @Autowired
    private TbChargeStandardService tbChargeStandardService;


    public <T extends ChargeFill> void fillinfo(List<T> records, Class<T> clazz) {
        List<Long> carteIds = new ArrayList<>();
        List<Long> chargeStandardIds = new ArrayList<>();

        boolean isChargeItemFlag = ChargeItemNameSet.class.isAssignableFrom(clazz);
        boolean transactionModeFlag = TransactionModeNameSet.class.isAssignableFrom(clazz);
        boolean refundModeNameFlag = RefundModeNameSet.class.isAssignableFrom(clazz);
        boolean chargeStandardFlag =  ChargeStandardNameSet.class.isAssignableFrom(clazz);

        for (T record : records) {
            if(isChargeItemFlag){
                carteIds.add(((ChargeItemNameSet)record).getChargeItemId());
            }
            if(transactionModeFlag){
                carteIds.add(((TransactionModeNameSet)record).getTransactionMode());
            }
            if(refundModeNameFlag){
                carteIds.add(((RefundModeNameSet)record).getRefundMode());
            }
            if(chargeStandardFlag){
                chargeStandardIds.add(((ChargeStandardNameSet)record).getChargeStandard());
            }
        }

        if(CollectionUtils.isNotEmpty(carteIds)){
            //收费项目名称
            CommonOptionSettingReqDTO commonOptionSettingReqDTO = new CommonOptionSettingReqDTO();
            commonOptionSettingReqDTO.setIdList(carteIds);
            List<CommonOptionSettingRspDTO> commonOptionSettingRspDTOS = Optional.ofNullable(systemToFlowService.batchQueryCommonOptionSetting(commonOptionSettingReqDTO)).orElse(new ArrayList<>());
            commonOptionSettingRspDTOS.add(new CommonOptionSettingRspDTO(0L,"微信支付",(byte)1));
            commonOptionSettingRspDTOS.add(new CommonOptionSettingRspDTO(1L,"预缴代扣",(byte)2));

            if(CollectionUtils.isNotEmpty(commonOptionSettingRspDTOS)){
                Map<Long, CommonOptionSettingRspDTO> changeItemMap = commonOptionSettingRspDTOS.stream().collect(Collectors.toMap(CommonOptionSettingRspDTO::getId, Function.identity()));

                for (T record : records) {
                    if(isChargeItemFlag){
                        Long chargeItemId = ((ChargeItemNameSet) record).getChargeItemId();
                        if (changeItemMap.containsKey(chargeItemId)) {
                            ((ChargeItemNameSet) record).setChargeItemName(changeItemMap.get(chargeItemId).getName());
                        }
                    }

                    if(transactionModeFlag){
                        Long chargeItemId = ((TransactionModeNameSet) record).getTransactionMode();
                        changeItemMap.put(0L,new CommonOptionSettingRspDTO(0L,"微信支付",(byte)1));
                        changeItemMap.put(1L,new CommonOptionSettingRspDTO(1L,"预缴代扣",(byte)2));
                        if (changeItemMap.containsKey(chargeItemId)) {
                            ((TransactionModeNameSet) record).setTransactionModeName(changeItemMap.get(chargeItemId).getName());
                        }
                    }

                    if(refundModeNameFlag){
                        Long refundMode = ((RefundModeNameSet) record).getRefundMode();
                        if (changeItemMap.containsKey(refundMode)) {
                            ((RefundModeNameSet) record).setRefundModeName(changeItemMap.get(refundMode).getName());
                        }
                    }
                }
            }
        }

        //填充收费标准
        if(CollectionUtils.isNotEmpty(chargeStandardIds)){
            List<TbChargeStandard> tbChargeStandards = Optional.ofNullable(tbChargeStandardService.listByIds(chargeStandardIds)).orElse(new ArrayList<>());
            Map<Long, TbChargeStandard> chargeStandardMap = tbChargeStandards.stream().collect(Collectors.toMap(TbChargeStandard::getId, Function.identity()));

            if(chargeStandardMap != null && !chargeStandardMap.isEmpty()){
                for (T record : records) {
                    Long chargeStandard = ((ChargeStandardNameSet) record).getChargeStandard();
                    if (chargeStandardMap.containsKey(chargeStandard)) {
                        ((ChargeStandardNameSet) record).setChargeStandardName(chargeStandardMap.get(chargeStandard).getChargeStandard());
                    }
                }
            }
        }
    }
}
