package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.RegexConst;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.common.utils.Const;
import cn.cuiot.dmp.common.utils.ValidateUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.system.application.service.OrganizationService;
import cn.cuiot.dmp.system.application.service.UserService;
import cn.cuiot.dmp.system.infrastructure.entity.dto.CompanyReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.CompanyReqDto.CompanyDetailReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.CompanyResDto.CompanyDetailResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.InsertOrganizationDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.ListOrganizationDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.OperateOrganizationDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.OrgTypeDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.OrganizationResDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.ResetUserPasswordReqDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UpdateOrganizationDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UserDataReqDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UserDataResDTO;
import cn.cuiot.dmp.system.infrastructure.entity.vo.GetOrganizationVO;
import cn.cuiot.dmp.system.infrastructure.entity.vo.ListOrganizationVO;
import cn.hutool.core.util.PhoneUtil;
import java.util.List;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 企业与账号管理
 * @author wuyongchong
 * @date 2020-10-23 10:00:07
 */
@RestController
@RequestMapping(value = "/organization")
public class OrganizationController extends BaseController {

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private UserService userService;

    /**
     * 应用id长度常量
     */
    private static final String DEPT_ID_ADMIN_ROOT = "1";

    /**
     * 超级账户账户id
     */
    private static final String SUP_ORGID = "1";

    /**
     * 新增企业与账号
     * @param dto
     */
    @RequiresPermissions("system:account:add")
    @PostMapping(value = "/insertOrganization", produces = MediaType.APPLICATION_JSON_VALUE)
    public void insertSonOrganization(@RequestBody @Valid InsertOrganizationDto dto) {
        if (!PhoneUtil.isPhone(dto.getPhoneNumber())) {
            throw new BusinessException(ResultCode.PHONE_NUMBER_IS_NOT_VALID,"请输入正确的11位手机号");
        }
        dto.setSessionOrgId(LoginInfoHolder.getCurrentOrgId());
        dto.setSessionUserId(LoginInfoHolder.getCurrentUserId());
        organizationService.insertOrganization(dto);
    }

    /**
     * 编辑企业与账号
     * @param dto
     */
    @RequiresPermissions("system:account:edit")
    @PostMapping(value = "/updateOrganization", produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateOrganization(@RequestBody @Valid UpdateOrganizationDto dto) {
        if (!getOrgId().equals(SUP_ORGID)) {
            throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
        }
        dto.setSessionOrgId(LoginInfoHolder.getCurrentOrgId());
        dto.setSessionUserId(LoginInfoHolder.getCurrentUserId());
        organizationService.updateOrganization(dto);
    }


    /**
     * 企业与账号详情
     * @param id
     * @return
     */
    @RequiresPermissions("system:account:control")
    @GetMapping(value = "/getOrganizationInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    public GetOrganizationVO getOrganizationInfo(@RequestParam("id") String id) {
        if (StringUtils.isBlank(id)) {
            throw new BusinessException(ResultCode.PARAM_CANNOT_NULL);
        }
        String sessionUserId =LoginInfoHolder.getCurrentUserId().toString();
        String sessionOrgId = LoginInfoHolder.getCurrentOrgId().toString();
        return organizationService.findOne(id, sessionUserId, sessionOrgId);
    }

    /**
     * 账户详情下的用户分页列表
     * @param userDataReqDTO
     * @return
     */
    @RequiresPermissions("system:account:control")
    @PostMapping(value = "/user/pageList", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResult<UserDataResDTO> queryUserPageList(@Validated @RequestBody UserDataReqDTO userDataReqDTO) {
        userDataReqDTO.init();
        userDataReqDTO.setSessionOrgId(getOrgId());
        userDataReqDTO.setSessionUserId(getUserId());
        return organizationService.queryUserPageList(userDataReqDTO);
    }

    /**
     * 账户详情下的用户,重置密码
     * @param resetUserPasswordReqDTO
     */
    @RequiresPermissions("system:account:control")
    @LogRecord(operationCode = "resetPassword", operationName = "admin重置账户详情下用户的密码", serviceType = ServiceTypeConst.ORGANIZATION_MANAGEMENT)
    @PostMapping(value = "/user/resetPassword", produces = MediaType.APPLICATION_JSON_VALUE)
    public void resetUserPassword(@Validated @RequestBody ResetUserPasswordReqDTO resetUserPasswordReqDTO) {
        resetUserPasswordReqDTO.setSessionOrgId(getOrgId());
        resetUserPasswordReqDTO.setSessionUserId(getUserId());
        organizationService.resetUserPassword(resetUserPasswordReqDTO);
    }


    /**
     * 账户列表
     * @param dto
     * @return
     */
    @GetMapping(value = "/listOrganization", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResult<ListOrganizationVO> listOrganization(ListOrganizationDto dto) {
        if (dto.getPageSize() > 100) {
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT);
        }
        ValidateUtil.validate(dto);
        String orgId = getOrgId();
        dto.setLoginOrgId(Long.parseLong(orgId));
        // 组织为空则使用当前登陆用户的组织
        if (StringUtils.isEmpty(dto.getDeptId())) {
            String deptId = userService.getDeptId(getUserId(), orgId);
            if (DEPT_ID_ADMIN_ROOT.equals(deptId)) {
                // 联通物联网总部账号需要看到所有组织，包括组织信息是空的企业账户。设置null才能看到。 超级管理员deptId本身就是空所以也能看到。
                dto.setDeptId(null);
            }
            else {
                dto.setDeptId(deptId);
            }
        }
        return organizationService.commercialOrgList(dto);
    }

    /**
     * 启用/禁用账户
     * @param dto
     * @return
     */
    @RequiresPermissions("system:account:edit")
    @PostMapping(value = "/operateOrganization", produces = MediaType.APPLICATION_JSON_VALUE)
    public Integer operateOrganization(@RequestBody @Valid OperateOrganizationDto dto) {
        String userId = getUserId();
        dto.setUserId(userId);
        return organizationService.operateOrganization(dto);
    }

    /**
     * 账户类型列表
     * @return
     */
    @GetMapping(value = "/orgTypeList", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<OrgTypeDto> orgTypeList() {
        return organizationService.getOrgTypeList();
    }

    @PostMapping("/deleteAccount")
    public int deleteAccount(@RequestParam("accountId") String accountId) {
        OrganizationResDTO organizationResDto = organizationService.getOneById(accountId);
        if (null == organizationResDto) return Const.NUMBER_0;
        int result = organizationService.deleteAccount(accountId);
        return result;
    }

    /**
     * 查询企业详情，仅物业用户可用，账户下用户均可查询
     * @return
     */
    @PostMapping("/companyDetail")
    public CompanyDetailResDto companyDetail() {
        CompanyDetailReqDto companyDetailReqDto = new CompanyDetailReqDto();
        companyDetailReqDto.setOrgId(getOrgId());
        return organizationService.companyDetail(companyDetailReqDto);
    }

    /**
     * 编辑企业详情，只有未编辑过才可以编辑，仅物业用户可用，账户下用户均可编辑
     * @return
     */
    @PostMapping("/updateCompany")
    public void updateCompany(@Valid @RequestBody CompanyReqDto.UpdateCompanyReqDto updateCompanyReqDto) {
        updateCompanyReqDto.setOrgId(getOrgId());
        updateCompanyReqDto.setUserId(getUserId());
        organizationService.updateCompany(updateCompanyReqDto);
    }

}
