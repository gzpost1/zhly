package cn.cuiot.dmp.app.controller;

import cn.cuiot.dmp.app.dto.UserFeedbackQuery;
import cn.cuiot.dmp.app.dto.UserFeedbackReplyDto;
import cn.cuiot.dmp.app.entity.UserFeedbackEntity;
import cn.cuiot.dmp.app.service.UserFeedbackService;
import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.service.ApiSystemService;
import cn.cuiot.dmp.base.infrastructure.dto.BaseUserDto;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.req.BaseUserReqDto;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.Date;
import java.util.Objects;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 【PC】意见反馈
 *
 * @author: wuyongchong
 * @date: 2024/6/14 10:53
 */
@Slf4j
@RestController
@RequestMapping("/user-feedback")
public class UserFeedbackController {

    @Autowired
    private UserFeedbackService userFeedbackService;

    @Autowired
    private ApiSystemService apiSystemService;

    /**
     * 分页查询
     */
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<UserFeedbackEntity>> queryForPage(@RequestBody UserFeedbackQuery query) {
        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        query.setCompanyId(currentOrgId);
        Long deptId = query.getDeptId();
        if (Objects.isNull(deptId) && CollectionUtils.isEmpty(query.getDeptIds())) {
            deptId = LoginInfoHolder.getCurrentDeptId();
        }
        if (Objects.nonNull(deptId)) {
            DepartmentDto departmentDto = apiSystemService.lookUpDepartmentInfo(deptId, null, null);
            if (departmentDto != null) {
                query.setDeptPath(departmentDto.getPath());
            }
        }
        query.setDeptId(null);
        IPage<UserFeedbackEntity> pageData = userFeedbackService.queryForPage(query);
        return IdmResDTO.success(pageData);
    }

    /**
     * 获得详情
     */
    @PostMapping("/queryForDetail")
    public IdmResDTO<UserFeedbackEntity> queryForDetail(@RequestBody @Valid IdParam idParam) {
        UserFeedbackEntity data = userFeedbackService.queryForDetail(idParam.getId());
        return IdmResDTO.success(data);
    }

    /**
     * 回复
     */
    @RequiresPermissions
    @LogRecord(operationCode = "replyUserFeedback", operationName = "意见反馈回复", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/reply")
    public IdmResDTO replyUserFeedback(@RequestBody @Valid UserFeedbackReplyDto dto) {
        BaseUserReqDto userReqDto = new BaseUserReqDto();
        userReqDto.setUserId(LoginInfoHolder.getCurrentUserId());
        BaseUserDto userInfo = apiSystemService.lookUpUserInfo(userReqDto);
        if (Objects.isNull(userInfo)) {
            throw new BusinessException(ResultCode.PARAM_CANNOT_NULL, "获取登录用户信息失败");
        }
        userFeedbackService
                .replyUserFeedback(dto.getId(), userInfo.getId(), userInfo.getName(), new Date(),
                        dto.getReplyContent());
        return IdmResDTO.success();
    }

}
