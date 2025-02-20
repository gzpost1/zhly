package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.system.application.param.dto.UserHouseAuditDTO;
import cn.cuiot.dmp.system.application.param.dto.UserHouseAuditPageQueryDTO;
import cn.cuiot.dmp.system.application.param.dto.UserHouseAuditStatusDTO;
import cn.cuiot.dmp.system.application.param.dto.UserHouseBuildingDTO;
import cn.cuiot.dmp.system.application.service.UserHouseAuditService;
import cn.cuiot.dmp.system.domain.aggregate.CodeArchivesPageQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理后台-系统配置-C端用户审核列表
 *
 * @author caorui
 * @date 2024/6/13
 */
@RestController
@RequestMapping("/userHouseAudit")
public class UserHouseAuditController {

    @Autowired
    private UserHouseAuditService userHouseAuditService;

    /**
     * 根据id获取详情
     */
    @RequiresPermissions
    @PostMapping("/queryForDetail")
    public UserHouseAuditDTO queryForDetail(@RequestBody @Valid IdParam idParam) {
        return userHouseAuditService.queryForDetail(idParam.getId());
    }

    /**
     * 查询列表
     */
    @RequiresPermissions
    @PostMapping("/queryForList")
    public List<UserHouseAuditDTO> queryForList(
            @RequestBody @Valid UserHouseAuditPageQueryDTO queryDTO) {
        queryDTO.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        return userHouseAuditService.queryForList(queryDTO);
    }

    /**
     * 查询分页列表
     */
    @RequiresPermissions
    @PostMapping("/queryForPage")
    public IPage<UserHouseAuditDTO> queryForPage(
            @RequestBody @Valid UserHouseAuditPageQueryDTO queryDTO) {
        queryDTO.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        return userHouseAuditService.queryForPage(queryDTO);
    }

    /**
     * 根据用户id查询楼盘列表
     */
    @PostMapping("/queryBuildingsByUser")
    public List<UserHouseBuildingDTO> queryBuildingsByUser(
            @RequestBody @Valid UserHouseAuditPageQueryDTO queryDTO) {
        Long userId = LoginInfoHolder.getCurrentUserId();
        return userHouseAuditService.queryBuildingsByUser(userId);
    }

    /**
     * 审核
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateAuditStatus", operationName = "C端用户审核", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/audit")
    public boolean updateAuditStatus(@RequestBody @Valid UserHouseAuditStatusDTO statusDTO) {
        return userHouseAuditService.updateAuditStatus(statusDTO);
    }

    /**
     * 取消身份
     */
    @RequiresPermissions
    @LogRecord(operationCode = "cancelAuditStatus", operationName = "取消身份", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/cancelAuditStatus")
    public boolean cancelAuditStatus(@RequestBody @Valid IdParam idParam) {
        return userHouseAuditService.cancelAuditStatus(idParam);
    }

    /**
     * 导出
     * @param pageQuery
     * @return
     * @throws Exception
     */
    @PostMapping("export")
    public IdmResDTO export(@RequestBody @Valid UserHouseAuditPageQueryDTO pageQuery) throws Exception {
        pageQuery.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        userHouseAuditService.export(pageQuery);
        return IdmResDTO.success();
    }

}
