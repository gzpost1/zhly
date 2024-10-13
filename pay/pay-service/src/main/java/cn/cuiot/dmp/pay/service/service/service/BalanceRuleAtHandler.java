package cn.cuiot.dmp.pay.service.service.service;

import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.pay.service.service.dto.BalanceChangeDto;
import cn.cuiot.dmp.pay.service.service.dto.BalanceChangeRecordQuery;
import cn.cuiot.dmp.pay.service.service.entity.BalanceChangeRecord;
import cn.cuiot.dmp.pay.service.service.entity.BalanceEntity;
import cn.cuiot.dmp.pay.service.service.vo.BalanceEventAggregate;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author huq
 * @ClassName BalanceRuleAtHandler
 * @Date 2024/1/18 16:44
 **/
@Service
@Slf4j
public class BalanceRuleAtHandler {
    @Autowired
    private BalanceRuleHandler ruleHandler;
    @Autowired
    private BalanceService balanceService;
    @Autowired
    private MbBalanceChangeRecordService changeRecordService;


    /**
     * 查询余额明细
     *
     * @param query
     * @return
     */
    public IPage<BalanceChangeRecord> queryBalanceChangeRecordForPage(BalanceChangeRecordQuery query) {
        return changeRecordService.queryForPage(query);
    }

    /**
     * 后台调用-增加或减少房屋余额
     *
     * @param param
     * @return
     */
    public void updateBalance(BalanceChangeDto param) {
        param.valid();
        ruleHandler.platformHandler(param);
    }

    /**
     * 业务调用-余额变动
     *
     * @param param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public IdmResDTO handler(BalanceEventAggregate param) {
        ruleHandler.handler(param);
        return IdmResDTO.success();
    }

    /**
     * 查询房屋当前余额
     *
     * @param id
     * @return
     */
    public BalanceEntity queryHouseBalance(Long id) {
        return balanceService.getById(id);
    }
    /**
     * 创建余额账户
     *
     * @param userId
     */
    public void createBalance(Long userId) {
         balanceService.createBalance(userId);
    }
}
