package cn.cuiot.dmp.lease.controller.charge;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.dto.ExcelReportDto;
import cn.cuiot.dmp.base.application.service.ExcelExportService;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.NumberConst;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.lease.dto.charge.CustomerUserInfo;
import cn.cuiot.dmp.lease.dto.charge.PaidInManageMentQuery;
import cn.cuiot.dmp.lease.entity.charge.TbChargeReceived;
import cn.cuiot.dmp.lease.service.charge.ChargeHouseAndUserService;
import cn.cuiot.dmp.lease.service.charge.ChargeInfoFillService;
import cn.cuiot.dmp.lease.service.charge.TbChargeReceivedService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 收费管理-实收管理-实收管理
 *
 * @Description 实收管理
 * @Date 2024/6/13 15:16
 * @Created by libo
 */
@RestController
@RequestMapping("/paidInManageMent")
public class PaidInManageMentController {
    @Autowired
    private TbChargeReceivedService tbChargeReceivedService;
    @Autowired
    private ChargeHouseAndUserService chargeHouseAndUserService;
    @Autowired
    private ChargeInfoFillService chargeInfoFillService;

    @Autowired
    private ExcelExportService excelExportService;
    /**
     * 获取分页
     *
     * @param query
     * @return
     */
    @RequiresPermissions
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<TbChargeReceived>> queryForPaidinPage(@RequestBody PaidInManageMentQuery query) {
        AssertUtil.notNull(query.getHouseId(), "房屋id不能为空");

        query.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        IPage<TbChargeReceived> chargeManagerPageDtoIPage = tbChargeReceivedService.queryForPaidinPage(query);
        if (Objects.nonNull(chargeManagerPageDtoIPage) && CollectionUtils.isNotEmpty(chargeManagerPageDtoIPage.getRecords())) {

            List<Long> userIds = chargeManagerPageDtoIPage.getRecords().stream().map(TbChargeReceived::getCustomerUserId).distinct().collect(Collectors.toList());
            List<CustomerUserInfo> userInfoByIds = chargeHouseAndUserService.getUserInfoByIds(userIds);
            if (CollectionUtils.isNotEmpty(userInfoByIds)) {
                Map<Long, CustomerUserInfo> userInfoMap = userInfoByIds.stream().collect(Collectors.toMap(CustomerUserInfo::getCustomerUserId, Function.identity()));

                //填充客户名称
                for (TbChargeReceived record : chargeManagerPageDtoIPage.getRecords()) {
                    if (userInfoMap.containsKey(record.getCustomerUserId())) {
                        record.setCustomerUserName(userInfoMap.get(record.getCustomerUserId()).getCustomerUserName());
                        record.setCustomerUserPhone(userInfoMap.get(record.getCustomerUserId()).getCustomerUserPhone());
                    }
                }
            }

            chargeInfoFillService.fillinfo(chargeManagerPageDtoIPage.getRecords(),TbChargeReceived.class);

        }
        return IdmResDTO.success().body(chargeManagerPageDtoIPage);
    }

    /**
     * 实收导出
     * @param dto
     * @throws Exception
     */
    @RequiresPermissions
    @PostMapping("/export")
    public void export(@RequestBody PaidInManageMentQuery dto) throws Exception {
        excelExportService.excelExport(ExcelReportDto.<PaidInManageMentQuery,TbChargeReceived>builder().title("实收导出").fileName("实收导出")
                .dataList(queryChargeReceived(dto)).build(),TbChargeReceived.class);
    }

    /**
     * 获取列表信息
     * @param query
     * @return
     */
    public List<TbChargeReceived>  queryChargeReceived(PaidInManageMentQuery query){
        AssertUtil.notNull(query.getHouseId(), "房屋id不能为空");

        query.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        Boolean flag =true;
        Long pageNo = 1L;
        query.setPageSize(NumberConst.PAGE_MAX_SIZE);
        List<TbChargeReceived> resultList = new ArrayList<>();
        do {
            query.setPageNo(pageNo);
            IPage<TbChargeReceived> chargeManagerPageDtoIPage = tbChargeReceivedService.queryForPaidinPage(query);
            if(chargeManagerPageDtoIPage.getTotal()> NumberConst.QUERY_MAX_SIZE){
                throw new BusinessException(ResultCode.EXPORT_DATA_OVER_LIMIT);
            }
            if(CollectionUtils.isEmpty(chargeManagerPageDtoIPage.getRecords())){
                flag=false;
            }
            if (Objects.nonNull(chargeManagerPageDtoIPage) && CollectionUtils.isNotEmpty(chargeManagerPageDtoIPage.getRecords())) {

                List<Long> userIds = chargeManagerPageDtoIPage.getRecords().stream().map(TbChargeReceived::getCustomerUserId).distinct().collect(Collectors.toList());
                List<CustomerUserInfo> userInfoByIds = chargeHouseAndUserService.getUserInfoByIds(userIds);
                if (CollectionUtils.isNotEmpty(userInfoByIds)) {
                    Map<Long, CustomerUserInfo> userInfoMap = userInfoByIds.stream().collect(Collectors.toMap(CustomerUserInfo::getCustomerUserId, Function.identity()));

                    //填充客户名称
                    for (TbChargeReceived record : chargeManagerPageDtoIPage.getRecords()) {
                        if (userInfoMap.containsKey(record.getCustomerUserId())) {
                            record.setCustomerUserName(userInfoMap.get(record.getCustomerUserId()).getCustomerUserName());
                            record.setCustomerUserPhone(userInfoMap.get(record.getCustomerUserId()).getCustomerUserPhone());
                        }
                    }
                }

                chargeInfoFillService.fillinfo(chargeManagerPageDtoIPage.getRecords(), TbChargeReceived.class);
                pageNo++;
                resultList.addAll(chargeManagerPageDtoIPage.getRecords());
            }
        }while (flag) ;

            return resultList;
        }
}
