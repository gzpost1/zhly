package cn.cuiot.dmp.archive.api.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.cuiot.dmp.archive.application.param.dto.ArchiveBatchUpdateDTO;
import cn.cuiot.dmp.archive.application.param.dto.ParkingArchivesImportDto;
import cn.cuiot.dmp.archive.application.param.query.ParkingArchivesQuery;
import cn.cuiot.dmp.archive.application.param.vo.ParkingArchivesExportVo;
import cn.cuiot.dmp.archive.application.service.ParkingArchivesService;
import cn.cuiot.dmp.archive.infrastructure.entity.HousesArchivesEntity;
import cn.cuiot.dmp.archive.infrastructure.entity.ParkingArchivesEntity;
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
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * @author liujianyu
 * @description 车位档案
 * @since 2024-05-15 10:30
 */
@RestController
@RequestMapping("/parking")
public class ParkingArchivesController {

    @Resource
    protected HttpServletRequest request;

    @Resource
    protected HttpServletResponse response;

    @Autowired
    private ParkingArchivesService parkingArchivesService;

    /**
     * 根据id获取详情
     */
    @PostMapping("/queryForDetail")
    public ParkingArchivesEntity queryForDetail(@RequestBody @Valid IdParam idParam) {
        return parkingArchivesService.getById(idParam.getId());
    }

    /**
     * 分页列表
     */
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<ParkingArchivesEntity>> queryForPage(@RequestBody @Valid ParkingArchivesQuery query) {
        LambdaQueryWrapper<ParkingArchivesEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ParkingArchivesEntity::getLoupanId, query.getLoupanId());
        wrapper.eq(StringUtils.isNotBlank(query.getCode()),ParkingArchivesEntity::getCode, query.getCode());
        wrapper.eq(Objects.nonNull(query.getStatus()), ParkingArchivesEntity::getStatus, query.getStatus());
        wrapper.eq(Objects.nonNull(query.getUsageStatus()), ParkingArchivesEntity::getUsageStatus, query.getUsageStatus());
        wrapper.eq(Objects.nonNull(query.getParkingType()), ParkingArchivesEntity::getParkingType, query.getParkingType());
        IPage<ParkingArchivesEntity> res = parkingArchivesService.page(new Page<>(query.getPageNo(), query.getPageSize()), wrapper);
        return IdmResDTO.success(res);
    }

    /**
     * 创建
     */
    @RequiresPermissions
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
    @PostMapping("/update")
    public IdmResDTO update(@RequestBody ParkingArchivesEntity entity) {
        parkingArchivesService.updateById(entity);
        return IdmResDTO.success(null);
    }

    /**
     * 删除
     */
    @RequiresPermissions
    @PostMapping("/delete")
    public IdmResDTO delete(@RequestBody @Valid IdParam idParam) {
        parkingArchivesService.removeById(idParam.getId());
        return IdmResDTO.success();
    }

    /**
     * 批量修改
     */
    @RequiresPermissions
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
    public void importData(@RequestParam("file") MultipartFile file) throws Exception {

        AssertUtil.isFalse((null == file || file.isEmpty()), "上传文件为空");

        ImportParams params = new ImportParams();
        params.setHeadRows(1);

        List<ParkingArchivesImportDto> importDtoList = ExcelImportUtil
                .importExcel(file.getInputStream(), ParkingArchivesImportDto.class, params);

        if(CollectionUtils.isEmpty(importDtoList)){
            throw new BusinessException(ResultCode.REQUEST_FORMAT_ERROR, "excel解析失败");
        }
        for (ParkingArchivesImportDto dto : importDtoList){
            parkingArchivesService.checkParamsImport(dto);
        }

        parkingArchivesService.importDataSave(importDtoList);
    }

    /**
     * 下载模板
     */
    @PostMapping("/downloadTemplate")
    public IdmResDTO<Object> download(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        String fileName = "importparkingArchives.xlsx";
        String template;
        template = "/template/importparkingArchives.xlsx";
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
