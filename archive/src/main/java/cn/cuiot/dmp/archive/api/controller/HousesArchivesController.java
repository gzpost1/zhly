package cn.cuiot.dmp.archive.api.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.cuiot.dmp.archive.application.constant.ArchivesApiConstant;
import cn.cuiot.dmp.archive.application.param.dto.ArchiveBatchUpdateDTO;
import cn.cuiot.dmp.archive.application.param.dto.HouseTreeQueryDto;
import cn.cuiot.dmp.archive.application.param.dto.HousesArchiveImportDto;
import cn.cuiot.dmp.archive.application.param.query.HousesArchivesQuery;
import cn.cuiot.dmp.archive.application.param.vo.BuildingArchivesExportVO;
import cn.cuiot.dmp.archive.application.param.vo.BuildingArchivesVO;
import cn.cuiot.dmp.archive.application.param.vo.HousesArchiveExportVo;
import cn.cuiot.dmp.archive.application.service.BuildingArchivesService;
import cn.cuiot.dmp.archive.application.service.HousesArchivesService;
import cn.cuiot.dmp.archive.infrastructure.entity.HousesArchivesEntity;
import cn.cuiot.dmp.archive.infrastructure.persistence.mapper.ArchivesApiMapper;
import cn.cuiot.dmp.archive.utils.ExcelUtils;
import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.base.infrastructure.domain.pojo.IdsReq;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.IdsParam;
import cn.cuiot.dmp.base.infrastructure.dto.contract.ContractStatus;
import cn.cuiot.dmp.base.infrastructure.dto.contract.ContractStatusVo;
import cn.cuiot.dmp.base.infrastructure.dto.req.DepartmentReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.DepartmentTreeRspDTO;
import cn.cuiot.dmp.base.infrastructure.feign.ContractFeignService;
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
import com.google.common.collect.Maps;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static cn.cuiot.dmp.common.constant.NumberConst.QUERY_MAX_SIZE;

/**
 * 房屋档案
 *
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
    @Autowired
    ContractFeignService contractFeignService;

    @Autowired
    private BuildingArchivesService buildingArchivesService;

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
        if (Objects.isNull(query.getLoupanId())) {
            // 获取当前平台下的楼盘列表
            List<BuildingArchive> buildingArchives = buildingArchivesService.lookupBuildingArchiveByDepartmentList(query.getDepartmentId());
            if (CollectionUtils.isEmpty(buildingArchives)) {
                return IdmResDTO.success(new Page<>());
            }
            List<Long> buildingIdList = buildingArchives.stream()
                    .map(BuildingArchive::getId)
                    .collect(Collectors.toList());
            wrapper.in(CollectionUtils.isNotEmpty(buildingIdList), HousesArchivesEntity::getLoupanId, buildingIdList);
        }

        wrapper.in(CollectionUtils.isNotEmpty(query.getLoupanIds()), HousesArchivesEntity::getLoupanId, query.getLoupanIds());

        wrapper.eq(Objects.nonNull(query.getLoupanId()), HousesArchivesEntity::getLoupanId, query.getLoupanId());
        wrapper.eq(Objects.nonNull(query.getHouseType()), HousesArchivesEntity::getHouseType, query.getHouseType());
        wrapper.eq(Objects.nonNull(query.getOrientation()), HousesArchivesEntity::getOrientation, query.getOrientation());
        wrapper.eq(Objects.nonNull(query.getPropertyType()), HousesArchivesEntity::getPropertyType, query.getPropertyType());
        wrapper.eq(Objects.nonNull(query.getStatus()), HousesArchivesEntity::getStatus, query.getStatus());
        wrapper.eq(Objects.nonNull(query.getOwnershipAttribute()), HousesArchivesEntity::getOwnershipAttribute, query.getOwnershipAttribute());
        wrapper.eq(Objects.nonNull(query.getRoomNum()), HousesArchivesEntity::getRoomNum, query.getRoomNum());
        wrapper.and(StringUtils.isNotEmpty(query.getCode()), t -> t.like(HousesArchivesEntity::getCode, query.getCode()).or().like(HousesArchivesEntity::getRoomNum, query.getCode()));
        if (StringUtils.isNotBlank(query.getCodeAndOwnershipUnit())) {
            wrapper.like(HousesArchivesEntity::getCode, query.getCodeAndOwnershipUnit()).or().like(HousesArchivesEntity::getOwnershipUnit, query.getCodeAndOwnershipUnit());
        }
        if (StringUtils.isNotBlank(query.getName())) {
            wrapper.like(HousesArchivesEntity::getName, query.getName());
        }
        wrapper.orderByDesc(HousesArchivesEntity::getCreateTime);
        IPage<HousesArchivesEntity> res = housesArchivesService.page(new Page<>(query.getPageNo(), query.getPageSize()), wrapper);
        if (CollectionUtils.isEmpty(res.getRecords())) {
            return IdmResDTO.success(res);
        }
        List<HousesArchivesEntity> records = res.getRecords();
        records.forEach(r->{
            BuildingArchivesVO buildingArchivesVO = buildingArchivesService.queryForDetail(r.getLoupanId());
            r.setBuildingArchivesVO(buildingArchivesVO);
        });
        housesArchivesService.fillBuildingName(records);
        housesArchivesService.fullContractInfo(records);
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
        if (Objects.nonNull(entity.getUsableArea()) && Objects.nonNull(entity.getBuildingArea()) && entity.getBuildingArea() > 0.0) {
            entity.setUtilizationRate(entity.getUsableArea() / entity.getBuildingArea());
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
        if (Objects.nonNull(entity.getUsableArea()) && Objects.nonNull(entity.getBuildingArea()) && entity.getBuildingArea() > 0.0) {
            entity.setUtilizationRate(entity.getUsableArea() / entity.getBuildingArea());
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
     * 导出
     */
    @RequiresPermissions
    @PostMapping(value = "/export", produces = MediaType.APPLICATION_JSON_VALUE)
    public void export(@RequestBody HousesArchivesQuery param) throws IOException {
        param.setPageSize(QUERY_MAX_SIZE + 1);
        IdmResDTO<IPage<HousesArchivesEntity>> pageResult = queryForPage(param);
        List<HousesArchivesEntity> dataList = pageResult.getData().getRecords();
        AssertUtil.isFalse(CollectionUtils.isEmpty(dataList),"无数据");
        AssertUtil.isFalse(dataList.size() > QUERY_MAX_SIZE, "一次最多可导出1万条数据，请筛选条件分多次导出！");
        List<HousesArchiveExportVo> exportVos = housesArchivesService.buildExportData(dataList);
        List<Map<String, Object>> sheetsList = new ArrayList<>();
        Map<String, Object> sheet1 = ExcelUtils
                .createSheet("房屋档案", exportVos, HousesArchiveExportVo.class);
        sheetsList.add(sheet1);
        Workbook workbook = ExcelExportUtil.exportExcel(sheetsList, ExcelType.XSSF);
        String fileName = "房屋导出(" + DateTimeUtil.localDateToString(LocalDate.now(), "yyyyMMdd") + ")";
        ExcelUtils.downLoadExcel(
                fileName,
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
        params.setTitleRows(1);

        List<HousesArchiveImportDto> importDtoList = ExcelImportUtil
                .importExcel(file.getInputStream(), HousesArchiveImportDto.class, params);

        if (CollectionUtils.isEmpty(importDtoList)) {
            throw new BusinessException(ResultCode.REQUEST_FORMAT_ERROR, "excel解析失败");
        }
        AssertUtil.isFalse(importDtoList.size() > ArchivesApiConstant.ARCHIVES_IMPORT_MAX_NUM,
                "数据量超过5000条，导入失败");
        for (HousesArchiveImportDto dto : importDtoList) {
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

    /**
     * 获取组织楼盘房屋树
     */
    @PostMapping("/getDepartmentBuildingHouseTree")
    public List<DepartmentTreeRspDTO> getDepartmentBuildingHouseTree(@RequestBody HouseTreeQueryDto houseTreeQueryDto) {
        String orgId = getOrgId();
        String userId = getUserId();
        AssertUtil.notNull(orgId, "获取企业信息失败");
        AssertUtil.notNull(userId, "获取用户信息失败");

        houseTreeQueryDto.setOrgId(Long.valueOf(orgId));
        houseTreeQueryDto.setUserId(Long.valueOf(userId));
        return housesArchivesService.getDepartmentBuildingHouseTree(houseTreeQueryDto);
    }

}
