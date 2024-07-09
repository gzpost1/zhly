package cn.cuiot.dmp.app.controller.app;

import cn.cuiot.dmp.app.dto.AppUserDto;
import cn.cuiot.dmp.app.dto.UserFeedbackDto;
import cn.cuiot.dmp.app.dto.UserFeedbackQuery;
import cn.cuiot.dmp.app.entity.UserFeedbackEntity;
import cn.cuiot.dmp.app.service.AppUserService;
import cn.cuiot.dmp.app.service.UserFeedbackService;
import cn.cuiot.dmp.base.application.service.ApiArchiveService;
import cn.cuiot.dmp.base.application.service.ApiSystemService;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.Sm4;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.Objects;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 【APP】意见反馈
 *
 * @author: wuyongchong
 * @date: 2024/6/14 10:53
 */
@Slf4j
@RestController
@RequestMapping("/app/user-feedback")
public class AppUserFeedbackController {

    @Autowired
    private UserFeedbackService userFeedbackService;

    @Autowired
    private ApiArchiveService apiArchiveService;

    @Autowired
    private ApiSystemService apiSystemService;

    @Autowired
    private AppUserService appUserService;

    /**
     * 创建
     */
    @PostMapping("/create")
    public IdmResDTO create(@RequestBody @Valid UserFeedbackDto dto) {
        Long currentUserId = LoginInfoHolder.getCurrentUserId();
        String currentName = LoginInfoHolder.getCurrentName();
        String currentPhoneNumber = LoginInfoHolder.getCurrentPhoneNumber();
        Long buildingId = dto.getBuildingId();
        BuildingArchive buildingArchive = apiArchiveService.lookupBuildingArchiveInfo(buildingId);
        if (Objects.isNull(buildingArchive)) {
            throw new BusinessException(ResultCode.OBJECT_NOT_EXIST, "所选楼盘不存在");
        }
        Long companyId = buildingArchive.getCompanyId();
        Long departmentId = buildingArchive.getDepartmentId();
        DepartmentDto departmentDto = apiSystemService
                .lookUpDepartmentInfo(departmentId, null, null);
        if (Objects.isNull(departmentDto)) {
            throw new BusinessException(ResultCode.OBJECT_NOT_EXIST, "所选楼盘所属组织不存在");
        }

        UserFeedbackEntity entity = new UserFeedbackEntity();
        entity.setUserId(currentUserId);
        entity.setName(currentName);
        if (StringUtils.isNotBlank(currentPhoneNumber)) {
            entity.setPhone(Sm4.encryption(currentPhoneNumber));
        }
        entity.setCompanyId(companyId);
        entity.setDeptId(departmentId);
        entity.setDeptName(departmentDto.getName());
        entity.setBuildingId(buildingId);
        entity.setBuildingName(buildingArchive.getName());
        entity.setFeedbackContent(dto.getFeedbackContent());
        entity.setImages(dto.getImages());
        entity.setStatus(EntityConstants.NO);

        userFeedbackService.createUserFeedback(entity);

        return IdmResDTO.success();
    }

    /**
     * 分页查询
     */
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<UserFeedbackEntity>> queryForPage(@RequestBody UserFeedbackQuery query) {
        if (Objects.isNull(query.getBuildingId())) {
            throw new BusinessException(ResultCode.PARAM_CANNOT_NULL, "楼盘ID不能为空");
        }
        Long currentUserId = LoginInfoHolder.getCurrentUserId();
        query.setUserId(currentUserId);
        IPage<UserFeedbackEntity> pageData = userFeedbackService.queryForPage(query);
        return IdmResDTO.success(pageData);
    }

    /**
     * 获得详情
     */
    @PostMapping("/queryForDetail")
    public IdmResDTO<UserFeedbackEntity> queryForDetail(@RequestBody @Valid IdParam idParam) {
        UserFeedbackEntity data = userFeedbackService.queryForDetail(idParam.getId());
        if(Objects.nonNull(data)){
            AppUserDto user = appUserService.getUserById(data.getUserId());
            if(Objects.nonNull(user)){
                data.setAvatar(user.getAvatar());
            }
        }
        return IdmResDTO.success(data);
    }

}
