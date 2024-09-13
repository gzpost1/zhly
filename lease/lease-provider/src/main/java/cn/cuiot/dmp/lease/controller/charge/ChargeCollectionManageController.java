package cn.cuiot.dmp.lease.controller.charge;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.lease.dto.charge.ChargeCollectionManageQuery;
import cn.cuiot.dmp.lease.dto.charge.ChargeCollectionManageRecordQuery;
import cn.cuiot.dmp.lease.dto.charge.ChargeCollectionManageSendQuery;
import cn.cuiot.dmp.lease.dto.charge.TbChargeManagerQuery;
import cn.cuiot.dmp.lease.enums.ChargeCollectionTypeEnum;
import cn.cuiot.dmp.lease.service.charge.ChargeCollectionManageService;
import cn.cuiot.dmp.lease.service.charge.TbChargeManagerService;
import cn.cuiot.dmp.lease.vo.ChargeCollectionManageVo;
import cn.cuiot.dmp.lease.vo.ChargeCollectionRecordVo;
import cn.cuiot.dmp.lease.vo.ChargeManagerCustomerStatisticsVo;
import cn.cuiot.dmp.util.Sm4;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Objects;

/**
 * 催缴管理
 *
 * @Author: zc
 * @Date: 2024-06-25
 */
@RestController
@RequestMapping("/charge/collectionManage")
public class ChargeCollectionManageController {

    @Autowired
    private ChargeCollectionManageService chargeCollectionManageService;
    @Autowired
    private TbChargeManagerService tbChargeManagerService;

    /**
     * 管理列表-分页查询
     */
    @RequiresPermissions
    @PostMapping("queryForPage")
    public IdmResDTO<IPage<ChargeCollectionManageVo>> queryForPage(@RequestBody ChargeCollectionManageQuery query) {
        //获取前一天23:59:59
        Date date = DateTimeUtil.localDateTimeToDate(LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MAX)
                .withNano(999999000));
        query.setDueDate(date);
        query.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        if (StringUtils.isNotBlank(query.getCustomerUserPhone())) {
            query.setCustomerUserPhone(Sm4.encryption(query.getCustomerUserPhone()));
        }
        return IdmResDTO.success(chargeCollectionManageService.queryForPage(query));
    }

    /**
     * 客户应收统计
     */
    @RequiresPermissions
    @PostMapping("/customerStatistics")
    public IdmResDTO<ChargeManagerCustomerStatisticsVo> customerStatistics(@RequestBody TbChargeManagerQuery query) {
        AssertUtil.isFalse(Objects.isNull(query.getCustomerUserId()), "客户id不能为空");
        return IdmResDTO.success(tbChargeManagerService.customerStatistics(query));
    }

    /**
     * 催款记录
     */
    @RequiresPermissions
    @PostMapping("record")
    public IdmResDTO<IPage<ChargeCollectionRecordVo>> record(@RequestBody @Valid ChargeCollectionManageRecordQuery query) {
        AssertUtil.isFalse(Objects.isNull(query.getCustomerUserId()), "客户id不能为空");
        query.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        return IdmResDTO.success(chargeCollectionManageService.record(query));
    }

    /**
     * 发送通知
     */
    @RequiresPermissions
    @PostMapping("sengMsg")
    @LogRecord(operationCode = "sengMsg", operationName = "收费管理-催款管理-发送通知", serviceType = ServiceTypeConst.CHARGE_COLLECTION_MANAGE)
    public IdmResDTO<?> sengMsg(@RequestBody @Valid ChargeCollectionManageSendQuery query) {
        //获取前一天23:59:59
        Date date = DateTimeUtil.localDateTimeToDate(LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MAX)
                .withNano(999999000));
        query.setOperationType(ChargeCollectionTypeEnum.MANUAL.getCode());
        query.setDueDate(date);
        query.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        chargeCollectionManageService.sengMsg(query);
        return IdmResDTO.success();
    }
}