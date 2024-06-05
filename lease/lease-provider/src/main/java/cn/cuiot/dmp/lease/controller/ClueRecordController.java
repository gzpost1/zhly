package cn.cuiot.dmp.lease.controller;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.lease.dto.clue.*;
import cn.cuiot.dmp.lease.service.ClueRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 租赁管理-线索管理-线索记录
 *
 * @author caorui
 * @date 2024/6/4
 */
@RestController
@RequestMapping("/clueRecord")
public class ClueRecordController {

    @Autowired
    private ClueRecordService clueRecordService;

    /**
     * 根据id获取详情
     */
    @PostMapping("/queryForDetail")
    public ClueRecordDTO queryForDetail(@RequestBody @Valid IdParam idParam) {
        return clueRecordService.queryForDetail(idParam.getId());
    }

    /**
     * 查询列表
     */
    @PostMapping("/queryForList")
    public List<ClueRecordDTO> queryForList(@RequestBody @Valid ClueRecordPageQueryDTO queryDTO) {
        return clueRecordService.queryForList(queryDTO);
    }

    /**
     * 查询分页列表
     */
    @PostMapping("/queryForPage")
    public PageResult<ClueRecordDTO> queryForPage(@RequestBody @Valid ClueRecordPageQueryDTO queryDTO) {
        return clueRecordService.queryForPage(queryDTO);
    }

    /**
     * 更新
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateClueRecord", operationName = "更新线索记录", serviceType = ServiceTypeConst.CLUE_MANAGEMENT)
    @PostMapping("/update")
    public boolean updateClueRecord(@RequestBody @Valid ClueRecordUpdateDTO updateDTO) {
        return clueRecordService.updateClueRecord(updateDTO);
    }

    /**
     * 删除
     */
    @RequiresPermissions
    @LogRecord(operationCode = "deleteClueRecord", operationName = "删除线索记录", serviceType = ServiceTypeConst.CLUE_MANAGEMENT)
    @PostMapping("/delete")
    public boolean deleteClueRecord(@RequestBody @Valid IdParam idParam) {
        return clueRecordService.deleteClueRecord(idParam.getId());
    }

}
