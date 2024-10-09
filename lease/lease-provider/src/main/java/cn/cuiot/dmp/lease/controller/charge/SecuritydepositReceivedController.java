package cn.cuiot.dmp.lease.controller.charge;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.lease.dto.charge.CustomerUserInfo;
import cn.cuiot.dmp.lease.dto.charge.HouseInfoDto;
import cn.cuiot.dmp.lease.dto.charge.SecuritydepositManagerPageDto;
import cn.cuiot.dmp.lease.dto.charge.SecuritydepositManagerQuery;
import cn.cuiot.dmp.lease.service.charge.ChargeHouseAndUserService;
import cn.cuiot.dmp.lease.service.charge.ChargeInfoFillService;
import cn.cuiot.dmp.lease.service.charge.TbSecuritydepositManagerService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 收费管理-实收管理-押金实收
 * @Description 押金实收
 * @Date 2024/6/17 11:51
 * @Created by libo
 */
@RestController
@RequestMapping("/securitydepositReceived")
public class SecuritydepositReceivedController {

    @Autowired
    private TbSecuritydepositManagerService securitydepositManagerService;
    @Autowired
    private ChargeHouseAndUserService chargeHouseAndUserService;
    @Autowired
    private ChargeInfoFillService chargeInfoFillService;

    /**
     * 获取分页
     *
     * @param query
     * @return
     */
    @RequiresPermissions
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<SecuritydepositManagerPageDto>> queryForPage(@RequestBody SecuritydepositManagerQuery query) {
        query.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        query.setSelectReceived(EntityConstants.YES);
        IPage<SecuritydepositManagerPageDto> chargeManagerPageDtoIPage = securitydepositManagerService.queryForPage(query);
        if (Objects.nonNull(chargeManagerPageDtoIPage) && CollectionUtils.isNotEmpty(chargeManagerPageDtoIPage.getRecords())) {

            List<Long> userIds = chargeManagerPageDtoIPage.getRecords().stream().map(SecuritydepositManagerPageDto::getCustomerUserId).distinct().collect(Collectors.toList());
            List<CustomerUserInfo> userInfoByIds = chargeHouseAndUserService.getUserInfoByIds(userIds);
            if (CollectionUtils.isNotEmpty(userInfoByIds)) {
                Map<Long, CustomerUserInfo> userInfoMap = userInfoByIds.stream().collect(Collectors.toMap(CustomerUserInfo::getCustomerUserId, Function.identity()));

                //填充客户名称
                for (SecuritydepositManagerPageDto record : chargeManagerPageDtoIPage.getRecords()) {
                    if (userInfoMap.containsKey(record.getCustomerUserId())) {
                        record.setCustomerUserName(userInfoMap.get(record.getCustomerUserId()).getCustomerUserName());
                    }
                }
            }
            List<Long> houseIds = chargeManagerPageDtoIPage.getRecords().stream().map(SecuritydepositManagerPageDto::getHouseId).distinct().collect(Collectors.toList());

            List<HouseInfoDto> houseInfoDtos = chargeHouseAndUserService.getHouseInfoByIds(Lists.newArrayList(houseIds));
            if (CollectionUtils.isNotEmpty(houseInfoDtos)) {
                Map<Long, HouseInfoDto> userInfoMap = houseInfoDtos.stream().collect(Collectors.toMap(HouseInfoDto::getHouseId, Function.identity()));

                for (SecuritydepositManagerPageDto record : chargeManagerPageDtoIPage.getRecords()) {
                    if (userInfoMap.containsKey(record.getHouseId())) {
                        record.setHouseName(userInfoMap.get(record.getHouseId()).getHouseName());
                        record.setHouseCode(userInfoMap.get(record.getHouseId()).getHouseCode());

                    }
                }
            }

            chargeInfoFillService.fillinfo(chargeManagerPageDtoIPage.getRecords(),SecuritydepositManagerPageDto.class);

        }
        return IdmResDTO.success().body(chargeManagerPageDtoIPage);
    }

}
