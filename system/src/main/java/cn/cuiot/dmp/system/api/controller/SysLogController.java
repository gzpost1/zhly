package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.dto.ExcelDownloadDto;
import cn.cuiot.dmp.base.application.dto.ExcelReportDto;
import cn.cuiot.dmp.base.application.service.ExcelExportService;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.NumberConst;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.system.application.service.SysLogService;
import cn.cuiot.dmp.system.application.param.dto.SysLogQuery;
import cn.cuiot.dmp.system.infrastructure.entity.OperateLogEntity;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 【PC】系统日志
 *
 * @author: wuyongchong
 * @date: 2024/6/18 10:15
 */
@Slf4j
@RestController
@RequestMapping("/sys/log")
public class SysLogController {

    @Autowired
    private SysLogService sysLogService;
    @Autowired
    private ExcelExportService excelExportService;

    /**
     * 分页查询
     */
    @RequiresPermissions
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<OperateLogEntity>> queryForPage(@RequestBody SysLogQuery query) {
        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        query.setOrgId(currentOrgId);
        IPage<OperateLogEntity> pageData = sysLogService.queryForPage(query);
        return IdmResDTO.success(pageData);
    }

    /**
     * 系统日志导出
     * @param dto
     * @throws Exception
     */
    @RequiresPermissions
    @PostMapping("/export")
    public void export(@RequestBody SysLogQuery dto) throws Exception {
        excelExportService.excelExport(ExcelDownloadDto.<SysLogQuery>builder().loginInfo(LoginInfoHolder.getCurrentLoginInfo()).query(dto)
                .title("系统日志导出").fileName("系统日志导出(" + DateTimeUtil.dateToString(new Date(), "yyyyMMdd")+")").sheetName("系统日志导出")
                .build(), OperateLogEntity.class, this::queryExport);
    }

    /**
     * 查询导出列表
     * @param downloadDto
     * @return
     */
    public IPage<OperateLogEntity> queryExport(ExcelDownloadDto<SysLogQuery> downloadDto){
        SysLogQuery pageQuery = downloadDto.getQuery();
        IPage<OperateLogEntity> data = this.queryForPage(pageQuery).getData();
        List<OperateLogEntity> operateLogEntities = Optional.ofNullable(data.getRecords()).orElse(new ArrayList<>());
        operateLogEntities.stream().forEach(item->{
            item.setUserTypeName(String.valueOf(item.getUserType()));
            item.setOperation(item.getOperationByName()+"("+item.getOperationById()+")");
        });
        return data;
    }

}
