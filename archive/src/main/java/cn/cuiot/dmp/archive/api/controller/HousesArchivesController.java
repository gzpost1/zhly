package cn.cuiot.dmp.archive.api.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.cuiot.dmp.archive.application.param.dto.ArchiveBatchUpdateDTO;
import cn.cuiot.dmp.archive.application.param.dto.HousesArchiveImportDto;
import cn.cuiot.dmp.archive.application.param.query.HousesArchivesQuery;
import cn.cuiot.dmp.archive.application.param.vo.HousesArchiveExportVo;
import cn.cuiot.dmp.archive.application.service.HousesArchivesService;
import cn.cuiot.dmp.archive.infrastructure.entity.DeviceArchivesEntity;
import cn.cuiot.dmp.archive.infrastructure.entity.HousesArchivesEntity;
import cn.cuiot.dmp.archive.utils.ExcelUtils;
import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
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
 * @description 房屋档案
 * @since 2024-05-15 10:30
 */
@RestController
@RequestMapping("/houses")
public class HousesArchivesController {

    @Resource
    protected HttpServletRequest request;

    @Resource
    protected HttpServletResponse response;

    @Autowired
    private HousesArchivesService housesArchivesService;

    /**
     * 根据id获取详情
     */
    @PostMapping("/queryForDetail")
    public HousesArchivesEntity queryForDetail(@RequestBody @Valid IdParam idParam) {
        return housesArchivesService.getById(idParam.getId());
    }

    /**
     * 分页列表
     */
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<HousesArchivesEntity>> queryForPage(@RequestBody @Valid HousesArchivesQuery query) {
        LambdaQueryWrapper<HousesArchivesEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HousesArchivesEntity::getLoupanId, query.getLoupanId());
        wrapper.eq(Objects.nonNull(query.getHouseType()), HousesArchivesEntity::getHouseType, query.getHouseType());
        wrapper.eq(Objects.nonNull(query.getOrientation()), HousesArchivesEntity::getOrientation, query.getOrientation());
        wrapper.eq(Objects.nonNull(query.getPropertyType()), HousesArchivesEntity::getPropertyType, query.getPropertyType());
        wrapper.eq(Objects.nonNull(query.getStatus()), HousesArchivesEntity::getStatus, query.getStatus());
        wrapper.eq(Objects.nonNull(query.getOwnershipAttribute()), HousesArchivesEntity::getOwnershipAttribute, query.getOwnershipAttribute());
        if (StringUtils.isNotBlank(query.getCodeAndOwnershipUnit())){
            wrapper.like( HousesArchivesEntity::getCode, query.getCodeAndOwnershipUnit()).or().like(HousesArchivesEntity::getOwnershipUnit, query.getCodeAndOwnershipUnit());
        }

        IPage<HousesArchivesEntity> res = housesArchivesService.page(new Page<>(query.getPageNo(), query.getPageSize()), wrapper);
        return IdmResDTO.success(res);
    }

    /**
     * 创建
     */
    @RequiresPermissions
    @LogRecord(operationCode = "saveHousesArchives", operationName = "保存房屋档案", serviceType = ServiceTypeConst.ARCHIVE_CENTER)
    @PostMapping("/create")
    public IdmResDTO create(@RequestBody HousesArchivesEntity entity) {
        // 校验参数合法性，写在service层，用于导入的时候使用
        housesArchivesService.checkParams(entity);
        // 保存数据
        housesArchivesService.save(entity);
        return IdmResDTO.success();
    }

    /**
     * 修改
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateHousesArchives", operationName = "编辑房屋档案", serviceType = ServiceTypeConst.ARCHIVE_CENTER)
    @PostMapping("/update")
    public IdmResDTO update(@RequestBody HousesArchivesEntity entity) {
        housesArchivesService.updateById(entity);
        return IdmResDTO.success();
    }

    /**
     * 删除
     */
    @RequiresPermissions
    @LogRecord(operationCode = "deleteHousesArchives", operationName = "删除房屋档案", serviceType = ServiceTypeConst.ARCHIVE_CENTER)
    @PostMapping("/delete")
    public IdmResDTO delete(@RequestBody @Valid IdParam idParam) {
        housesArchivesService.removeById(idParam.getId());
        return IdmResDTO.success();
    }

    /**
     * 批量修改
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateByIdsHousesArchives", operationName = "批量编辑房屋档案", serviceType = ServiceTypeConst.ARCHIVE_CENTER)
    @PostMapping("/updateByIds")
    public IdmResDTO updateByIds(@RequestBody @Valid ArchiveBatchUpdateDTO param) {
        LambdaQueryWrapper<HousesArchivesEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(HousesArchivesEntity::getId, param.getIds());

        HousesArchivesEntity entity = new HousesArchivesEntity();
        entity.setLoupanId(param.getLoupanId());
        housesArchivesService.update(entity, wrapper);
        return IdmResDTO.success();
    }

    /**
     * 导出,按照id列表
     */
    @RequiresPermissions
    @PostMapping(value = "/export", produces = MediaType.APPLICATION_JSON_VALUE)
    public void export(@RequestBody IdsParam param) throws IOException {
        List<HousesArchiveExportVo> dataList = housesArchivesService.buildExportData(param);
        List<Map<String, Object>> sheetsList = new ArrayList<>();
        Map<String, Object> sheet1 = ExcelUtils
                .createSheet("房屋档案", dataList, HousesArchiveExportVo.class);

        sheetsList.add(sheet1);

        Workbook workbook = ExcelExportUtil.exportExcel(sheetsList, ExcelType.XSSF);

        ExcelUtils.downLoadExcel(
                "houses-" + DateTimeUtil.dateToString(new Date(), "yyyyMMddHHmmss"),
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

        List<HousesArchiveImportDto> importDtoList = ExcelImportUtil
                .importExcel(file.getInputStream(), HousesArchiveImportDto.class, params);

        if(CollectionUtils.isEmpty(importDtoList)){
            throw new BusinessException(ResultCode.REQUEST_FORMAT_ERROR, "excel解析失败");
        }
        for (HousesArchiveImportDto dto : importDtoList){
            housesArchivesService.checkParamsImport(dto);
        }

        housesArchivesService.importDataSave(importDtoList);
    }

    /**
     * 下载模板
     */
    @PostMapping("/downloadTemplate")
    public IdmResDTO<Object> download(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        String fileName = "importHouseesArchives.xlsx";
        String template;
        template = "/template/importHouseesArchives.xlsx";
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
