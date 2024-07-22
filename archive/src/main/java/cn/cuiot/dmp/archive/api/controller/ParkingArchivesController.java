package cn.cuiot.dmp.archive.api.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.cuiot.dmp.archive.application.constant.ArchivesApiConstant;
import cn.cuiot.dmp.archive.application.param.dto.ArchiveBatchUpdateDTO;
import cn.cuiot.dmp.archive.application.param.dto.ParkingArchivesImportDto;
import cn.cuiot.dmp.archive.application.param.query.ParkingArchivesQuery;
import cn.cuiot.dmp.archive.application.param.vo.ParkingArchivesExportVo;
import cn.cuiot.dmp.archive.application.service.BuildingArchivesService;
import cn.cuiot.dmp.archive.application.service.ParkingArchivesService;
import cn.cuiot.dmp.archive.infrastructure.entity.ParkingArchivesEntity;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liujianyu
 * @description 车位档案
 * @since 2024-05-15 10:30
 */
@RestController
@RequestMapping("/parking")
public class ParkingArchivesController extends BaseController {

    @Autowired
    private ParkingArchivesService parkingArchivesService;
    @Autowired
    private ArchivesApiMapper archivesApiMapper;

    @Autowired
    private BuildingArchivesService buildingArchivesService;

    /**
     * 根据id获取详情
     */
    @PostMapping("/queryForDetail")
    public ParkingArchivesEntity queryForDetail(@RequestBody @Valid IdParam idParam) {
        ParkingArchivesEntity res = parkingArchivesService.getById(idParam.getId());
        res.setQrCodeId(archivesApiMapper.getCodeId(idParam.getId(), SystemOptionTypeEnum.PARK_ARCHIVE.getCode()));
        return res;
    }

    /**
     * 分页列表
     */
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<ParkingArchivesEntity>> queryForPage(@RequestBody @Valid ParkingArchivesQuery query) {
        LambdaQueryWrapper<ParkingArchivesEntity> wrapper = new LambdaQueryWrapper<>();
        if (Objects.isNull(query.getLoupanId())) {
            // 获取当前平台下的楼盘列表
            DepartmentReqDto dto = new DepartmentReqDto();
            dto.setDeptId(LoginInfoHolder.getCurrentOrgId());
            dto.setSelfReturn(true);
            List<BuildingArchive> buildingArchives = buildingArchivesService.lookupBuildingArchiveByDepartmentList(dto);
            if (CollectionUtils.isEmpty(buildingArchives)) {
                return IdmResDTO.success(new Page<>());
            }
            List<Long> buildingIdList = buildingArchives.stream()
                    .map(BuildingArchive::getId)
                    .collect(Collectors.toList());
            wrapper.in(CollectionUtils.isNotEmpty(buildingIdList), ParkingArchivesEntity::getLoupanId, buildingIdList);
        }
        wrapper.eq(Objects.nonNull(query.getLoupanId()), ParkingArchivesEntity::getLoupanId, query.getLoupanId());
        wrapper.eq(StringUtils.isNotBlank(query.getCode()),ParkingArchivesEntity::getCode, query.getCode());
        wrapper.eq(Objects.nonNull(query.getStatus()), ParkingArchivesEntity::getStatus, query.getStatus());
        wrapper.eq(Objects.nonNull(query.getUsageStatus()), ParkingArchivesEntity::getUsageStatus, query.getUsageStatus());
        wrapper.eq(Objects.nonNull(query.getParkingType()), ParkingArchivesEntity::getParkingType, query.getParkingType());
        wrapper.orderByDesc(ParkingArchivesEntity::getCreateTime);
        IPage<ParkingArchivesEntity> res = parkingArchivesService.page(new Page<>(query.getPageNo(), query.getPageSize()), wrapper);
        return IdmResDTO.success(res);
    }

    /**
     * 创建
     */
    @RequiresPermissions
    @LogRecord(operationCode = "saveParkingArchives", operationName = "保存车位档案", serviceType = ServiceTypeConst.ARCHIVE_CENTER)
    @PostMapping("/create")
    public IdmResDTO create(@RequestBody ParkingArchivesEntity entity) {
        // 校验参数合法性，写在service层，用于导入的时候使用
        parkingArchivesService.checkParams(entity);
        // 保存数据
        parkingArchivesService.save(entity);
        return IdmResDTO.success(null);
    }

    /**
     * 修改
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateParkingArchives", operationName = "修改车位档案", serviceType = ServiceTypeConst.ARCHIVE_CENTER)
    @PostMapping("/update")
    public IdmResDTO update(@RequestBody ParkingArchivesEntity entity) {
        parkingArchivesService.updateById(entity);
        return IdmResDTO.success(null);
    }

    /**
     * 删除
     */
    @RequiresPermissions
    @LogRecord(operationCode = "deleteParkingArchives", operationName = "删除车位档案", serviceType = ServiceTypeConst.ARCHIVE_CENTER)
    @PostMapping("/delete")
    public IdmResDTO delete(@RequestBody @Valid IdParam idParam) {
        parkingArchivesService.removeById(idParam.getId());
        return IdmResDTO.success();
    }

    /**
     * 批量修改
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateByIdsParkingArchives", operationName = "批量修改车位档案", serviceType = ServiceTypeConst.ARCHIVE_CENTER)
    @PostMapping("/updateByIds")
    public IdmResDTO updateByIds(@RequestBody @Valid ArchiveBatchUpdateDTO param) {
        LambdaQueryWrapper<ParkingArchivesEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(ParkingArchivesEntity::getId, param.getIds());

        ParkingArchivesEntity entity = new ParkingArchivesEntity();
        entity.setLoupanId(param.getLoupanId());
        parkingArchivesService.update(entity, wrapper);
        return IdmResDTO.success();
    }
    /**
     * 批量删除
     */
    @RequiresPermissions
    @LogRecord(operationCode = "deleteByIdsParkingArchives", operationName = "批量删除车位档案", serviceType = ServiceTypeConst.ARCHIVE_CENTER)
    @PostMapping("/deleteByIds")
    public IdmResDTO deleteByIds(@RequestBody @Valid IdsParam param) {
        parkingArchivesService.removeByIds(param.getIds());
        return IdmResDTO.success();
    }

    /**
     * 导出,按照id列表
     */
    @RequiresPermissions
    @PostMapping(value = "/export", produces = MediaType.APPLICATION_JSON_VALUE)
    public void export(@RequestBody IdsParam param) throws IOException {
        List<ParkingArchivesExportVo> dataList = parkingArchivesService.buildExportData(param);
        List<Map<String, Object>> sheetsList = new ArrayList<>();
        Map<String, Object> sheet1 = ExcelUtils
                .createSheet("空间档案", dataList, ParkingArchivesExportVo.class);

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
    public void importData(@RequestParam("file") MultipartFile file, @RequestParam(value = "loupanId", required = true) Long loupanId) throws Exception {

        AssertUtil.isFalse((null == file || file.isEmpty()), "上传文件为空");
        AssertUtil.isFalse((null == loupanId), "楼盘id为空");

        ImportParams params = new ImportParams();
        params.setHeadRows(1);

        List<ParkingArchivesImportDto> importDtoList = ExcelImportUtil
                .importExcel(file.getInputStream(), ParkingArchivesImportDto.class, params);

        if(CollectionUtils.isEmpty(importDtoList)){
            throw new BusinessException(ResultCode.REQUEST_FORMAT_ERROR, "excel解析失败");
        }
        AssertUtil.isFalse(importDtoList.size() > ArchivesApiConstant.ARCHIVES_IMPORT_MAX_NUM,
                "数据量超过5000条，导入失败");
        for (ParkingArchivesImportDto dto : importDtoList){
            parkingArchivesService.checkParamsImport(dto);
        }

        parkingArchivesService.importDataSave(importDtoList, loupanId, Long.valueOf(getOrgId()));
    }

    /**
     * 下载模板
     */
    @PostMapping("/downloadTemplate")
    public void downloadTemplate(HttpServletResponse response)
            throws IOException {
        BufferedOutputStream bos = null;
        String templatePath = "template/importparkingArchives.xlsx";
        try (InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(templatePath)) {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder
                    .encode("车位档案导入模板.xls", "UTF-8"));
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
