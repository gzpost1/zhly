package cn.cuiot.dmp.archive.api.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.cuiot.dmp.archive.application.param.dto.ArchiveBatchUpdateDTO;
import cn.cuiot.dmp.archive.application.param.dto.RoomArchivesImportDto;
import cn.cuiot.dmp.archive.application.param.query.RoomArchivesQuery;
import cn.cuiot.dmp.archive.application.param.vo.RoomArchivesExportVo;
import cn.cuiot.dmp.archive.application.service.RoomArchivesService;
import cn.cuiot.dmp.archive.infrastructure.entity.ParkingArchivesEntity;
import cn.cuiot.dmp.archive.infrastructure.entity.RoomArchivesEntity;
import cn.cuiot.dmp.archive.utils.ExcelUtils;
import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.IdsParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
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
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * @author liujianyu
 * @description 空间档案
 * @since 2024-05-15 10:30
 */
@RestController
@RequestMapping("/room")
public class RoomArchivesController extends BaseController {

    @Autowired
    private RoomArchivesService roomArchivesService;

    /**
     * 根据id获取详情
     */
    @PostMapping("/queryForDetail")
    public RoomArchivesEntity queryForDetail(@RequestBody @Valid IdParam idParam) {
        return roomArchivesService.getById(idParam.getId());
    }

    /**
     * 分页列表
     */
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<RoomArchivesEntity>> queryForPage(@RequestBody @Valid RoomArchivesQuery query) {
        LambdaQueryWrapper<RoomArchivesEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoomArchivesEntity::getLoupanId, query.getLoupanId());
        wrapper.like(StringUtils.isNotBlank(query.getName()), RoomArchivesEntity::getName, query.getName());
        wrapper.like(StringUtils.isNotBlank(query.getOwnershipUnit()), RoomArchivesEntity::getOwnershipUnit, query.getOwnershipUnit());
        wrapper.eq(Objects.nonNull(query.getResourceType()), RoomArchivesEntity::getResourceType, query.getResourceType());
        wrapper.eq(Objects.nonNull(query.getStatus()), RoomArchivesEntity::getStatus, query.getStatus());
        wrapper.eq(Objects.nonNull(query.getProfessionalPurpose()), RoomArchivesEntity::getProfessionalPurpose, query.getProfessionalPurpose());
        wrapper.eq(Objects.nonNull(query.getLocationMethod()), RoomArchivesEntity::getLocationMethod, query.getLocationMethod());
        wrapper.eq(Objects.nonNull(query.getSpaceCategory()), RoomArchivesEntity::getSpaceCategory, query.getSpaceCategory());
        wrapper.eq(Objects.nonNull(query.getBusinessNature()), RoomArchivesEntity::getBusinessNature, query.getBusinessNature());
        wrapper.eq(Objects.nonNull(query.getOwnershipAttribute()), RoomArchivesEntity::getOwnershipAttribute, query.getOwnershipAttribute());
        IPage<RoomArchivesEntity> res = roomArchivesService.page(new Page<>(query.getPageNo(), query.getPageSize()), wrapper);
        return IdmResDTO.success(res);
    }

    /**
     * 创建
     */
    @RequiresPermissions
    @LogRecord(operationCode = "saveRoomArchives", operationName = "保存空间档案", serviceType = ServiceTypeConst.ARCHIVE_CENTER)
    @PostMapping("/create")
    public IdmResDTO create(@RequestBody RoomArchivesEntity entity) {
        // 校验参数合法性，写在service层，用于导入的时候使用
        roomArchivesService.checkParams(entity);
        // 保存数据
        roomArchivesService.save(entity);
        return IdmResDTO.success();
    }

    /**
     * 修改
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateRoomArchives", operationName = "编辑空间档案", serviceType = ServiceTypeConst.ARCHIVE_CENTER)
    @PostMapping("/update")
    public IdmResDTO update(@RequestBody RoomArchivesEntity entity) {
        roomArchivesService.updateById(entity);
        return IdmResDTO.success();
    }

    /**
     * 删除
     */
    @RequiresPermissions
    @LogRecord(operationCode = "deleteRoomArchives", operationName = "删除空间档案", serviceType = ServiceTypeConst.ARCHIVE_CENTER)
    @PostMapping("/delete")
    public IdmResDTO delete(@RequestBody @Valid IdParam idParam) {
        roomArchivesService.removeById(idParam.getId());
        return IdmResDTO.success();
    }

    /**
     * 批量修改
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateByIdsRoomArchives", operationName = "批量编辑空间档案", serviceType = ServiceTypeConst.ARCHIVE_CENTER)
    @PostMapping("/updateByIds")
    public IdmResDTO updateByIds(@RequestBody @Valid ArchiveBatchUpdateDTO param) {
        LambdaQueryWrapper<RoomArchivesEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(RoomArchivesEntity::getId, param.getIds());

        RoomArchivesEntity entity = new RoomArchivesEntity();
        entity.setLoupanId(param.getLoupanId());
        roomArchivesService.update(entity, wrapper);
        return IdmResDTO.success();
    }

    /**
     * 批量删除
     */
    @RequiresPermissions
    @LogRecord(operationCode = "deleteByIdsRoomArchives", operationName = "批量删除空间档案", serviceType = ServiceTypeConst.ARCHIVE_CENTER)
    @PostMapping("/deleteByIds")
    public IdmResDTO deleteByIds(@RequestBody @Valid IdsParam param) {
        roomArchivesService.removeByIds(param.getIds());
        return IdmResDTO.success();
    }

    /**
     * 导出,按照id列表
     */
    @RequiresPermissions
    @PostMapping(value = "/export", produces = MediaType.APPLICATION_JSON_VALUE)
    public void export(@RequestBody IdsParam param) throws IOException {
        List<RoomArchivesExportVo> dataList = roomArchivesService.buildExportData(param);
        List<Map<String, Object>> sheetsList = new ArrayList<>();
        Map<String, Object> sheet1 = ExcelUtils
                .createSheet("空间档案", dataList, RoomArchivesExportVo.class);

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

        List<RoomArchivesImportDto> importDtoList = ExcelImportUtil
                .importExcel(file.getInputStream(), RoomArchivesImportDto.class, params);

        if(CollectionUtils.isEmpty(importDtoList)){
            throw new BusinessException(ResultCode.REQUEST_FORMAT_ERROR, "excel解析失败");
        }
        for (RoomArchivesImportDto dto : importDtoList){
            roomArchivesService.checkParamsImport(dto);
        }

        roomArchivesService.importDataSave(importDtoList, loupanId);
    }

    /**
     * 下载模板
     */
    @PostMapping("/downloadTemplate")
    public IdmResDTO<Object> download(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        String fileName = "importRoomArchives.xlsx";
        String template;
        template = "/template/importRoomArchives.xlsx";
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        try (InputStream inStream = this.getClass().getResourceAsStream(template)) {
            OutputStream outputStream = response.getOutputStream();
            byte[] b = new byte[1000];
            int len;
            if (inStream != null) {
                while ((len = inStream.read(b)) > 0) {
                    outputStream.write(b, 0, len);
                }
            }
        }
        return IdmResDTO.success();
    }
}
