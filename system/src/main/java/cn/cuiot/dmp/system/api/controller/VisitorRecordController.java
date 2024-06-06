package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.system.application.param.dto.VisitorRecordCreateDTO;
import cn.cuiot.dmp.system.application.param.dto.VisitorRecordDTO;
import cn.cuiot.dmp.system.application.param.dto.VisitorRecordPageQueryDTO;
import cn.cuiot.dmp.system.application.param.dto.VisitorRecordUpdateDTO;
import cn.cuiot.dmp.system.application.service.VisitorRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 客户端小程序-访客记录
 *
 * @author caorui
 * @date 2024/6/6
 */
@RestController
@RequestMapping("/visitorRecord")
public class VisitorRecordController {

    @Autowired
    private VisitorRecordService visitorRecordService;

    /**
     * 根据id获取详情
     */
    @PostMapping("/queryForDetail")
    public VisitorRecordDTO queryForDetail(@RequestBody @Valid IdParam idParam) {
        return visitorRecordService.queryForDetail(idParam.getId());
    }

    /**
     * 查询列表
     */
    @PostMapping("/queryForList")
    public List<VisitorRecordDTO> queryForList(@RequestBody @Valid VisitorRecordPageQueryDTO queryDTO) {
        return visitorRecordService.queryForList(queryDTO);
    }

    /**
     * 查询分页列表
     */
    @PostMapping("/queryForPage")
    public PageResult<VisitorRecordDTO> queryForPage(@RequestBody @Valid VisitorRecordPageQueryDTO queryDTO) {
        return visitorRecordService.queryForPage(queryDTO);
    }

    /**
     * 保存
     */
    @RequiresPermissions
    @LogRecord(operationCode = "saveVisitorRecord", operationName = "保存访客记录", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/save")
    public boolean saveVisitorRecord(@RequestBody @Valid VisitorRecordCreateDTO createDTO) {
        return visitorRecordService.saveVisitorRecord(createDTO);
    }

    /**
     * 更新
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateVisitorRecord", operationName = "更新访客记录", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/update")
    public boolean updateVisitorRecord(@RequestBody @Valid VisitorRecordUpdateDTO updateDTO) {
        return visitorRecordService.updateVisitorRecord(updateDTO);
    }

    /**
     * 删除
     */
    @RequiresPermissions
    @LogRecord(operationCode = "deleteVisitorRecord", operationName = "删除访客记录", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/delete")
    public boolean deleteVisitorRecord(@RequestBody @Valid IdParam idParam) {
        return visitorRecordService.deleteVisitorRecord(idParam.getId());
    }

}
