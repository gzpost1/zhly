package cn.cuiot.dmp.archive.api.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.cuiot.dmp.archive.application.param.dto.CustomerDto;
import cn.cuiot.dmp.archive.application.param.query.CustomerQuery;
import cn.cuiot.dmp.archive.application.service.CustomerService;
import cn.cuiot.dmp.archive.infrastructure.vo.ArchiveOptionItemVo;
import cn.cuiot.dmp.archive.infrastructure.vo.CustomerExportVo;
import cn.cuiot.dmp.archive.infrastructure.vo.CustomerHouseExportVo;
import cn.cuiot.dmp.archive.infrastructure.vo.CustomerHouseVo;
import cn.cuiot.dmp.archive.infrastructure.vo.CustomerVo;
import cn.cuiot.dmp.archive.utils.ExcelUtils;
import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.utils.CommonCsvUtil;
import cn.cuiot.dmp.base.infrastructure.dto.DeleteParam;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.common.enums.SystemOptionTypeEnum;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 【PC】客户管理
 *
 * @author wuyongchong
 * @since 2024-06-12
 */
@Slf4j
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Resource
    protected HttpServletResponse response;

    /**
     * 分页查询
     */
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<CustomerVo>> queryForPage(@RequestBody CustomerQuery query) {
        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        query.setCompanyId(currentOrgId);
        IPage<CustomerVo> pageData = customerService.queryForPage(query);
        return IdmResDTO.success(pageData);
    }

    /**
     * 获取详情
     */
    @PostMapping("/queryForDetail")
    public IdmResDTO<CustomerVo> queryForDetail(@RequestBody @Valid IdParam idParam) {
        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        CustomerVo data = customerService.queryForDetail(idParam.getId(), currentOrgId);
        return IdmResDTO.success(data);
    }

    /**
     * 创建
     */
    @RequiresPermissions
    @LogRecord(operationCode = "createCustomer", operationName = "创建客户", serviceType = ServiceTypeConst.ARCHIVE_CENTER)
    @PostMapping("/create")
    public IdmResDTO create(@RequestBody @Valid CustomerDto dto) {
        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        dto.setCompanyId(currentOrgId);
        customerService.createCustomer(dto);
        return IdmResDTO.success();
    }

    /**
     * 修改
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateCustomer", operationName = "修改客户", serviceType = ServiceTypeConst.ARCHIVE_CENTER)
    @PostMapping("/update")
    public IdmResDTO update(@RequestBody @Valid CustomerDto dto) {
        if (Objects.isNull(dto.getId())) {
            throw new BusinessException(ResultCode.PARAM_CANNOT_NULL, "ID不能为空");
        }
        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        dto.setCompanyId(currentOrgId);
        customerService.updateCustomer(dto);
        return IdmResDTO.success();
    }

    /**
     * 删除
     */
    @RequiresPermissions
    @LogRecord(operationCode = "deleteCustomer", operationName = "删除客户", serviceType = ServiceTypeConst.ARCHIVE_CENTER)
    @PostMapping("/delete")
    public IdmResDTO delete(@RequestBody @Valid DeleteParam deleteParam) {
        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        customerService.deleteCustomer(deleteParam.getId(), currentOrgId);
        return IdmResDTO.success();
    }

    /**
     * 修改状态
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateCustomerStatus", operationName = "修改客户状态", serviceType = ServiceTypeConst.ARCHIVE_CENTER)
    @PostMapping("/updateStatus")
    public IdmResDTO updateStatus(@RequestBody @Valid UpdateStatusParam updateStatusParam) {
        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        customerService.updateStatus(updateStatusParam.getId(), updateStatusParam.getStatus(),
                currentOrgId);
        return IdmResDTO.success();
    }

    /**
     * 下载模板
     */
    @PostMapping("/downloadTemplate")
    public void downloadExcel(HttpServletResponse response)
            throws IOException {
        BufferedOutputStream bos = null;
        String templatePath = "template/importCustomers.xlsx";
        try (InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(templatePath)) {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder
                    .encode("客户导入模板.xls", "UTF-8"));
            bos = new BufferedOutputStream(response.getOutputStream());
            FileCopyUtils.copy(is, bos);
        } catch (Exception ex) {
            log.error("download extractTemplate error", ex);
            throw new BusinessException(ResultCode.INNER_ERROR, "下载失败");
        } finally {
            if (null != bos) {
                bos.flush();
                bos.close();
            }
        }
    }

    /**
     * 导入客户
     */
    @RequiresPermissions
    @LogRecord(operationCode = "importCustomers", operationName = "导入客户", serviceType = ServiceTypeConst.ARCHIVE_CENTER)
    @PostMapping(value = "/importCustomers")
    public IdmResDTO importCustomers(@RequestParam("file") MultipartFile file) throws Exception {
        AssertUtil.isFalse((null == file || file.isEmpty()), "上传文件为空");
        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
        List<List<Object>> readList = reader.read(2);
        if(CollectionUtils.isEmpty(readList)){
            throw new BusinessException(ResultCode.INNER_ERROR, "解析内容失败");
        }
        int size = readList.size();
        List<String> headers = readList.get(0).stream().map(ite -> ite.toString())
                .collect(Collectors.toList());
        List<List<Object>> dataList = Lists.newArrayList();
        for(int i=1;i<size;i++){
            dataList.add(readList.get(i));
        }
        if(CollectionUtils.isEmpty(dataList)){
            throw new BusinessException(ResultCode.INNER_ERROR, "解析内容为空");
        }
        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        List<CustomerDto> dtoList = customerService.analysisImportData(currentOrgId,headers,dataList);
        customerService.saveImportCustomers(dtoList);
        return IdmResDTO.success();
    }

    /**
     * 导出客户
     */
    @RequiresPermissions
    @LogRecord(operationCode = "exportCustomers", operationName = "导出客户", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping(value = "/exportCustomers", produces = MediaType.APPLICATION_JSON_VALUE)
    public void exportCustomers(@RequestBody CustomerQuery query) throws IOException {
        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        query.setCompanyId(currentOrgId);
        List<CustomerVo> dataList = customerService.queryForList(query);
        if(CollectionUtils.isEmpty(dataList)){
            throw new BusinessException(ResultCode.INNER_ERROR, "没有可导出数据");
        }
        //找出房屋最多的数量
        Integer maxHouseSize = dataList.stream().map(item->{
            if(CollectionUtils.isEmpty(item.getHouseList())){
                return 0;
            }
            return item.getHouseList().size();
        }).max(Integer::compareTo).get();

        List<ArchiveOptionItemVo> optionItems = customerService.getCustomerOptionItems();

        List<CustomerExportVo> dataExportList= customerService.buildExportData(dataList,optionItems);

        List<Map<String, Object>> sheetsList = new ArrayList<>();
        Map<String, Object> sheet1 = ExcelUtils
                .createSheet("客户信息", dataExportList, CustomerExportVo.class);
        sheetsList.add(sheet1);
        Workbook workbook = ExcelExportUtil.exportExcel(sheetsList, ExcelType.XSSF);
        Integer baseCols = 14;
        Integer perHouseCols = 4;
        if(maxHouseSize>0){
            Integer houseStartIndex = baseCols;
            Integer maxCols = baseCols+(maxHouseSize*perHouseCols);
            Sheet sheetAt = workbook.getSheetAt(0);
            //动态构建列头
            Row headerRow = sheetAt.getRow(0);
            int n=1;
            while (houseStartIndex<maxCols){
                headerRow.createCell(houseStartIndex, CellType.STRING).setCellValue("关联房屋id"+n);
                headerRow.createCell(houseStartIndex+1, CellType.STRING).setCellValue("身份");
                headerRow.createCell(houseStartIndex+2, CellType.STRING).setCellValue("迁入日期");
                headerRow.createCell(houseStartIndex+3, CellType.STRING).setCellValue("迁出日期");
                houseStartIndex=houseStartIndex+perHouseCols;
                n=n+1;
            }
            //动态填充数据
            int i=1;
            for(CustomerVo data:dataList){
                Row dataRow = sheetAt.getRow(i);
                if(CollectionUtils.isNotEmpty(data.getHouseList())){
                    List<CustomerHouseExportVo> houseList = customerService.buildHouseExportData(data.getHouseList(),optionItems);
                    Integer dataHouseStartIndex = baseCols;
                    for(CustomerHouseExportVo houseExportVo:houseList){
                        dataRow.createCell(dataHouseStartIndex, CellType.STRING).setCellValue(houseExportVo.getHouseId().toString());
                        dataRow.createCell(dataHouseStartIndex+1, CellType.STRING).setCellValue(houseExportVo.getIdentityTypeName());
                        dataRow.createCell(dataHouseStartIndex+2, CellType.STRING).setCellValue(houseExportVo.getMoveInDateStr());
                        dataRow.createCell(dataHouseStartIndex+3, CellType.STRING).setCellValue(houseExportVo.getMoveOutDateStr());
                        dataHouseStartIndex=dataHouseStartIndex+perHouseCols;
                    }
                }
                i=i+1;
            }
        }
        //返回文件
        ExcelUtils.downLoadExcel("客户信息", response,workbook);
    }


}
