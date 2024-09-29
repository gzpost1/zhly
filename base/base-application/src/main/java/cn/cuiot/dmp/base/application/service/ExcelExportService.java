package cn.cuiot.dmp.base.application.service;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.cuiot.dmp.base.application.dto.ExcelReportDto;
import cn.cuiot.dmp.base.application.utils.ExcelUtil;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author pengjian
 * @create 2024/9/27 9:47
 */
@Slf4j
@Service
public class ExcelExportService {


    @Resource
    protected HttpServletResponse response;
    /**
     * 导出
     */
    public void excelExport(ExcelReportDto dto,Class clzz ) throws Exception {
        List<Map<String, Object>> sheetsList = new ArrayList<>();
        Map<String, Object> sheet1 = ExcelUtil
                .createSheet(dto.getTitle(), dto.getDataList(),clzz);
        sheetsList.add(sheet1);

        Workbook workbook = ExcelExportUtil.exportExcel(sheetsList, ExcelType.XSSF);

        ExcelUtil.downLoadExcel(
                dto.getFileName() + DateTimeUtil.dateToString(new Date(), "yyyyMMddHHmmss"),
                response,
                workbook);
    }
}
