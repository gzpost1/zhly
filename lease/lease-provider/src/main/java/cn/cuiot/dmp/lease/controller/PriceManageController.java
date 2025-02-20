package cn.cuiot.dmp.lease.controller;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.lease.dto.price.*;
import cn.cuiot.dmp.lease.entity.PriceManageRecordEntity;
import cn.cuiot.dmp.lease.service.PriceManageService;
import cn.cuiot.dmp.system.domain.aggregate.CodeArchivesPageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 租赁管理-定价管理
 *
 * @author caorui
 * @date 2024/7/1
 */
@RestController
@RequestMapping("/priceManage")
public class PriceManageController {

    @Autowired
    private PriceManageService priceManageService;

    /**
     * 根据id获取详情
     */
    @RequiresPermissions
    @PostMapping("/queryForDetail")
    public PriceManageDTO queryForDetail(@RequestBody @Valid IdParam idParam) {
        return priceManageService.queryForDetail(idParam.getId());
    }

    /**
     * 查询列表
     */
    @RequiresPermissions
    @PostMapping("/queryForList")
    public List<PriceManageDTO> queryForList(@RequestBody @Valid PriceManagePageQueryDTO queryDTO) {
        return priceManageService.queryForList(queryDTO);
    }

    /**
     * 查询分页列表
     */
    @RequiresPermissions
    @PostMapping("/queryForPage")
    public PageResult<PriceManageDTO> queryForPage(@RequestBody @Valid PriceManagePageQueryDTO queryDTO) {
        return priceManageService.queryForPage(queryDTO);
    }

    /**
     * 保存
     */
    @RequiresPermissions
    @LogRecord(operationCode = "savePriceManage", operationName = "保存定价管理", serviceType = ServiceTypeConst.CLUE_MANAGEMENT)
    @PostMapping("/save")
    public Long savePriceManage(@RequestBody @Valid PriceManageCreateDTO createDTO) {
        return priceManageService.savePriceManage(createDTO);
    }

    /**
     * 更新
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updatePriceManage", operationName = "更新定价管理", serviceType = ServiceTypeConst.CLUE_MANAGEMENT)
    @PostMapping("/update")
    public boolean updatePriceManage(@RequestBody @Valid PriceManageUpdateDTO updateDTO) {
        return priceManageService.updatePriceManage(updateDTO);
    }

    /**
     * 复制新增
     */
    @RequiresPermissions
    @LogRecord(operationCode = "copyPriceManage", operationName = "复制定价管理", serviceType = ServiceTypeConst.CLUE_MANAGEMENT)
    @PostMapping("/copy")
    public boolean copyPriceManage(@RequestBody @Valid IdParam idParam) {
        return priceManageService.copyPriceManage(idParam.getId());
    }

    /**
     * 提交
     */
    @RequiresPermissions
    @LogRecord(operationCode = "submitPriceManage", operationName = "提交定价管理", serviceType = ServiceTypeConst.CLUE_MANAGEMENT)
    @PostMapping("/submit")
    public boolean submitPriceManage(@RequestBody @Valid IdParam idParam) {
        return priceManageService.submitPriceManage(idParam.getId());
    }

    /**
     * 审核
     */
    @RequiresPermissions
    @LogRecord(operationCode = "auditPriceManage", operationName = "审核定价管理", serviceType = ServiceTypeConst.CLUE_MANAGEMENT)
    @PostMapping("/audit")
    public boolean auditPriceManage(@RequestBody @Valid PriceManageAuditDTO auditDTO) {
        return priceManageService.auditPriceManage(auditDTO);
    }

    /**
     * 作废
     */
    @RequiresPermissions
    @LogRecord(operationCode = "invalidPriceManage", operationName = "作废定价管理", serviceType = ServiceTypeConst.CLUE_MANAGEMENT)
    @PostMapping("/invalid")
    public boolean invalidPriceManage(@RequestBody @Valid PriceManageAuditDTO auditDTO) {
        return priceManageService.invalidPriceManage(auditDTO);
    }

    /**
     * 删除
     */
    @RequiresPermissions
    @LogRecord(operationCode = "deletePriceManage", operationName = "删除定价管理", serviceType = ServiceTypeConst.CLUE_MANAGEMENT)
    @PostMapping("/delete")
    public boolean deletePriceManage(@RequestBody @Valid IdParam idParam) {
        return priceManageService.deletePriceManage(idParam.getId());
    }

    /**
     * 通过定价管理id查询定价管理记录
     */
    @RequiresPermissions
    @PostMapping("/queryRecordByPriceId")
    public PageResult<PriceManageRecordEntity> queryRecordByPriceId(@RequestBody @Valid PriceManageRecordPageQueryDTO pageQueryDTO) {
        return priceManageService.queryRecordByPriceId(pageQueryDTO);
    }

    /**
     * 通过定价管理状态查询统计数量
     */
    @RequiresPermissions
    @PostMapping("/queryCountByStatus")
    public List<PriceManageCountDTO> queryCountByStatus() {
        return priceManageService.queryCountByStatus();
    }

    /**
     * 导出
     * @param pageQuery
     * @return
     * @throws Exception
     */
    @PostMapping("export")
    public IdmResDTO export(@RequestBody @Valid PriceManagePageQueryDTO pageQuery) throws Exception {
        priceManageService.export(pageQuery);
        return IdmResDTO.success();
    }
}
