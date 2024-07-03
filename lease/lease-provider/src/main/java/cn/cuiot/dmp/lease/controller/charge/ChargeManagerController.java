package cn.cuiot.dmp.lease.controller.charge;


import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.BaseUserDto;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.req.BaseUserReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.CommonOptionSettingReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.CommonOptionSettingRspDTO;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.common.enums.CustomerIdentityTypeEnum;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.lease.dto.charge.*;
import cn.cuiot.dmp.lease.entity.charge.TbChargeAbrogate;
import cn.cuiot.dmp.lease.entity.charge.TbChargeHangup;
import cn.cuiot.dmp.lease.entity.charge.TbChargeManager;
import cn.cuiot.dmp.lease.entity.charge.TbChargeReceived;
import cn.cuiot.dmp.lease.enums.*;
import cn.cuiot.dmp.lease.feign.SystemToFlowService;
import cn.cuiot.dmp.lease.service.charge.ChargeHouseAndUserService;
import cn.cuiot.dmp.lease.service.charge.ChargeInfoFillService;
import cn.cuiot.dmp.lease.service.charge.TbChargeManagerService;
import cn.cuiot.dmp.system.domain.repository.CommonOptionTypeRepository;
import cn.cuiot.dmp.util.Sm4;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 收费管理-应收管理-收银台-缴费管理
 *
 * @author libo
 * @date 2024/6/12
 */
@RestController
@RequestMapping("/chargemanager")
public class ChargeManagerController {

    @Autowired
    private TbChargeManagerService tbChargeManagerService;
    @Autowired
    private ChargeHouseAndUserService chargeHouseAndUserService;
    @Autowired
    private SystemToFlowService systemToFlowService;
    @Autowired
    private ChargeInfoFillService chargeInfoFillService;

    public static final String SERVICETYPENAME = "缴费管理";


    /**
     * 获取房屋欠费等相关信息
     *
     * @param idParam
     * @return
     */
    @PostMapping("/queryForHouseDetail")
    public IdmResDTO<ChargeHouseDetailDto> queryForHouseDetail(@RequestBody @Valid IdParam idParam) {
        ChargeHouseDetailDto chargeHouseDetailDto = tbChargeManagerService.queryForHouseDetail(idParam.getId());
        List<HouseInfoDto> houseInfoDtos = chargeHouseAndUserService.getHouseInfoByIds(Lists.newArrayList(idParam.getId()));
        if (CollectionUtils.isNotEmpty(houseInfoDtos)) {
            chargeHouseDetailDto.setHouseCode(houseInfoDtos.get(0).getHouseCode());
            chargeHouseDetailDto.setHouseName(houseInfoDtos.get(0).getHouseName());
        }

        ChargeHouseDetailDto ownerInfo = chargeHouseAndUserService.getOwnerInfo(idParam.getId());
        if (Objects.nonNull(ownerInfo)) {
            chargeHouseDetailDto.setOwnerName(ownerInfo.getOwnerName());
            if(StringUtils.isNotBlank(ownerInfo.getOwnerPhone())){
                chargeHouseDetailDto.setOwnerPhone(StringUtils.join(Arrays.stream(StringUtils.split(ownerInfo.getOwnerPhone(),",")).map(e -> Sm4.decrypt(e)).toArray()));
            }
        }
        chargeHouseDetailDto.setHouseId(idParam.getId());
        return IdmResDTO.success().body(chargeHouseDetailDto);
    }

    /**
     * 获取房屋客户信息
     * @param
     * @return
     */
    @PostMapping("/queryHouseCustmerPage")
    public IdmResDTO<IPage<CustomerUserInfo>> queryHouseCustmerPage(@RequestBody @Valid HouseCustomerQuery query) {
        return IdmResDTO.success().body(tbChargeManagerService.queryHouseCustmerPage(query));
    }

    /**
     * 获取分页
     *
     * @param query
     * @return
     */
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<ChargeManagerPageDto>> queryForPage(@RequestBody TbChargeManagerQuery query) {
        AssertUtil.notNull(query.getHouseId(), "房屋id不能为空");
        query.setAbrogateStatus(ChargeAbrogateEnum.NORMAL.getCode());
        query.setCompanyId(LoginInfoHolder.getCurrentOrgId());

        IPage<ChargeManagerPageDto> chargeManagerPageDtoIPage = tbChargeManagerService.queryForPage(query);
        if (Objects.nonNull(chargeManagerPageDtoIPage) && CollectionUtils.isNotEmpty(chargeManagerPageDtoIPage.getRecords())) {
            List<Long> houseIds = chargeManagerPageDtoIPage.getRecords().stream().map(ChargeManagerPageDto::getHouseId).collect(Collectors.toList());
            List<Long> userIds = chargeManagerPageDtoIPage.getRecords().stream().map(ChargeManagerPageDto::getCustomerUserId).collect(Collectors.toList());

            List<CustomerUserInfo> userInfoList = chargeHouseAndUserService.getUserInfo(houseIds, userIds);

            if (CollectionUtils.isNotEmpty(userInfoList)) {
                for (ChargeManagerPageDto record : chargeManagerPageDtoIPage.getRecords()) {
                    if(CollectionUtils.isNotEmpty(userInfoList)){
                        for (CustomerUserInfo userInfo : userInfoList) {
                            if (Objects.equals(record.getCustomerUserId(), userInfo.getCustomerUserId())) {
                                record.setCustomerUserName(userInfo.getCustomerUserName());
                                record.setCustomerUserPhone(userInfo.getCustomerUserPhone());

                                if (Objects.equals(record.getHouseId(), userInfo.getHouseId())) {
                                    if (Objects.nonNull(userInfo.getIdentityType())) {
                                        CustomerIdentityTypeEnum customerIdentityTypeEnum = CustomerIdentityTypeEnum.parseByCode(userInfo.getIdentityType().toString());
                                        if (Objects.nonNull(customerIdentityTypeEnum)) {
                                            record.setCustomerUserRoleName(customerIdentityTypeEnum.getName());
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            chargeInfoFillService.fillinfo(chargeManagerPageDtoIPage.getRecords(),ChargeManagerPageDto.class);
        }
        return IdmResDTO.success().body(chargeManagerPageDtoIPage);
    }

    /**
     * 获取详情
     *
     * @param idParam
     * @return
     */
    @PostMapping("/queryForDetail")
    public IdmResDTO<ChargeManagerDetailDto> queryForDetail(@RequestBody @Valid IdParam idParam) {
        ChargeManagerDetailDto chargeManagerDetailDto = tbChargeManagerService.queryForDetail(idParam.getId());
        //填充客户信息
        if (Objects.nonNull(chargeManagerDetailDto)) {
            List<Long> houseIds = Lists.newArrayList(chargeManagerDetailDto.getHouseId());
            List<Long> userIds = Lists.newArrayList(chargeManagerDetailDto.getCustomerUserId());

            List<CustomerUserInfo> userInfoList = chargeHouseAndUserService.getUserInfo(houseIds, userIds);
            if (CollectionUtils.isNotEmpty(userInfoList)) {
                chargeManagerDetailDto.setCustomerUserName(userInfoList.get(0).getCustomerUserName());
                chargeManagerDetailDto.setCustomerUserPhone(userInfoList.get(0).getCustomerUserPhone());

                if (Objects.nonNull(userInfoList.get(0).getIdentityType())) {
                    CustomerIdentityTypeEnum customerIdentityTypeEnum = CustomerIdentityTypeEnum.parseByCode(userInfoList.get(0).getIdentityType().toString());
                    if (Objects.nonNull(customerIdentityTypeEnum)) {
                        chargeManagerDetailDto.setCustomerUserRoleName(customerIdentityTypeEnum.getName());
                    }
                }
            }

            List<HouseInfoDto> houseInfoDtos = chargeHouseAndUserService.getHouseInfoByIds(Lists.newArrayList(chargeManagerDetailDto.getHouseId()));
            if (CollectionUtils.isNotEmpty(houseInfoDtos)) {
                chargeManagerDetailDto.setHouseCode(houseInfoDtos.get(0).getHouseCode());
                chargeManagerDetailDto.setHouseName(houseInfoDtos.get(0).getHouseName());
            }

            chargeInfoFillService.fillinfo(Lists.newArrayList(chargeManagerDetailDto),ChargeManagerDetailDto.class);

            //填充操作人员名称
            BaseUserReqDto baseUserReqDto = new BaseUserReqDto();
            baseUserReqDto.setUserIdList(Lists.newArrayList(chargeManagerDetailDto.getCreateUser()));
            List<BaseUserDto> baseUserDtos = systemToFlowService.lookUpUserList(baseUserReqDto);
            if(CollectionUtils.isNotEmpty(baseUserDtos)){
                chargeManagerDetailDto.setCreateUserName(baseUserDtos.get(0).getName());
            }
        }
        return IdmResDTO.success().body(chargeManagerDetailDto);
    }

    /**
     * 获取挂解明细分页
     *
     * @param queryDto
     * @return
     */
    @PostMapping("/queryForHangupPage")
    public IdmResDTO<IPage<TbChargeHangup>> queryForHangupPage(@RequestBody @Valid ChargeHangupQueryDto queryDto) {
        IPage<TbChargeHangup> tbChargeHangupIPage = tbChargeManagerService.queryForHangupPage(queryDto);
        if(Objects.nonNull(tbChargeHangupIPage) && CollectionUtils.isNotEmpty(tbChargeHangupIPage.getRecords())){
            List<Long> userIds = tbChargeHangupIPage.getRecords().stream().map(TbChargeHangup::getCreateUser).distinct().collect(Collectors.toList());
            BaseUserReqDto baseUserReqDto = new BaseUserReqDto();
            baseUserReqDto.setUserIdList(userIds);
            List<BaseUserDto> baseUserDtoList = systemToFlowService.lookUpUserList(baseUserReqDto);
            if(CollectionUtils.isNotEmpty(baseUserDtoList)){
                for (TbChargeHangup record : tbChargeHangupIPage.getRecords()) {
                    for (BaseUserDto baseUserDto : baseUserDtoList) {
                        if(Objects.equals(record.getCreateUser(),baseUserDto.getId())){
                            record.setOperatorName(baseUserDto.getName());
                            break;
                        }
                    }
                }
            }
        }
        return IdmResDTO.success().body(tbChargeHangupIPage);
    }

    /**
     * 获取作废明细分页
     *
     * @param queryDto
     * @return
     */
    @PostMapping("/queryForAbrogatePage")
    public IdmResDTO<IPage<TbChargeAbrogate>> queryForAbrogatePage(@RequestBody @Valid ChargeHangupQueryDto queryDto) {
        queryDto.setDataType(ChargeAbrogateTypeEnum.CHARGE.getCode());

        IPage<TbChargeAbrogate> tbChargeHangupIPage = tbChargeManagerService.queryForAbrogatePage(queryDto);
        if(Objects.nonNull(tbChargeHangupIPage) && CollectionUtils.isNotEmpty(tbChargeHangupIPage.getRecords())){
            List<Long> userIds = tbChargeHangupIPage.getRecords().stream().map(TbChargeAbrogate::getCreateUser).distinct().collect(Collectors.toList());
            BaseUserReqDto baseUserReqDto = new BaseUserReqDto();
            baseUserReqDto.setUserIdList(userIds);
            List<BaseUserDto> baseUserDtoList = systemToFlowService.lookUpUserList(baseUserReqDto);
            if(CollectionUtils.isNotEmpty(baseUserDtoList)){
                for (TbChargeAbrogate record : tbChargeHangupIPage.getRecords()) {
                    for (BaseUserDto baseUserDto : baseUserDtoList) {
                        if(Objects.equals(record.getCreateUser(),baseUserDto.getId())){
                            record.setOperatorName(baseUserDto.getName());
                            break;
                        }
                    }
                }
            }
        }

        return IdmResDTO.success().body(tbChargeHangupIPage);
    }

    /**
     * 获取收款明细分页
     *
     * @param queryDto
     * @return
     */
    @PostMapping("/queryForReceivedPage")
    public IdmResDTO<IPage<TbChargeReceived>> queryForReceivedPage(@RequestBody @Valid ChargeHangupQueryDto queryDto) {
        IPage<TbChargeReceived> tbChargeHangupIPage = tbChargeManagerService.queryForReceivedPage(queryDto);
        if(Objects.nonNull(tbChargeHangupIPage) && CollectionUtils.isNotEmpty(tbChargeHangupIPage.getRecords())){
            chargeInfoFillService.fillinfo(tbChargeHangupIPage.getRecords(),TbChargeReceived.class);
        }
        return IdmResDTO.success().body(tbChargeHangupIPage);
    }

    /**
     * 创建
     *
     * @param createDto
     * @return
     */
    @RequiresPermissions
    @PostMapping("/create")
    @LogRecord(operationCode = "create", operationName = "缴费管理-创建", serviceType = ServiceTypeConst.RECEIVED_MANAGER, serviceTypeName = ServiceTypeConst.BASE_CONFIG)
    public IdmResDTO create(@RequestBody @Valid ChargeManagerInsertVo createDto) {
        createDto.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        tbChargeManagerService.saveData(createDto, ChargeTypeEnum.MANUAL_CREATE.getCode(), null);
        return IdmResDTO.success();
    }

    /**
     * 挂起/解挂
     *
     * @param idParam
     * @return
     */
    @RequiresPermissions
    @PostMapping("/updateHangUpStatus")
    @LogRecord(operationCode = "updateHangUpStatus", operationName = "缴费管理-挂起/解挂", serviceType = ServiceTypeConst.RECEIVED_MANAGER, serviceTypeName = ServiceTypeConst.BASE_CONFIG)
    public IdmResDTO updateHangUpStatus(@RequestBody @Valid ChargeAbrogateInsertDto idParam) {
        TbChargeManager entity = tbChargeManagerService.getById(idParam.getDataId());
        AssertUtil.notNull(entity, "数据不存在");

        tbChargeManagerService.updateHangUpStatus(entity, idParam.getAbrogateDesc());
        return IdmResDTO.success();
    }

    /**
     * 作废
     *
     * @param idParam
     * @return
     */
    @RequiresPermissions
    @PostMapping("/abrogateStatus")
    @LogRecord(operationCode = "abrogateStatus", operationName = "缴费管理-作废", serviceType = ServiceTypeConst.RECEIVED_MANAGER, serviceTypeName = ServiceTypeConst.BASE_CONFIG)
    public IdmResDTO abrogateStatus(@RequestBody @Valid ChargeAbrogateInsertDto idParam) {
        TbChargeManager entity = tbChargeManagerService.getById(idParam.getDataId());
        AssertUtil.notNull(entity, "数据不存在");
        AssertUtil.isFalse(ChargeReceivbleEnum.isShowAbrogate(entity.getReceivbleStatus()), "已开交、已交清的状态不显示作废按钮");
        AssertUtil.isTrue(Objects.equals(ChargeHangUpEnum.HANG_UP.getCode(), entity.getHangUpStatus()), "已挂起的数据不能作废");

        tbChargeManagerService.abrogateStatus(entity, idParam.getAbrogateDesc());
        return IdmResDTO.success();
    }

    /**
     * 收款
     *
     * @param
     * @return
     */
    @RequiresPermissions
    @PostMapping("/receivedAmount")
    @LogRecord(operationCode = "receivedAmount", operationName = "缴费管理-收款", serviceType = ServiceTypeConst.RECEIVED_MANAGER, serviceTypeName = ServiceTypeConst.BASE_CONFIG)
    public IdmResDTO receivedAmount(@RequestBody @Valid ChargeReceiptsReceivedDto dto) {
        tbChargeManagerService.receivedAmount(dto);
        return IdmResDTO.success();
    }


}

