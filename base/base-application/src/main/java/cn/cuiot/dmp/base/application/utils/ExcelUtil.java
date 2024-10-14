package cn.cuiot.dmp.base.application.utils;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.google.common.collect.Maps;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author pengjian
 * @create 2024/9/27 9:49
 */
public class ExcelUtil {

    /**
     * 创建参数
     * @param sheetName
     * @param data
     * @param clazz
     * @return
     */
    public static Map<String, Object> createSheet(String sheetName, Object data, Class clazz) {
        Map<String, Object> allDataMap = Maps.newHashMap();
        ExportParams allParams = new ExportParams();
        allParams.setSheetName(sheetName);
        allParams.setType(ExcelType.XSSF);
        allDataMap.put("title", allParams);
        allDataMap.put("entity", clazz);
        allDataMap.put("data", data);
        return allDataMap;
    }




    /**
     * 下载
     *
     * @param fileName 文件名称
     * @param workbook excel数据
     */
    public static void downLoadExcel(String fileName, HttpServletResponse response,
                                     Workbook workbook) throws IOException {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder
                    .encode(fileName + "." + ExcelTypeEnum.XLSX.getValue(), "UTF-8"));
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }



    /**
     * Excel 类型枚举
     */
    enum ExcelTypeEnum {
        XLS("xls"), XLSX("xlsx");
        private String value;

        ExcelTypeEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
