package cn.cuiot.dmp.lease.mapper.charge;

import cn.cuiot.dmp.lease.dto.charge.*;
import cn.cuiot.dmp.lease.entity.charge.TbSecuritydepositManager;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface TbSecuritydepositManagerMapper extends BaseMapper<TbSecuritydepositManager> {
    IPage<SecuritydepositManagerPageDto> queryForPage(Page page, @Param("query") SecuritydepositManagerQuery query);

    int refund(@Param("query") SecuritydepositRefundDto refundAmount);

    Integer getHouseReundableAmount(Long houseId);

    int receivedAmount(@Param("dto") TbSecuritydepositManager dto);

    int updateChargePayStatus(@Param("chargeIds") List<Long> chargeIds, @Param("orderId") Long orderId);

    List<ChargePayToWechatDetailDto> queryForPayToWechat(@Param("chargeIds") List<Long> chargeIds);

    int updateChargePayStatusToCancel(@Param("chargeIds") List<Long> chargeIds);

    int updateChargePayStatusToPaySuccess(@Param("query") ChargeOrderPaySuccInsertDto chargeOrderPaySuccInsertDto);

    IPage<Chargeovertimeorderdto> queryNeedPayPage(Page<Chargeovertimeorderdto> page);

    PrePayAmountAndHouseId queryNeedToPayAmount(Long chargeId);

    int updateChargePayStatusToPaySuccessBYPrePay(@Param("chargeId") Long chargeId, @Param("needToPayAmount") Integer needToPayAmount,@Param("orderId") Long orderId);

    IPage<Chargeovertimeorderdto> queryOverTimeOrderAndClosePage(Page<Chargeovertimeorderdto> chargeovertimeorderdtoPage,@Param("date") Date date);

}