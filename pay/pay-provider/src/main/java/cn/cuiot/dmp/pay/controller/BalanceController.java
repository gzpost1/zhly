package cn.cuiot.dmp.pay.controller;

import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.pay.service.service.dto.BalanceChangeDto;
import cn.cuiot.dmp.pay.service.service.dto.BalanceChangeRecordQuery;
import cn.cuiot.dmp.pay.service.service.entity.BalanceChangeRecord;
import cn.cuiot.dmp.pay.service.service.entity.BalanceEntity;
import cn.cuiot.dmp.pay.service.service.service.BalanceRuleAtHandler;
import cn.cuiot.dmp.pay.service.service.vo.BalanceEventAggregate;
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
 * 会员中心-账户余额
 *
 * @author wuyongchong
 * @since 2023-11-16
 */
@Slf4j
@RestController
@RequestMapping("/mb-balance")
public class BalanceController {

    @Autowired
    private BalanceRuleAtHandler ruleAtHandler;


    /**
     * 查询余额明细
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
     * 增加或减少用户余额（系统管理端）
     *
     * @param param
     * @return
     */
    @PostMapping("/updateBalance")
    public IdmResDTO updateBalance(@RequestBody @Valid BalanceChangeDto param) {
        ruleAtHandler.updateBalance(param);
        return IdmResDTO.success();
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
    public IdmResDTO changeBalance(@RequestBody @Valid BalanceEventAggregate param) {
        param.setChangeUser(ARTIFICIAL);
        ruleAtHandler.handler(param);
        return IdmResDTO.success();
    }


}
