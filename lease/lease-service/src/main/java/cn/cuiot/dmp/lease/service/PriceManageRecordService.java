package cn.cuiot.dmp.lease.service;

import cn.cuiot.dmp.lease.entity.PriceManageRecordEntity;
import cn.cuiot.dmp.lease.mapper.PriceManageRecordMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author caorui
 * @date 2024/6/24
 */
@Slf4j
@Service
public class PriceManageRecordService extends ServiceImpl<PriceManageRecordMapper, PriceManageRecordEntity> {

    /**
     * 保存定价管理操作记录
     */
    public void savePriceManageRecord(String operateName, Long operatorId){
        PriceManageRecordEntity priceManageRecordEntity = new PriceManageRecordEntity();
        priceManageRecordEntity.setOperateName(operateName);
        priceManageRecordEntity.setOperatorId(operatorId);
        priceManageRecordEntity.setOperateTime(new Date());
        save(priceManageRecordEntity);
    }

}
