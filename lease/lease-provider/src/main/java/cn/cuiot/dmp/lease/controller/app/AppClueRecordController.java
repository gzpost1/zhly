package cn.cuiot.dmp.lease.controller.app;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.lease.dto.clue.ClueRecordUpdateDTO;
import cn.cuiot.dmp.lease.service.ClueRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 小程序端-线索管理-线索记录
 *
 * @author caorui
 * @date 2024/6/24
 */
@RestController
@RequestMapping("/app/clueRecord")
public class AppClueRecordController {

    @Autowired
    private ClueRecordService clueRecordService;

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
