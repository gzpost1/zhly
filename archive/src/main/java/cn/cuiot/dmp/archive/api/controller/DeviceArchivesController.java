package cn.cuiot.dmp.archive.api.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.cuiot.dmp.archive.application.constant.ArchivesApiConstant;
import cn.cuiot.dmp.archive.application.param.dto.ArchiveBatchUpdateDTO;
import cn.cuiot.dmp.archive.application.param.dto.DeviceArchivesImportDto;
import cn.cuiot.dmp.archive.application.param.query.DeviceArchivesQuery;
import cn.cuiot.dmp.archive.application.param.vo.BuildingArchivesVO;
import cn.cuiot.dmp.archive.application.param.vo.DeviceArchivesExportVo;
import cn.cuiot.dmp.archive.application.service.BuildingArchivesService;
import cn.cuiot.dmp.archive.application.service.DeviceArchivesService;
import cn.cuiot.dmp.archive.application.service.impl.BuildingAndConfigCommonUtilService;
import cn.cuiot.dmp.archive.application.service.impl.DeviceArchivesServiceImpl;
import cn.cuiot.dmp.archive.infrastructure.entity.DeviceArchivesEntity;
import cn.cuiot.dmp.archive.infrastructure.entity.RoomArchivesEntity;
import cn.cuiot.dmp.archive.infrastructure.persistence.mapper.ArchivesApiMapper;
import cn.cuiot.dmp.archive.utils.ExcelUtils;
import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.IdsParam;
import cn.cuiot.dmp.base.infrastructure.dto.req.DepartmentReqDto;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.common.enums.SystemOptionTypeEnum;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static cn.cuiot.dmp.common.constant.NumberConst.QUERY_MAX_SIZE;

/**
 * @author liujianyu
 * @description 设备档案
 * @since 2024-05-15 10:29
 */
@RestController
@RequestMapping("/device")
public class DeviceArchivesController extends BaseController {

    @Autowired
    private DeviceArchivesService deviceArchivesService;
    @Autowired
    private ArchivesApiMapper archivesApiMapper;

    @Autowired
    private BuildingArchivesService buildingArchivesService;
    @Autowired
    private BuildingAndConfigCommonUtilService buildingAndConfigCommonUtilService;

    /**
     * 根据id获取详情
     */
    @PostMapping("/queryForDetail")
    public DeviceArchivesEntity queryForDetail(@RequestBody @Valid IdParam idParam) {
        DeviceArchivesEntity res = deviceArchivesService.getById(idParam.getId());
        res.setQrCodeId(archivesApiMapper.getCodeId(idParam.getId(), SystemOptionTypeEnum.DEVICE_ARCHIVE.getCode()));

        return res;
    }

    /**
     * 分页列表
     */
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<DeviceArchivesEntity>> queryForPage(@RequestBody @Valid DeviceArchivesQuery query) {
        LambdaQueryWrapper<DeviceArchivesEntity> wrapper = new LambdaQueryWrapper<>();
        if (Objects.isNull(query.getLoupanId())) {
            // 获取当前平台下的楼盘列表
            List<BuildingArchive> buildingArchives = buildingArchivesService.lookupBuildingArchiveByDepartmentList(query.getDepartmentId());
            if (CollectionUtils.isEmpty(buildingArchives)) {
                return IdmResDTO.success(new Page<>());
            }
            List<Long> buildingIdList = buildingArchives.stream()
                    .map(BuildingArchive::getId)
                    .collect(Collectors.toList());
            wrapper.in(CollectionUtils.isNotEmpty(buildingIdList), DeviceArchivesEntity::getLoupanId, buildingIdList);
        }
        wrapper.eq(Objects.nonNull(query.getId()), DeviceArchivesEntity::getId, query.getId());
        wrapper.eq(Objects.nonNull(query.getLoupanId()), DeviceArchivesEntity::getLoupanId, query.getLoupanId());
        wrapper.like(StringUtils.isNotBlank(query.getDeviceName()), DeviceArchivesEntity::getDeviceName, query.getDeviceName());
        wrapper.like(StringUtils.isNotBlank(query.getDeviceProfessional()), DeviceArchivesEntity::getDeviceProfessional, query.getDeviceProfessional());
        wrapper.like(StringUtils.isNotBlank(query.getInstallationLocation()), DeviceArchivesEntity::getInstallationLocation, query.getInstallationLocation());
        wrapper.eq(Objects.nonNull(query.getDeviceStatus()), DeviceArchivesEntity::getDeviceStatus, query.getDeviceStatus());
        wrapper.eq(Objects.nonNull(query.getDeviceCategory()), DeviceArchivesEntity::getDeviceCategory, query.getDeviceCategory());
        wrapper.orderByDesc(DeviceArchivesEntity::getCreateTime);
        IPage<DeviceArchivesEntity> res = deviceArchivesService.page(new Page<>(query.getPageNo(), query.getPageSize()), wrapper);
        List<DeviceArchivesEntity> records = res.getRecords();
        records.forEach(r->{
            BuildingArchivesVO buildingArchivesVO = buildingArchivesService.queryForDetail(r.getLoupanId());
            r.setBuildingArchivesVO(buildingArchivesVO);
        });
        return IdmResDTO.success(res);
    }

    /**
     * 创建
     */
    @RequiresPermissions
    @LogRecord(operationCode = "saveDeviceArchives", operationName = "保存设备档案", serviceType = ServiceTypeConst.ARCHIVE_CENTER)
    @PostMapping("/create")
    public IdmResDTO create(@RequestBody @Valid DeviceArchivesEntity entity) {
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
    @LogRecord(operationCode = "updateDeviceArchives", operationName = "修改设备档案", serviceType = ServiceTypeConst.ARCHIVE_CENTER)
    @PostMapping("/update")
    public IdmResDTO update(@RequestBody DeviceArchivesEntity entity) {
        deviceArchivesService.updateById(entity);
        return IdmResDTO.success(null);
    }

    /**
     * 删除
     */
    @RequiresPermissions
    @LogRecord(operationCode = "deleteDeviceArchives", operationName = "删除设备档案", serviceType = ServiceTypeConst.ARCHIVE_CENTER)
    @PostMapping("/delete")
    public IdmResDTO delete(@RequestBody @Valid IdParam idParam) {
        deviceArchivesService.removeById(idParam.getId());
        return IdmResDTO.success();
    }

    /**
     * 批量修改
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateByIdsDeviceArchives", operationName = "批量修改设备档案", serviceType = ServiceTypeConst.ARCHIVE_CENTER)
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
     * 批量删除
     */
    @RequiresPermissions
    @LogRecord(operationCode = "deleteByIdsDeviceArchives", operationName = "批量删除设备档案", serviceType = ServiceTypeConst.ARCHIVE_CENTER)
    @PostMapping("/deleteByIds")
    public IdmResDTO deleteByIds(@RequestBody @Valid IdsParam param) {
        deviceArchivesService.removeByIds(param.getIds());
        return IdmResDTO.success();
    }

    /**
     * 导出,按照id列表
     */
    @RequiresPermissions
    @PostMapping(value = "/export", produces = MediaType.APPLICATION_JSON_VALUE)
    public void export(@RequestBody DeviceArchivesQuery param) throws IOException {
        param.setPageSize(QUERY_MAX_SIZE + 1);
        IdmResDTO<IPage<DeviceArchivesEntity>> pageResult = queryForPage(param);
        List<DeviceArchivesEntity> dataList =  Optional.ofNullable(pageResult.getData().getRecords()).orElse(Lists.newArrayList());;
        AssertUtil.isFalse(dataList.size() > QUERY_MAX_SIZE, "一次最多可导出1万条数据，请筛选条件分多次导出！");
        List<DeviceArchivesExportVo> exportVos = deviceArchivesService.buildExportData(dataList);
        List<Map<String, Object>> sheetsList = new ArrayList<>();
        Map<String, Object> sheet1 = ExcelUtils
                .createSheet("设备档案", exportVos, DeviceArchivesExportVo.class);
        sheetsList.add(sheet1);
        Workbook workbook = ExcelExportUtil.exportExcel(sheetsList, ExcelType.XSSF);
        String fileName = "设备导出(" + DateTimeUtil.localDateToString(LocalDate.now(), "yyyyMMdd") + ")";
        ExcelUtils.downLoadExcel(
                fileName,
                response,
                workbook);
    }

    private void addListCanNull(Set<Long> configIdList, Long configId){
        if (Objects.nonNull(configId)){
            configIdList.add(configId);
        }
    }

    private void getConfigIdFromEntity(DeviceArchivesEntity entity, Set<Long> configIdList){
        addListCanNull(configIdList, entity.getDeviceCategory());
        addListCanNull(configIdList, entity.getDeviceStatus());
        addListCanNull(configIdList, entity.getPropertyServiceLevel());
    }

    /**
     * 导入
     */
    @RequiresPermissions
    @PostMapping(value = "/import", produces = MediaType.APPLICATION_JSON_VALUE)
    public void importData(@RequestParam("file") MultipartFile file, @RequestParam(value = "loupanId", required = true) Long loupanId) throws Exception {

        AssertUtil.isFalse((null == file || file.isEmpty()), "上传文件为空");
        AssertUtil.isFalse((null == loupanId), "楼盘id为空");

        ImportParams params = new ImportParams();
        params.setHeadRows(1);

        List<DeviceArchivesImportDto> importDtoList = ExcelImportUtil
                .importExcel(file.getInputStream(), DeviceArchivesImportDto.class, params);

        if(CollectionUtils.isEmpty(importDtoList)){
            throw new BusinessException(ResultCode.REQUEST_FORMAT_ERROR, "excel解析失败");
        }
        AssertUtil.isFalse(importDtoList.size() > ArchivesApiConstant.ARCHIVES_IMPORT_MAX_NUM,
                "数据量超过5000条，导入失败");
        for (DeviceArchivesImportDto dto : importDtoList){
            deviceArchivesService.checkParamsImport(dto);
        }

        deviceArchivesService.importDataSave(importDtoList, loupanId, Long.valueOf(getOrgId()));
    }

    /**
     * 下载模板
     */
    @PostMapping("/downloadTemplate")
    public void downloadTemplate(HttpServletResponse response)
            throws IOException {
        BufferedOutputStream bos = null;
        String templatePath = "template/importDeviceArchives.xlsx";
        try (InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(templatePath)) {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder
                    .encode("设备档案导入模板.xls", "UTF-8"));
            bos = new BufferedOutputStream(response.getOutputStream());
            FileCopyUtils.copy(is, bos);
        } catch (Exception ex) {
            throw new BusinessException(ResultCode.INNER_ERROR, "下载失败");
        } finally {
            if (null != bos) {
                bos.flush();
                bos.close();
            }
        }
    }

}
