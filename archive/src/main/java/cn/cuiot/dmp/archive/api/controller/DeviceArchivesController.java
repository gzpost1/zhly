package cn.cuiot.dmp.archive.api.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.cuiot.dmp.archive.application.param.dto.ArchiveBatchUpdateDTO;
import cn.cuiot.dmp.archive.application.param.dto.DeviceArchivesImportDto;
import cn.cuiot.dmp.archive.application.param.dto.RoomArchivesImportDto;
import cn.cuiot.dmp.archive.application.param.query.DeviceArchivesQuery;
import cn.cuiot.dmp.archive.application.param.vo.DeviceArchivesExportVo;
import cn.cuiot.dmp.archive.application.param.vo.RoomArchivesExportVo;
import cn.cuiot.dmp.archive.application.service.DeviceArchivesService;
import cn.cuiot.dmp.archive.infrastructure.entity.DeviceArchivesEntity;
import cn.cuiot.dmp.archive.utils.ExcelUtils;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.IdsParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

/**
 * @author liujianyu
 * @description 设备档案
 * @since 2024-05-15 10:29
 */
@RestController
@RequestMapping("/device")
public class DeviceArchivesController {

    @Resource
    protected HttpServletRequest request;

    @Resource
    protected HttpServletResponse response;

    @Autowired
    private DeviceArchivesService deviceArchivesService;

    /**
     * 分页列表
     */
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<DeviceArchivesEntity>> queryForPage(@RequestBody @Valid DeviceArchivesQuery query) {
        LambdaQueryWrapper<DeviceArchivesEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DeviceArchivesEntity::getLoupanId, query.getLoupanId());
        wrapper.like(StringUtils.isNotBlank(query.getDeviceName()), DeviceArchivesEntity::getDeviceName, query.getDeviceName());
        wrapper.like(StringUtils.isNotBlank(query.getInstallationLocation()), DeviceArchivesEntity::getInstallationLocation, query.getInstallationLocation());
        wrapper.eq(Objects.nonNull(query.getDeviceStatus()), DeviceArchivesEntity::getDeviceStatus, query.getDeviceStatus());
        wrapper.eq(Objects.nonNull(query.getDeviceCategory()), DeviceArchivesEntity::getDeviceCategory, query.getDeviceCategory());
        IPage<DeviceArchivesEntity> res = deviceArchivesService.page(new Page<>(query.getPageNo(), query.getPageSize()), wrapper);
        return IdmResDTO.success(res);
    }

    /**
     * 创建
     */
    @RequiresPermissions
    @PostMapping("/create")
    public IdmResDTO create(@RequestBody DeviceArchivesEntity entity) {
        // 校验参数合法性，写在service层，用于导入的时候使用
        deviceArchivesService.checkParams(entity);
        // 保存数据
        deviceArchivesService.save(entity);
        return IdmResDTO.success(null);
    }

    /**
     * 修改
     */
    @RequiresPermissions
    @PostMapping("/update")
    public IdmResDTO update(@RequestBody DeviceArchivesEntity entity) {
        deviceArchivesService.updateById(entity);
        return IdmResDTO.success(null);
    }

    /**
     * 删除
     */
    @RequiresPermissions
    @PostMapping("/delete")
    public IdmResDTO delete(@RequestBody @Valid IdParam idParam) {
        deviceArchivesService.removeById(idParam.getId());
        return IdmResDTO.success();
    }

    /**
     * 批量修改
     */
    @RequiresPermissions
    @PostMapping("/updateByIds")
    public IdmResDTO updateByIds(@RequestBody @Valid ArchiveBatchUpdateDTO param) {
        LambdaQueryWrapper<DeviceArchivesEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(DeviceArchivesEntity::getId, param.getIds());

        DeviceArchivesEntity entity = new DeviceArchivesEntity();
        entity.setLoupanId(param.getLoupanId());
        deviceArchivesService.update(entity, wrapper);
        return IdmResDTO.success();
    }

    /**
     * 导出,按照id列表
     */
    @RequiresPermissions
    @PostMapping(value = "/export", produces = MediaType.APPLICATION_JSON_VALUE)
    public void export(@RequestBody IdsParam param) throws IOException {
        List<DeviceArchivesExportVo> dataList = deviceArchivesService.buildExportData(param);
        List<Map<String, Object>> sheetsList = new ArrayList<>();
        Map<String, Object> sheet1 = ExcelUtils
                .createSheet("空间档案", dataList, DeviceArchivesExportVo.class);

        sheetsList.add(sheet1);

        Workbook workbook = ExcelExportUtil.exportExcel(sheetsList, ExcelType.XSSF);

        ExcelUtils.downLoadExcel(
                "room-" + DateTimeUtil.dateToString(new Date(), "yyyyMMddHHmmss"),
                response,
                workbook);
    }

    /**
     * 导入
     */
    @RequiresPermissions
    @PostMapping(value = "/import", produces = MediaType.APPLICATION_JSON_VALUE)
    public void importData(@RequestParam("file") MultipartFile file) throws Exception {

        AssertUtil.isFalse((null == file || file.isEmpty()), "上传文件为空");

        ImportParams params = new ImportParams();
        params.setHeadRows(1);

        List<DeviceArchivesImportDto> importDtoList = ExcelImportUtil
                .importExcel(file.getInputStream(), DeviceArchivesImportDto.class, params);

        if(CollectionUtils.isEmpty(importDtoList)){
            throw new BusinessException(ResultCode.REQUEST_FORMAT_ERROR, "excel解析失败");
        }
        for (DeviceArchivesImportDto dto : importDtoList){
            deviceArchivesService.checkParamsImport(dto);
        }

        deviceArchivesService.importDataSave(importDtoList);
    }
}
