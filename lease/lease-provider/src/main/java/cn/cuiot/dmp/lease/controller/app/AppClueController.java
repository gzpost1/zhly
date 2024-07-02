package cn.cuiot.dmp.lease.controller.app;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.lease.dto.clue.*;
import cn.cuiot.dmp.lease.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 小程序端-线索管理
 *
 * @author caorui
 * @date 2024/6/24
 */
@RestController
@RequestMapping("/app/clue")
public class AppClueController {

    @Autowired
    private ClueService clueService;

    /**
     * 保存
     */
    @RequiresPermissions
    @LogRecord(operationCode = "saveClue", operationName = "保存线索", serviceType = ServiceTypeConst.CLUE_MANAGEMENT)
    @PostMapping("/save")
    public boolean saveClue(@RequestBody @Valid ClueCreateDTO createDTO) {
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        Long departmentId = LoginInfoHolder.getCurrentDeptId();
        createDTO.setCompanyId(companyId);
        createDTO.setDepartmentId(departmentId);
        return clueService.saveClue(createDTO);
    }

    /**
     * 更新
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateClue", operationName = "更新线索", serviceType = ServiceTypeConst.CLUE_MANAGEMENT)
    @PostMapping("/update")
    public boolean updateClue(@RequestBody @Valid ClueUpdateDTO updateDTO) {
        Long departmentId = LoginInfoHolder.getCurrentDeptId();
        updateDTO.setDepartmentId(departmentId);
        return clueService.updateClue(updateDTO);
    }

    /**
     * 分配
     */
    @RequiresPermissions
    @LogRecord(operationCode = "distributeClue", operationName = "分配线索", serviceType = ServiceTypeConst.CLUE_MANAGEMENT)
    @PostMapping("/distribute")
    public boolean distributeClue(@RequestBody @Valid ClueDistributeDTO distributeDTO) {
        return clueService.distributeClue(distributeDTO);
    }

    /**
     * 跟进
     */
    @RequiresPermissions
    @LogRecord(operationCode = "followClue", operationName = "跟进线索", serviceType = ServiceTypeConst.CLUE_MANAGEMENT)
    @PostMapping("/follow")
    public boolean followClue(@RequestBody @Valid ClueFollowDTO followDTO) {
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        Long userId = LoginInfoHolder.getCurrentUserId();
        followDTO.setFollowerId(userId);
        followDTO.setCompanyId(companyId);
        return clueService.followClue(followDTO);
    }

    /**
     * 完成
     */
    @RequiresPermissions
    @LogRecord(operationCode = "finishClue", operationName = "完成线索", serviceType = ServiceTypeConst.CLUE_MANAGEMENT)
    @PostMapping("/finish")
    public boolean finishClue(@RequestBody @Valid ClueFinishDTO finishDTO) {
        Long userId = LoginInfoHolder.getCurrentUserId();
        finishDTO.setFinishUserId(userId);
        return clueService.finishClue(finishDTO);
    }

    /**
     * 删除
     */
    @RequiresPermissions
    @LogRecord(operationCode = "deleteClue", operationName = "删除线索", serviceType = ServiceTypeConst.CLUE_MANAGEMENT)
    @PostMapping("/delete")
    public boolean deleteClue(@RequestBody @Valid IdParam idParam) {
        return clueService.deleteClue(idParam.getId());
    }

    /**
     * 批量分配
     */
    @RequiresPermissions
    @LogRecord(operationCode = "batchDistributeClue", operationName = "批量分配线索", serviceType = ServiceTypeConst.CLUE_MANAGEMENT)
    @PostMapping("/batchDistribute")
    public boolean batchDistributeClue(@RequestBody @Valid ClueBatchUpdateDTO batchUpdateDTO) {
        Long userId = LoginInfoHolder.getCurrentUserId();
        batchUpdateDTO.setCurrentFollowerId(userId);
        return clueService.batchDistributeClue(batchUpdateDTO);
    }

    /**
     * 批量完成
     */
    @RequiresPermissions
    @LogRecord(operationCode = "batchFinishClue", operationName = "批量完成线索", serviceType = ServiceTypeConst.CLUE_MANAGEMENT)
    @PostMapping("/batchFinish")
    public boolean batchFinishClue(@RequestBody @Valid ClueBatchUpdateDTO batchUpdateDTO) {
        Long userId = LoginInfoHolder.getCurrentUserId();
        batchUpdateDTO.setFinishUserId(userId);
        return clueService.batchFinishClue(batchUpdateDTO);
    }

    /**
     * 批量删除
     */
    @RequiresPermissions
    @LogRecord(operationCode = "batchDeleteClue", operationName = "批量删除线索", serviceType = ServiceTypeConst.CLUE_MANAGEMENT)
    @PostMapping("/batchDelete")
    public boolean batchDeleteClue(@RequestBody @Valid ClueBatchUpdateDTO batchUpdateDTO) {
        return clueService.batchDeleteClue(batchUpdateDTO.getIdList());
    }

}
