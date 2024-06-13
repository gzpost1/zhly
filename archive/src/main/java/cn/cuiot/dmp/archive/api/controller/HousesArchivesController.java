package cn.cuiot.dmp.archive.api.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.cuiot.dmp.archive.application.constant.ArchivesApiConstant;
import cn.cuiot.dmp.archive.application.param.dto.ArchiveBatchUpdateDTO;
import cn.cuiot.dmp.archive.application.param.dto.HousesArchiveImportDto;
import cn.cuiot.dmp.archive.application.param.query.HousesArchivesQuery;
import cn.cuiot.dmp.archive.application.param.vo.HousesArchiveExportVo;
import cn.cuiot.dmp.archive.application.service.HousesArchivesService;
import cn.cuiot.dmp.archive.infrastructure.entity.HousesArchivesEntity;
import cn.cuiot.dmp.archive.infrastructure.persistence.mapper.ArchivesApiMapper;
import cn.cuiot.dmp.archive.utils.ExcelUtils;
import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.IdsParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.common.enums.SystemOptionTypeEnum;
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

/**
 * 房屋档案
 * @author liujianyu
 * @since 2024-05-15 10:30
 */
@RestController
@RequestMapping("/houses")
public class HousesArchivesController extends BaseController {

    @Autowired
    private HousesArchivesService housesArchivesService;

    @Autowired
    private ArchivesApiMapper archivesApiMapper;

    /**
     * 根据id获取详情
     */
    @PostMapping("/queryForDetail")
    public HousesArchivesEntity queryForDetail(@RequestBody @Valid IdParam idParam) {
        HousesArchivesEntity res = housesArchivesService.getById(idParam.getId());
        res.setQrCodeId(archivesApiMapper.getCodeId(idParam.getId(), SystemOptionTypeEnum.HOUSE_ARCHIVE.getCode()));
        return res;
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
        wrapper.orderByDesc(HousesArchivesEntity::getCreateTime);
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
        // 计算使用率 公式：使用面积/建筑面积的结果，显示百分数，精确小数点后两位
        if (Objects.nonNull(entity.getUsableArea()) && Objects.nonNull(entity.getBuildingArea()) && entity.getBuildingArea() > 0.0){
            entity.setUtilizationRate(entity.getUsableArea()/entity.getBuildingArea());
        }
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
        // 计算使用率 公式：使用面积/建筑面积的结果，显示百分数，精确小数点后两位
        if (Objects.nonNull(entity.getUsableArea()) && Objects.nonNull(entity.getBuildingArea()) && entity.getBuildingArea() > 0.0){
            entity.setUtilizationRate(entity.getUsableArea()/entity.getBuildingArea());
        }
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
     * 批量删除
     */
    @RequiresPermissions
    @LogRecord(operationCode = "deleteByIdsHousesArchives", operationName = "批量删除房屋档案", serviceType = ServiceTypeConst.ARCHIVE_CENTER)
    @PostMapping("/deleteByIds")
    public IdmResDTO deleteByIds(@RequestBody @Valid IdsParam param) {
        housesArchivesService.removeByIds(param.getIds());
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
    public void importData(@RequestParam("file") MultipartFile file, @RequestParam(value = "loupanId", required = true) Long loupanId) throws Exception {

        AssertUtil.isFalse((null == file || file.isEmpty()), "上传文件为空");
        AssertUtil.isFalse((null == loupanId), "楼盘id为空");

        ImportParams params = new ImportParams();
        params.setHeadRows(1);

        List<HousesArchiveImportDto> importDtoList = ExcelImportUtil
                .importExcel(file.getInputStream(), HousesArchiveImportDto.class, params);

        if(CollectionUtils.isEmpty(importDtoList)){
            throw new BusinessException(ResultCode.REQUEST_FORMAT_ERROR, "excel解析失败");
        }
        AssertUtil.isFalse(importDtoList.size() > ArchivesApiConstant.ARCHIVES_IMPORT_MAX_NUM,
                "数据量超过5000条，导入失败");
        for (HousesArchiveImportDto dto : importDtoList){
            housesArchivesService.checkParamsImport(dto);
        }

        housesArchivesService.importDataSave(importDtoList, loupanId, Long.valueOf(getOrgId()));
    }

    /**
     * 下载模板
     */
    @PostMapping("/downloadTemplate")
    public void downloadTemplate(HttpServletResponse response)
            throws IOException {
        BufferedOutputStream bos = null;
        String templatePath = "template/importHousesArchives.xlsx";
        try (InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(templatePath)) {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder
                    .encode("房屋档案导入模板.xls", "UTF-8"));
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
