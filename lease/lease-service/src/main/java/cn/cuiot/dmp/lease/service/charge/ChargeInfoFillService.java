package cn.cuiot.dmp.lease.service.charge;

import cn.cuiot.dmp.base.infrastructure.dto.req.CommonOptionSettingReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.CommonOptionSettingRspDTO;
import cn.cuiot.dmp.lease.dto.charge.*;
import cn.cuiot.dmp.lease.feign.SystemToFlowService;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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


    public <T extends ChargeFill> void fillinfo(List<T> records, Class<T> clazz) {
        List<Long> carteIds = new ArrayList<>();

        boolean isChargeItemFlag = ChargeItemNameSet.class.isAssignableFrom(clazz);
        boolean transactionModeFlag = TransactionModeNameSet.class.isAssignableFrom(clazz);

        for (T record : records) {
            if(isChargeItemFlag){
                carteIds.add(((ChargeItemNameSet)record).getChargeItemId());
            }
            if(transactionModeFlag){
                carteIds.add(((TransactionModeNameSet)record).getTransactionMode());
            }
        }

        if(CollectionUtils.isNotEmpty(carteIds)){
            //收费项目名称
            CommonOptionSettingReqDTO commonOptionSettingReqDTO = new CommonOptionSettingReqDTO();
            commonOptionSettingReqDTO.setIdList(carteIds);
            List<CommonOptionSettingRspDTO> commonOptionSettingRspDTOS = systemToFlowService.batchQueryCommonOptionSetting(commonOptionSettingReqDTO);

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
                        if (changeItemMap.containsKey(chargeItemId)) {
                            ((TransactionModeNameSet) record).setTransactionModeName(changeItemMap.get(chargeItemId).getName());
                        }
                    }
                }
            }
        }
    }
}