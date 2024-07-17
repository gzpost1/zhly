package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.base.infrastructure.syslog.LogContextHolder;
import cn.cuiot.dmp.base.infrastructure.syslog.OptTargetData;
import cn.cuiot.dmp.base.infrastructure.syslog.OptTargetInfo;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.ResultCode;
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
import cn.cuiot.dmp.system.infrastructure.entity.dto.OrgTypeDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.OrganizationChangeDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.OrganizationResDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UpdateOrganizationDto;
import cn.cuiot.dmp.system.infrastructure.entity.vo.GetOrganizationVO;
import cn.cuiot.dmp.system.infrastructure.entity.vo.ListOrganizationVO;
import cn.hutool.core.util.PhoneUtil;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
    @RequiresPermissions
    @LogRecord(operationCode = "insertOrganization", operationName = "添加企业", serviceType = "organization",serviceTypeName = "企业管理")
    @PostMapping(value = "/insertOrganization", produces = MediaType.APPLICATION_JSON_VALUE)
    public void insertSonOrganization(@RequestBody @Valid InsertOrganizationDto dto) {
        if (!getOrgId().equals(SUP_ORGID)) {
            throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
        }
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
    @RequiresPermissions
    @LogRecord(operationCode = "updateOrganization", operationName = "编辑企业", serviceType = "organization",serviceTypeName = "企业管理")
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
     * 企业账户列表
     */
    @GetMapping(value = "/listOrganization", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResult<ListOrganizationVO> listOrganization(ListOrganizationDto dto) {
        if (dto.getPageSize() > 100) {
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT);
        }
        ValidateUtil.validate(dto);
        dto.setLoginOrgId(LoginInfoHolder.getCurrentOrgId());
        dto.setLoginUserId(LoginInfoHolder.getCurrentUserId());
        return organizationService.commercialOrgList(dto);
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
    @RequiresPermissions
    @LogRecord(operationCode = "deleteOrganizationAccount", operationName = "删除企业", serviceType = "organization",serviceTypeName = "企业管理")
    @PostMapping("/deleteAccount")
    public int deleteAccount(@RequestParam("accountId") String accountId) {
        OrganizationResDTO organizationResDto = organizationService.getOneById(accountId);
        if (null == organizationResDto) {
            throw new BusinessException(ResultCode.OBJECT_NOT_EXIST,"企业不存在");
        }
        if (!getOrgId().equals(SUP_ORGID)) {
            throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
        }
        //设置日志操作对象内容
        LogContextHolder.setOptTargetInfo(OptTargetInfo.builder()
                .name("企业")
                .targetDatas(Lists.newArrayList(new OptTargetData(organizationResDto.getOrgName(),organizationResDto.getId().toString())))
                .build());

        LoginInfoHolder.markDeleteOperation();
        int result = organizationService.deleteAccount(accountId);
        return result;
    }

    /**
     * 启停用
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateOrganizationStatus", operationName = "启停用企业", serviceType = "organization",serviceTypeName = "企业管理")
    @PostMapping("/updateStatus")
    public IdmResDTO updateStatus(@RequestBody @Valid UpdateStatusParam updateStatusParam) {
        String sessionUserId = LoginInfoHolder.getCurrentUserId().toString();
        String sessionOrgId = LoginInfoHolder.getCurrentOrgId().toString();
        if (!getOrgId().equals(SUP_ORGID)) {
            throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
        }
        organizationService.updateStatus(updateStatusParam, sessionUserId, sessionOrgId);
        return IdmResDTO.success();
    }

    /**
     * 进入企业
     */
    @RequiresPermissions
    @LogRecord(operationCode = "simulateLogin", operationName = "进入企业", serviceType = "organization",serviceTypeName = "企业管理")
    @PostMapping("/simulateLogin")
    public LoginResDTO simulateLogin(@RequestBody @Valid IdParam idParam) {
        String sessionUserId = LoginInfoHolder.getCurrentUserId().toString();
        String sessionOrgId = LoginInfoHolder.getCurrentOrgId().toString();

        if (!getOrgId().equals(SUP_ORGID)) {
            throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
        }

        GetOrganizationVO organizationVO = organizationService
                .findOne(idParam.getId().toString(), sessionUserId, sessionOrgId);

        //设置日志操作对象内容
        LogContextHolder.setOptTargetInfo(OptTargetInfo.builder()
                .name("企业")
                .targetDatas(Lists.newArrayList(new OptTargetData(organizationVO.getCompanyName(),organizationVO.getId().toString())))
                .build());

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

    /**
     * 初始化企业
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateInitFlag", operationName = "初始化企业", serviceType = "organization", serviceTypeName = "企业管理")
    @PostMapping("/updateInitFlag")
    public int updateInitFlag(@RequestBody @Valid IdParam idParam) {
        OrganizationResDTO organizationResDto = organizationService.getOneById(idParam.getId().toString());
        if (Objects.isNull(organizationResDto)) {
            throw new BusinessException(ResultCode.OBJECT_NOT_EXIST,"企业不存在");
        }
        if (!getOrgId().equals(SUP_ORGID)) {
            throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
        }
        //设置日志操作对象内容
        LogContextHolder.setOptTargetInfo(OptTargetInfo.builder()
                .name("企业")
                .targetDatas(Lists.newArrayList(new OptTargetData(organizationResDto.getOrgName(),organizationResDto.getId().toString())))
                .build());
        return organizationService.updateInitFlag(idParam.getId());
    }

}
