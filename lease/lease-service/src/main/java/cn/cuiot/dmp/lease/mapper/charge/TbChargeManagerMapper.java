package cn.cuiot.dmp.lease.mapper.charge;

import cn.cuiot.dmp.lease.dto.charge.*;
import cn.cuiot.dmp.lease.entity.charge.TbChargeManager;
import cn.cuiot.dmp.lease.entity.charge.TbChargeReceived;
import cn.cuiot.dmp.lease.dto.charge.PrePayAmountAndHouseId;
import cn.cuiot.dmp.lease.vo.ChargeCollectionManageVo;
import cn.cuiot.dmp.lease.vo.ChargeManagerCustomerStatisticsVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbChargeManagerMapper extends BaseMapper<TbChargeManager> {
    IPage<ChargeManagerPageDto> queryForPage(Page page, @Param("query") TbChargeManagerQuery query);

    ChargeHouseDetailDto queryForHouseDetail(Long id);

    int receivedAmount(@Param("received") TbChargeReceived received);

    List<HouseInfoDto> getHouseInfoByIds(@Param("ids") List<Long> ids);

    ChargeHouseDetailDto getOwnerInfo(Long houseId);

    List<CustomerUserInfo> getUserInfo(@Param("houseIds") List<Long> houseIds, @Param("userIds") List<Long> userIds);

    List<CustomerUserInfo> getUserInfoByIds(@Param("userIds") List<Long> userIds);

    IPage<CustomerUserInfo> queryHouseCustmerPage(Page page, @Param("query") HouseCustomerQuery query);

    int insertList(@Param("list") List<TbChargeManager> list);

    /**
     * 催款管理分页
     */
    IPage<ChargeCollectionManageVo> queryCollectionManagePage(Page page, @Param("params") ChargeCollectionManageQuery query);

    /**
     * 催款管理-客户应收统计
     */
    ChargeManagerCustomerStatisticsVo customerStatistics(@Param("query") TbChargeManagerQuery query);

    /**
     * 催款管理-查询用户欠费统计用于发送消息
     */
    IPage<ChargeCollectionManageSendDto> queryUserArrearsStatistics(Page page, @Param("params") ChargeCollectionManageSendQuery query);

    IPage<AppChargeManagerDto> appChargeManager(Page page, @Param("query") AppChargemanagerQuery query);

    int updateChargePayStatus(@Param("chargeIds") List<Long> chargeIds, @Param("orderId") Long orderId);

    List<ChargePayToWechatDetailDto> queryForPayToWechat(@Param("chargeIds") List<Long> chargeIds);

    IPage<Chargeovertimeorderdto> queryNeedPayPage(Page<Chargeovertimeorderdto> page);

    int updateChargePayStatusToSuccsess(@Param("chargeIds") List<Long> chargeIds);

    int updateChargePayStatusToCancel(@Param("chargeIds") List<Long> chargeIds);

    PrePayAmountAndHouseId queryNeedToPayAmount(Long chargeId);

    int updateChargePayStatusToPaySuccessBYPrePay(@Param("chargeId") Long chargeId, @Param("needToPayAmount") Integer needToPayAmount);

    IPage<Chargeovertimeorderdto> queryOverTimeOrderAndClosePage(Page<Chargeovertimeorderdto> chargeovertimeorderdtoPage);
}