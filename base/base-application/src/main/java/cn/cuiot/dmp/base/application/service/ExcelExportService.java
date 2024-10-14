package cn.cuiot.dmp.base.application.service;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.cuiot.dmp.base.application.dto.ExcelDownloadCallable;
import cn.cuiot.dmp.base.application.dto.ExcelDownloadDto;
import cn.cuiot.dmp.base.application.dto.ExcelReportDto;
import cn.cuiot.dmp.base.application.utils.ExcelUtil;
import cn.cuiot.dmp.common.bean.PageQuery;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
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

    public static final Integer MAX_EXPORT_DATA = 10000;

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
                dto.getFileName() + DateTimeUtil.dateToString(new Date(), "yyyyMMdd"),
                response,
                workbook);
    }

    /**
     * 导出
     */
    public <T extends PageQuery, R> void  excelExport(ExcelDownloadDto<T> dto,Class<R> clzz ,
            ExcelDownloadCallable<T,R> func) {
        AtomicLong pageNo = new AtomicLong(1);
        long pages = 0;
        try {
            List<R> dataList = Lists.newArrayList();
            do {
                dto.getQuery().setPageNo(pageNo.getAndIncrement());
                dto.getQuery().setPageSize(2000L);
                IPage<R> page = func.excute(dto);
                if (page.getTotal() > ExcelExportService.MAX_EXPORT_DATA) {
                    throw new BusinessException(ResultCode.EXPORT_DATA_OVER_LIMIT);
                }
                pages = page.getPages();
                List<R> records = page.getRecords();
                if(CollectionUtils.isNotEmpty(records)){
                    dataList.addAll(records);
                }
            }while (pageNo.get() <= pages);

            if(CollectionUtils.isNotEmpty(dataList)){

                List<Map<String, Object>> sheetsList = new ArrayList<>();

                Map<String, Object> sheet1 = ExcelUtil
                        .createSheet(dto.getTitle(),dataList,clzz);

                sheetsList.add(sheet1);

                Workbook workbook = ExcelExportUtil.exportExcel(sheetsList, ExcelType.XSSF);

                ExcelUtil.downLoadExcel(dto.getFileName(),response,workbook);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }

    }
}
