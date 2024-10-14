package cn.cuiot.dmp.lease.controller.app;

import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.utils.BeanMapper;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.lease.dto.balance.MbRechargeOrderCreateDto;
import cn.cuiot.dmp.lease.entity.balance.MbRechargeOrder;
import cn.cuiot.dmp.lease.service.balance.MbRechargeOrderService;
import cn.cuiot.dmp.lease.service.balance.RechargeOrderPayRule;
import cn.cuiot.dmp.lease.vo.balance.MbRechargeOrderCreateVo;
import cn.cuiot.dmp.pay.service.service.dto.BalanceChangeRecordQuery;
import cn.cuiot.dmp.pay.service.service.entity.BalanceChangeRecord;
import cn.cuiot.dmp.pay.service.service.entity.BalanceEntity;
import cn.cuiot.dmp.pay.service.service.service.BalanceRuleAtHandler;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;


/**
 * 移动端-预缴
 *
 * @author wuyongchong
 * @since 2023-11-30
 */
@RestController
@RequestMapping("/app/balance")
public class AppHouseBalanceOrderController {

    @Autowired
    private RechargeOrderPayRule orderPayRule;

    @Autowired
    private BalanceRuleAtHandler ruleAtHandler;
    @Autowired
    private MbRechargeOrderService mbRechargeOrderService;
    /**
     * 充值订单下单
     *
     * @return
     */
    @PostMapping("/placeOrder")
    public IdmResDTO<MbRechargeOrderCreateVo> placeOrder(@RequestBody @Valid MbRechargeOrderCreateDto param) {
        MbRechargeOrder order = BeanMapper.map(param, MbRechargeOrder.class);
        MbRechargeOrderCreateVo createVo = orderPayRule.createOrder(param, order);
        return IdmResDTO.success(createVo);
    }


    /**
     * 查询余额明细列表
     *
     * @param query
     * @return
     */
    @PostMapping("/queryBalanceChangeRecordForPage")
    public IdmResDTO<IPage<BalanceChangeRecord>> queryBalanceChangeRecordForPage(@RequestBody @Valid BalanceChangeRecordQuery query) {
        List<Long> houseIds = mbRechargeOrderService.queryHouseIdsByUser(LoginInfoHolder.getCurrentUserId());
        AssertUtil.isFalse(!houseIds.contains(query.getHouseId()),"您没有权限查看该用户信息");
        IPage<BalanceChangeRecord> pageResult = ruleAtHandler.queryBalanceChangeRecordForPage(query);
        return IdmResDTO.success(pageResult);
    }

    /**
     * 查询余额明细列表
     *
     * @return
     */
    @PostMapping("/queryBalanceChangeRecordForDetail")
    public IdmResDTO<BalanceChangeRecord> queryBalanceChangeRecordForDetail(@RequestBody @Valid IdParam param) {
        BalanceChangeRecord balanceChangeRecord = ruleAtHandler.queryBalanceChangeRecordForDetail(param);
        AssertUtil.isFalse(Objects.isNull(balanceChangeRecord),"该记录不存在");
        List<Long> houseIds = mbRechargeOrderService.queryHouseIdsByUser(LoginInfoHolder.getCurrentUserId());
        AssertUtil.isFalse(!houseIds.contains(balanceChangeRecord.getHouseId()),"您没有权限查看该用户信息");
        return IdmResDTO.success(balanceChangeRecord);
    }

    /**
     * 查询房屋当前余额
     *
     * @param param
     * @return
     */
    @PostMapping("/queryHouseBalance")
    public IdmResDTO<BalanceEntity> queryHouseBalance(@RequestBody @Valid IdParam param) {
        List<Long> houseIds = mbRechargeOrderService.queryHouseIdsByUser(LoginInfoHolder.getCurrentUserId());
        AssertUtil.isFalse(!houseIds.contains(param.getId()),"您没有权限查看该用户信息");
        return IdmResDTO.success(ruleAtHandler.queryHouseBalance(param.getId()));
    }

}
