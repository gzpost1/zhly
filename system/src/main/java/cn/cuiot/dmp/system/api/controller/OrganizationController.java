package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.Const;
import cn.cuiot.dmp.common.utils.ValidateUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.system.application.service.LoginService;
import cn.cuiot.dmp.system.application.service.OrganizationService;
import cn.cuiot.dmp.system.application.service.UserService;
import cn.cuiot.dmp.system.infrastructure.entity.dto.InsertOrganizationDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.ListOrganizationDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.LoginResDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.OperateOrganizationDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.OrgTypeDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.OrganizationChangeDto;
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
 *
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

    @Autowired
    private LoginService loginService;

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
     */
    @RequiresPermissions("system:account:add")
    @PostMapping(value = "/insertOrganization", produces = MediaType.APPLICATION_JSON_VALUE)
    public void insertSonOrganization(@RequestBody @Valid InsertOrganizationDto dto) {
        if (!PhoneUtil.isPhone(dto.getPhoneNumber())) {
            throw new BusinessException(ResultCode.PHONE_NUMBER_IS_NOT_VALID, "请输入正确的11位手机号");
        }
        dto.setSessionOrgId(LoginInfoHolder.getCurrentOrgId());
        dto.setSessionUserId(LoginInfoHolder.getCurrentUserId());
        organizationService.insertOrganization(dto);
    }

    /**
     * 编辑企业与账号
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
     */
    @GetMapping(value = "/getOrganizationInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    public GetOrganizationVO getOrganizationInfo(@RequestParam("id") String id) {
        if (StringUtils.isBlank(id)) {
            throw new BusinessException(ResultCode.PARAM_CANNOT_NULL);
        }
        String sessionUserId = LoginInfoHolder.getCurrentUserId().toString();
        String sessionOrgId = LoginInfoHolder.getCurrentOrgId().toString();
        return organizationService.findOne(id, sessionUserId, sessionOrgId);
    }

    /**
     * 企业账户详情下的用户分页列表
     */
    @PostMapping(value = "/user/pageList", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResult<UserDataResDTO> queryUserPageList(
            @Validated @RequestBody UserDataReqDTO userDataReqDTO) {
        userDataReqDTO.init();
        userDataReqDTO.setSessionOrgId(LoginInfoHolder.getCurrentOrgId().toString());
        userDataReqDTO.setSessionUserId(LoginInfoHolder.getCurrentUserId().toString());
        return organizationService.queryUserPageList(userDataReqDTO);
    }

    /**
     * 企业账户详情下的用户,重置密码
     */
    @RequiresPermissions("system:account:control")
    @LogRecord(operationCode = "resetPassword", operationName = "admin重置账户详情下用户的密码", serviceType = ServiceTypeConst.ORGANIZATION_MANAGEMENT)
    @PostMapping(value = "/user/resetPassword", produces = MediaType.APPLICATION_JSON_VALUE)
    public void resetUserPassword(
            @Validated @RequestBody ResetUserPasswordReqDTO resetUserPasswordReqDTO) {
        resetUserPasswordReqDTO.setSessionOrgId(LoginInfoHolder.getCurrentOrgId().toString());
        resetUserPasswordReqDTO.setSessionUserId(LoginInfoHolder.getCurrentUserId().toString());
        organizationService.resetUserPassword(resetUserPasswordReqDTO);
    }


    /**
     * 企业账户列表
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
            } else {
                dto.setDeptId(deptId);
            }
        }
        return organizationService.commercialOrgList(dto);
    }

    /**
     * 启用/禁用企业账户
     */
    @RequiresPermissions("system:account:edit")
    @PostMapping(value = "/operateOrganization", produces = MediaType.APPLICATION_JSON_VALUE)
    public Integer operateOrganization(@RequestBody @Valid OperateOrganizationDto dto) {
        String userId = getUserId();
        dto.setUserId(userId);
        return organizationService.operateOrganization(dto);
    }

    /**
     * 企业账户类型列表
     */
    @GetMapping(value = "/orgTypeList", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<OrgTypeDto> orgTypeList() {
        return organizationService.getOrgTypeList();
    }

    /**
     * 删除企业账户
     */
    @RequiresPermissions("system:account:delete")
    @PostMapping("/deleteAccount")
    public int deleteAccount(@RequestParam("accountId") String accountId) {
        OrganizationResDTO organizationResDto = organizationService.getOneById(accountId);
        if (null == organizationResDto) {
            return Const.NUMBER_0;
        }
        int result = organizationService.deleteAccount(accountId);
        return result;
    }

    /**
     * 启停用
     */
    @RequiresPermissions("system:account:edit")
    @PostMapping("/updateStatus")
    public IdmResDTO updateStatus(@RequestBody @Valid UpdateStatusParam updateStatusParam) {
        String sessionUserId = LoginInfoHolder.getCurrentUserId().toString();
        String sessionOrgId = LoginInfoHolder.getCurrentOrgId().toString();
        organizationService.updateStatus(updateStatusParam, sessionUserId, sessionOrgId);
        return IdmResDTO.success();
    }

    /**
     * 进入企业
     */
    @RequiresPermissions("system:account:simulateLogin")
    @PostMapping("/simulateLogin")
    public LoginResDTO simulateLogin(@RequestBody @Valid IdParam idParam) {
        String sessionUserId = LoginInfoHolder.getCurrentUserId().toString();
        String sessionOrgId = LoginInfoHolder.getCurrentOrgId().toString();
        GetOrganizationVO organizationVO = organizationService
                .findOne(idParam.getId().toString(), sessionUserId, sessionOrgId);
        LoginResDTO loginResDTO = loginService
                .simulateLogin(organizationVO.getUsername(), organizationVO.getPhoneNumber(),
                        request);
        return loginResDTO;
    }


    /**
     * 查询企业变更记录
     */
    @PostMapping("/organizationChangeList")
    public List<OrganizationChangeDto> organizationChangeList(@RequestBody @Valid IdParam idParam) {
        String sessionUserId = LoginInfoHolder.getCurrentUserId().toString();
        String sessionOrgId = LoginInfoHolder.getCurrentOrgId().toString();
        List<OrganizationChangeDto> list = organizationService
                .selectOrganizationChangeByOrgId(idParam.getId().toString(), sessionUserId,
                        sessionOrgId);
        return list;
    }

    /**
     * 获得变更详情内容
     */
    @PostMapping("/queryOrganizationChangeDetail")
    public OrganizationChangeDto queryOrganizationChangeDetail(
            @RequestBody @Valid IdParam idParam) {
        String sessionUserId = LoginInfoHolder.getCurrentUserId().toString();
        String sessionOrgId = LoginInfoHolder.getCurrentOrgId().toString();
        OrganizationChangeDto dto = organizationService
                .getOrganizationChangeById(idParam.getId(), sessionUserId, sessionOrgId);
        return dto;
    }

}
