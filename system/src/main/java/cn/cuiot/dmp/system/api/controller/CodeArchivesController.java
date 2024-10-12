package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.system.application.param.dto.CodeArchivesCreateDTO;
import cn.cuiot.dmp.system.application.param.dto.CodeArchivesUpdateDTO;
import cn.cuiot.dmp.system.application.param.vo.CodeArchivesVO;
import cn.cuiot.dmp.system.application.service.CodeArchivesService;
import cn.cuiot.dmp.system.domain.aggregate.CodeArchivesPageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 系统配置-二维码档案
 *
 * @author caorui
 * @date 2024/5/22
 */
@RestController
@RequestMapping("/codeArchives")
public class CodeArchivesController extends BaseController {

    @Autowired
    private CodeArchivesService codeArchivesService;

    /**
     * 根据id获取详情
     */
    @PostMapping("/queryForDetail")
    public CodeArchivesVO queryForDetail(@RequestBody @Valid IdParam idParam) {
        return codeArchivesService.queryForDetail(idParam.getId());
    }

    /**
     * 根据条件获取二维码档案分页
     */
    @PostMapping("/queryForPage")
    public PageResult<CodeArchivesVO> queryForPage(@RequestBody @Valid CodeArchivesPageQuery pageQuery) {
        return codeArchivesService.queryForPage(pageQuery);
    }

    /**
     * 保存
     */
    @RequiresPermissions
    @LogRecord(operationCode = "saveCodeArchives", operationName = "保存二维码档案", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/save")
    public int saveCodeArchives(@RequestBody @Valid CodeArchivesCreateDTO createDTO) {
        String orgId = getOrgId();
        String userId = getUserId();
        createDTO.setCompanyId(Long.valueOf(orgId));
        createDTO.setUserId(Long.valueOf(userId));
        return codeArchivesService.saveCodeArchives(createDTO);
    }

    /**
     * 更新
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateCodeArchives", operationName = "更新二维码档案", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/update")
    public int updateCodeArchives(@RequestBody @Valid CodeArchivesUpdateDTO updateDTO) {
        return codeArchivesService.updateCodeArchives(updateDTO);
    }

    /**
     * 更新状态
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateCodeArchivesStatus", operationName = "更新二维码档案状态", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/updateStatus")
    public int updateCodeArchivesStatus(@RequestBody @Valid UpdateStatusParam updateStatusParam) {
        return codeArchivesService.updateCodeArchivesStatus(updateStatusParam);
    }

    /**
     * 删除
     */
    @RequiresPermissions
    @LogRecord(operationCode = "deleteCodeArchives", operationName = "删除二维码档案", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/delete")
    public int deleteCodeArchives(@RequestBody @Valid IdParam idParam) {
        return codeArchivesService.deleteCodeArchives(idParam.getId());
    }

    /**
     * 手动关联
     */
    @RequiresPermissions
    @LogRecord(operationCode = "associateCodeArchives", operationName = "关联二维码档案", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/associate")
    public int associateCodeArchives(@RequestBody @Valid CodeArchivesUpdateDTO updateDTO) {
        return codeArchivesService.associateCodeArchives(updateDTO);
    }

    /**
     * 导出
     * @param pageQuery
     * @return
     * @throws Exception
     */
    @PostMapping("export")
    public IdmResDTO export(@RequestBody @Valid CodeArchivesPageQuery pageQuery) throws Exception {
        codeArchivesService.export(pageQuery);
        return IdmResDTO.success();
    }
}
