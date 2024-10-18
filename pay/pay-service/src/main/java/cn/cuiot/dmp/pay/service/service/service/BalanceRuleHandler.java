package cn.cuiot.dmp.pay.service.service.service;

import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.pay.service.service.dto.BalanceChangeDto;
import cn.cuiot.dmp.pay.service.service.dto.BalanceChangeRecordQuery;
import cn.cuiot.dmp.pay.service.service.entity.BalanceChangeRecord;
import cn.cuiot.dmp.pay.service.service.entity.BalanceEntity;
import cn.cuiot.dmp.pay.service.service.enums.BalanceChangeTypeEnum;
import cn.cuiot.dmp.pay.service.service.vo.BalanceEventAggregate;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static cn.cuiot.dmp.pay.service.service.enums.BalanceChangeTypeEnum.*;
import static cn.cuiot.dmp.pay.service.service.enums.Constants.ARTIFICIAL;
import static cn.cuiot.dmp.pay.service.service.enums.Constants.PLATFORM;


/**
 * 余额支付-操作类
 */
@Slf4j
@Service
public class BalanceRuleHandler {
    @Autowired
    private BalanceService balanceService;

    @Autowired
    private MbBalanceChangeRecordService changeRecordService;
    @Autowired
    private PlatformTransactionManager transactionManager;

    /**
     * 余额变动业务入口
     *
     * @param param
     * @return
     */
    public Long handler(BalanceEventAggregate param) {
        BalanceChangeTypeEnum typeEnum = BalanceChangeTypeEnum.getTypeEnum(param.getChangeType());
        BalanceChangeRecord record = BalanceChangeRecord.builder()
                .id(IdWorker.getId())
                .changeType(param.getChangeType())
                .createTime(param.getCreateTime() == null ? new Date() : param.getCreateTime())
                .houseId(param.getHouseId())
                .orderId(Objects.nonNull(param.getOrderId()) ? Long.parseLong(param.getOrderId()) : null)
                .orderName(param.getOrderName())
                .reason(param.getReason())
                .dataType(param.getDataType())
                .changeUser(Objects.isNull(param.getChangeUser())?PLATFORM:param.getChangeUser())
                .payOrderId(param.getPayOrderId())
                .build();
        //异步调用时 无法获得操作人id  这里手动传入
        record.setCreateUser(Objects.isNull(LoginInfoHolder.getCurrentUserId())?param.getCreateUser():LoginInfoHolder.getCurrentUserId());
        switch (typeEnum) {
            case BALANCE_PAY:
                pay(param, record);
                break;
            case BALANCE_PAY_REFUND:
                if (BALANCE_RECHARGE.getType().equals(param.getBeforeChangeType())) {
                    refundByRecharge(param, record);
                } else {
                    refund(param, record);
                }
                break;
            case BALANCE_RECHARGE:
                charge(param, record);
                break;
            case BALANCE_EXCHANGE:
                exchange(param, record);
                break;
            default:
                throw new BusinessException(ResultCode.SERVER_BUSY, "没有这个余额变更类型");
        }
        return record.getId();
    }

    /**
     * 订单消费-不做事务，统一由订单业务或后续的支付业务做
     *
     * @param param
     * @return
     */
    private void pay(BalanceEventAggregate param, BalanceChangeRecord record) {
        BalanceEntity old =
                Optional.ofNullable(balanceService.getById(param.getHouseId())).orElseThrow(() ->  new BusinessException(ResultCode.OBJECT_NOT_EXIST, "会员账户不存在，不能使用余额支付"));
        if (old.getBalance() < param.getBalance()) {
            throw new BusinessException(ResultCode.SERVER_BUSY, "账户余额不足");
        }
        record.setBalance(0 - param.getBalance());
        toSqlNoTransaction(old, record);
    }

    /**
     * 退款-不做事务，统一由订单业务或后续的支付业务做
     * 消费记录后退款
     *
     * @param param
     * @return
     */
    private void refund(BalanceEventAggregate param, BalanceChangeRecord record) {
        BalanceEntity old =
                Optional.ofNullable(balanceService.getById(param.getHouseId())).orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST,"会员账户不存在"));
        BalanceChangeRecord changeRecord = changeRecordService.selectOneByOrderId(Long.parseLong(param.getOrderId()),
                BALANCE_PAY.getType());
        if (changeRecord == null) {
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "不存在消费记录，无法退款");
        }
        if (param.getBalance() > Math.abs(changeRecord.getBalance())) {
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "退款金额大于消费金额，不可退款");
        }
        List<BalanceChangeRecord> list =
                changeRecordService.queryForList(BalanceChangeRecordQuery.builder().orderId(Long.parseLong(param.getOrderId())).changeType(BALANCE_PAY_REFUND.getType()).build());
        //已退款金额
        Integer alreadyRefundAmount = CollectionUtils.isEmpty(list) ? 0 :
                list.stream().mapToInt(p -> p.getBalance()).sum();
        //消费金额，原来入详细记录是负数，0-就是正数
        Integer paidAmount = 0 - changeRecord.getBalance();
        //如果本次退款金额大于可退款金额，抛异常（购物车场景可能存在多笔退款）
        if (param.getBalance() > paidAmount - alreadyRefundAmount) {
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "退款金额大于可退款金额，不可退款");
        }
        list.forEach(item -> {
            if (item.getOutRefundNo().equals(param.getOutRefundNo())) {
                throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "该笔退款单已退款，请勿重复发起");
            }
        });
        record.setBalance(param.getBalance());
        record.setOutRefundNo(param.getOutRefundNo());
        toSqlNoTransaction(old, record);
    }

    /**
     * 退款-不做事务，统一由订单业务或后续的支付业务做
     * 充值后退款
     *
     * @param param
     * @return
     */
    private void refundByRecharge(BalanceEventAggregate param, BalanceChangeRecord record) {
        BalanceEntity old =
                Optional.ofNullable(balanceService.getById(param.getHouseId())).orElseThrow(() ->  new BusinessException(ResultCode.OBJECT_NOT_EXIST, "会员账户不存在"));
        BalanceChangeRecord changeRecord = changeRecordService.selectOneByOrderId(Long.parseLong(param.getOrderId()),
                BALANCE_RECHARGE.getType());
        if (changeRecord == null) {
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "不存在充值记录，无法退款");
        }
        if (param.getBalance() > Math.abs(changeRecord.getBalance())) {
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "退款金额大于充值金额，不可退款");
        }
        List<BalanceChangeRecord> list =
                changeRecordService.queryForList(BalanceChangeRecordQuery.builder().orderId(Long.parseLong(param.getOrderId())).changeType(BALANCE_PAY_REFUND.getType()).build());
        //已退款金额
        Integer alreadyRefundAmount = CollectionUtils.isEmpty(list) ? 0 :
                list.stream().mapToInt(p -> p.getBalance()).sum();
        //充值金额
        Integer paidAmount = changeRecord.getBalance();
        //如果本次退款金额大于可退款金额，抛异常（购物车场景可能存在多笔退款）
        if (param.getBalance() > paidAmount - alreadyRefundAmount) {
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "退款金额大于可退款金额，不可退款");
        }
        list.forEach(item -> {
            if (item.getOutRefundNo().equals(param.getOutRefundNo())) {
                throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "该笔退款单已退款，请勿重复发起");
            }
        });
        record.setBalance(0 - param.getBalance());
        record.setOutRefundNo(param.getOutRefundNo());
        toSqlNoTransaction(old, record);
    }

    /**
     * 用户充值-不做事务，只变更余额及增加记录，事务由充值订单业务处理
     *
     * @param param
     * @return
     */
    private void charge(BalanceEventAggregate param, BalanceChangeRecord record) {
        if (param.getBalance() == null) {
            throw new BusinessException(ResultCode.OBJECT_NOT_EXIST, "充值金额不能为空");
        }
        //  2023/11/29 等待充值订单业务完成后处理
        BalanceEntity old =
                Optional.ofNullable(balanceService.getById(param.getHouseId()))
                        .orElseGet(() -> balanceService.createBalance(param.getHouseId()));
        record.setBalance(param.getBalance());
        toSqlNoTransaction(old, record);
    }

    /**
     * 兑换-不做事务，只变更余额及增加记录，事务由兑换业务处理
     *
     * @param param
     * @return
     */
    private void exchange(BalanceEventAggregate param, BalanceChangeRecord record) {
        Long userId = param.getHouseId();
        BalanceEntity old =
                Optional.ofNullable(balanceService.getById(userId)).orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST, "房屋账户不存在"));
        record.setBalance(param.getBalance());
        toSqlNoTransaction(old, record);
    }

    /**
     * 平台操作-需要做事务
     *
     * @param param
     * @return
     */
    public void platformHandler(BalanceChangeDto param) {
        BalanceEntity old =
                Optional.ofNullable(balanceService.getById(param.getHouseId())).orElseGet(() -> balanceService.createBalance(param.getHouseId()));
        int updateBalance = param.getBalance();
        if (updateBalance < 0) {
            //如果扣减金额数量大于当前余额
            if (0- updateBalance > old.getBalance()) {
                new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "充值金额不可小于"+centToYuan(old.getBalance())+"元");
            }
        }
        BalanceChangeRecord record = BalanceChangeRecord.builder()
                .id(IdWorker.getId())
                .changeUser(ARTIFICIAL)
                .changeType(BalanceChangeTypeEnum.BALANCE_CONSUMPTION.getType())
                .createTime(new Date())
                .houseId(param.getHouseId())
                .balance(updateBalance)
                .beforeBalance(old.getBalance())
                .reason(param.getReason())
                .orderName("后台充值")
                .build();
        toSql(old, record);
    }

    /**
     * 分转元
     */
    public static BigDecimal centToYuan(Integer price) {
        if (Objects.isNull(price)) {
            return BigDecimal.ZERO;
        } else {
            return BigDecimal.valueOf(price).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
        }
    }

    /**
     * 操作进数据库-不带事务
     *
     * @param old
     * @param record
     */
    private void toSqlNoTransaction(BalanceEntity old, BalanceChangeRecord record) {
        record.setBeforeBalance(old.getBalance());
        BalanceEntity accountEntity = BalanceEntity.builder()
                .balance(record.getBalance())
                .houseId(record.getHouseId())
                .version(old.getVersion())
                .build();
        balanceService.changeBalance(accountEntity);
        changeRecordService.save(record);
    }

    /**
     * 进数据库-带事务
     *
     * @param old
     * @param record
     */
    private void toSql(BalanceEntity old, BalanceChangeRecord record) {
        TransactionStatus transaction = null;
        try {
            DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
            definition.setTimeout(30);
            transaction = transactionManager.getTransaction(definition);
            toSqlNoTransaction(old, record);
            transactionManager.commit(transaction);
        } catch (Exception ex) {
            if (Objects.nonNull(transaction)) {
                transactionManager.rollback(transaction);
            }
            log.error("数据库入库异常", ex);
            new BusinessException(ResultCode.SERVER_BUSY, ex.getMessage());
        }
    }
}
