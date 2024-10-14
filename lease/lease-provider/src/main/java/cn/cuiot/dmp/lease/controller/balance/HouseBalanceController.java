package cn.cuiot.dmp.lease.controller.balance;

import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.pay.service.service.dto.BalanceChangeRecordQuery;
import cn.cuiot.dmp.pay.service.service.entity.BalanceChangeRecord;
import cn.cuiot.dmp.pay.service.service.entity.BalanceEntity;
import cn.cuiot.dmp.pay.service.service.enums.BalanceChangeTypeEnum;
import cn.cuiot.dmp.pay.service.service.service.BalanceRuleAtHandler;
import cn.cuiot.dmp.pay.service.service.vo.BalanceEventAggregate;
import cn.cuiot.dmp.pay.service.service.vo.RechargeBalanceVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static cn.cuiot.dmp.pay.service.service.enums.Constants.ARTIFICIAL;

/**
 * 系统管理-预缴
 *
 * @author wuyongchong
 * @since 2023-11-16
 */
@Slf4j
@RestController
@RequestMapping("/balance")
public class HouseBalanceController {

    @Autowired
    private BalanceRuleAtHandler ruleAtHandler;


    /**
     * 充值记录列表
     *
     * @param query
     * @return
     */
    @PostMapping("/queryBalanceChangeRecordForPage")
    public IdmResDTO<IPage<BalanceChangeRecord>> queryBalanceChangeRecordForPage(@RequestBody @Valid BalanceChangeRecordQuery query) {
        IPage<BalanceChangeRecord> pageResult = ruleAtHandler.queryBalanceChangeRecordForPage(query);
        return IdmResDTO.success(pageResult);
    }



    /**
     * 查询房屋当前余额
     *
     * @param param
     * @return
     */
    @PostMapping("/queryHouseBalance")
    public IdmResDTO<BalanceEntity> queryHouseBalance(@RequestBody @Valid IdParam param) {
        return IdmResDTO.success(ruleAtHandler.queryHouseBalance(param.getId()));
    }


    /**
     * 余额变动(包括充值，扣减等)-业务端调用
     */
    @PostMapping("/changeBalance")
    public IdmResDTO changeBalance(@RequestBody @Valid RechargeBalanceVo vo) {
        BalanceEventAggregate balanceEventAggregate = new BalanceEventAggregate();
        balanceEventAggregate.setChangeUser(ARTIFICIAL);
        balanceEventAggregate.setBalance(vo.getBalance());
        balanceEventAggregate.setHouseId(vo.getHouseId());
        balanceEventAggregate.setReason(vo.getReason());
        balanceEventAggregate.setOrderName("后台充值");
        balanceEventAggregate.setChangeType(BalanceChangeTypeEnum.BALANCE_RECHARGE.getType());
        ruleAtHandler.handler(balanceEventAggregate);
        return IdmResDTO.success();
    }


}
