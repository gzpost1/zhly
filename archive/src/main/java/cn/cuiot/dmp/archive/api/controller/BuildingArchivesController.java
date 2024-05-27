package cn.cuiot.dmp.archive.api.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.cuiot.dmp.archive.application.constant.ArchivesApiConstant;
import cn.cuiot.dmp.archive.application.param.dto.BatchBuildingArchivesDTO;
import cn.cuiot.dmp.archive.application.param.dto.BuildingArchiveImportDTO;
import cn.cuiot.dmp.archive.application.param.dto.BuildingArchivesCreateDTO;
import cn.cuiot.dmp.archive.application.param.dto.BuildingArchivesUpdateDTO;
import cn.cuiot.dmp.archive.application.param.vo.BuildingArchivesExportVO;
import cn.cuiot.dmp.archive.application.param.vo.BuildingArchivesVO;
import cn.cuiot.dmp.archive.application.service.BuildingArchivesService;
import cn.cuiot.dmp.archive.domain.aggregate.BuildingArchivesPageQuery;
import cn.cuiot.dmp.archive.utils.ExcelUtils;
import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.IdsParam;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.DepartmentTreeRspDTO;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 档案中心-楼盘档案
 *
 * @author caorui
 * @date 2024/5/21
 */
@RestController
@RequestMapping("/building")
public class BuildingArchivesController extends BaseController {

    @Resource
    protected HttpServletResponse response;

    @Autowired
    private BuildingArchivesService buildingArchivesService;

    /**
     * 根据id获取详情
     */
    @PostMapping("/queryForDetail")
    public BuildingArchivesVO queryForDetail(@RequestBody @Valid IdParam idParam) {
        return buildingArchivesService.queryForDetail(idParam.getId());
    }

    /**
     * 列表
     */
    @PostMapping("/queryForList")
    public List<BuildingArchivesVO> queryForList(@RequestBody @Valid BuildingArchivesPageQuery pageQuery) {
        String orgId = getOrgId();
        pageQuery.setCompanyId(Long.valueOf(orgId));
        return buildingArchivesService.queryForList(pageQuery);
    }

    /**
     * 分页列表
     */
    @PostMapping("/queryForPage")
    public PageResult<BuildingArchivesVO> queryForPage(@RequestBody @Valid BuildingArchivesPageQuery pageQuery) {
        String orgId = getOrgId();
        pageQuery.setCompanyId(Long.valueOf(orgId));
        return buildingArchivesService.queryForPage(pageQuery);
    }

    /**
     * 获取组织楼盘树
     */
    @PostMapping("/getDepartmentBuildingTree")
    public List<DepartmentTreeRspDTO> getDepartmentBuildingTree() {
        String orgId = getOrgId();
        String userId = getUserId();
        return buildingArchivesService.getDepartmentBuildingTree(Long.valueOf(orgId), Long.valueOf(userId));
    }

    /**
     * 保存
     */
    @RequiresPermissions
    @LogRecord(operationCode = "saveBuildingArchives", operationName = "保存楼盘档案", serviceType = ServiceTypeConst.ARCHIVE_CENTER)
    @PostMapping("/save")
    public int saveBuildingArchives(@RequestBody @Valid BuildingArchivesCreateDTO createDTO) {
        String orgId = getOrgId();
        createDTO.setCompanyId(Long.valueOf(orgId));
        return buildingArchivesService.saveBuildingArchives(createDTO);
    }

    /**
     * 更新
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateBuildingArchives", operationName = "更新楼盘档案", serviceType = ServiceTypeConst.ARCHIVE_CENTER)
    @PostMapping("/update")
    public int updateBuildingArchives(@RequestBody @Valid BuildingArchivesUpdateDTO updateDTO) {
        return buildingArchivesService.updateBuildingArchives(updateDTO);
    }

    /**
     * 删除
     */
    @RequiresPermissions
    @LogRecord(operationCode = "deleteBuildingArchives", operationName = "删除楼盘档案", serviceType = ServiceTypeConst.ARCHIVE_CENTER)
    @PostMapping("/delete")
    public int deleteBuildingArchives(@RequestBody @Valid IdParam idParam) {
        return buildingArchivesService.deleteBuildingArchives(idParam.getId());
    }

    /**
     * 批量更新
     */
    @RequiresPermissions
    @LogRecord(operationCode = "batchUpdateBuildingArchives", operationName = "批量更新楼盘档案", serviceType = ServiceTypeConst.ARCHIVE_CENTER)
    @PostMapping("/batchUpdate")
    public int batchUpdateBuildingArchives(@RequestBody @Valid BatchBuildingArchivesDTO batchBuildingArchivesDTO) {
        return buildingArchivesService.batchUpdateBuildingArchives(batchBuildingArchivesDTO);
    }

    /**
     * 批量删除
     */
    @RequiresPermissions
    @LogRecord(operationCode = "batchDeleteBuildingArchives", operationName = "批量删除楼盘档案", serviceType = ServiceTypeConst.ARCHIVE_CENTER)
    @PostMapping("/batchDelete")
    public int batchDeleteBuildingArchives(@RequestBody @Valid BatchBuildingArchivesDTO batchBuildingArchivesDTO) {
        return buildingArchivesService.batchDeleteBuildingArchives(batchBuildingArchivesDTO.getIdList());
    }

    /**
     * 下载模板
     */
    @PostMapping("/downloadTemplate")
    public void downloadTemplate(HttpServletResponse response)
            throws IOException {
        BufferedOutputStream bos = null;
        String templatePath = "template/importBuildingArchives.xlsx";
        try (InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(templatePath)) {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder
                    .encode("楼盘档案导入模板.xls", "UTF-8"));
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
     * 导入
     */
    @PostMapping("/import")
    public IdmResDTO<Object> importBuildingArchives(@RequestParam("file") MultipartFile file,
                                                    @RequestParam("departmentId") Long departmentId) throws Exception {
        AssertUtil.notNull(departmentId, "部门id不能为空");
        String orgId = getOrgId();
        AssertUtil.notBlank(orgId, "组织id不能为空");
        String userId = getUserId();
        AssertUtil.notBlank(userId, "用户id不能为空");
        ImportParams params = new ImportParams();
        params.setTitleRows(0);
        params.setHeadRows(1);
        params.setNeedVerify(true);
        List<BuildingArchiveImportDTO> buildingArchiveImportDTOList = ExcelImportUtil.importExcel(file.getInputStream(),
                BuildingArchiveImportDTO.class, params);
        AssertUtil.notEmpty(buildingArchiveImportDTOList, "导入数据为空");
        AssertUtil.isFalse(buildingArchiveImportDTOList.size() > ArchivesApiConstant.ARCHIVES_IMPORT_MAX_NUM,
                "数据量超过5000条，导入失败");
        buildingArchivesService.importBuildingArchives(buildingArchiveImportDTOList, Long.valueOf(orgId), departmentId,
                Long.valueOf(userId));
        return IdmResDTO.success();
    }

    /**
     * 导出
     */
    @PostMapping(value = "/export")
    public IdmResDTO<Object> exportBuildingArchives(@RequestBody @Valid IdsParam param) throws IOException {
        BuildingArchivesPageQuery pageQuery = new BuildingArchivesPageQuery();
        pageQuery.setIdList(param.getIds());
        List<BuildingArchivesExportVO> buildingArchivesExportVOList = buildingArchivesService.queryForExportList(pageQuery);
        List<Map<String, Object>> sheetsList = new ArrayList<>();
        Map<String, Object> sheet1 = ExcelUtils.createSheet("楼盘档案", buildingArchivesExportVOList, BuildingArchivesExportVO.class);
        sheetsList.add(sheet1);
        Workbook workbook = ExcelExportUtil.exportExcel(sheetsList, ExcelType.XSSF);
        ExcelUtils.downLoadExcel("exportBuildingArchives-" + DateTimeUtil.dateToString(new Date(),
                "yyyyMMddHHmmss"), response, workbook);
        return IdmResDTO.success();
    }

}
