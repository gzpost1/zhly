package cn.cuiot.dmp.system.infrastructure.utils;


import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author maonian
 * @description 设备CSV文件导出工具类
 * @data 2020/8/11 11:10
 */
public class CommonCsvUtil {

    private CommonCsvUtil() {
        throw new BusinessException(ResultCode.CANNOT_CREATE_CONST_CLASS_OBJECT);
    }

    /**
     * CSV文件生成方法
     * @param head 表头数据（第一行标题）
     * @param dataList 文件内容
     * @param filename 文件名称
     * @return
     */
    public static void createCsvFile(List<Object> head, List<JSONObject> dataList, String filename, HttpServletResponse response) throws UnsupportedEncodingException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        filename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "attachment; filename=" + filename + ".csv");

        ServletOutputStream outputStream = null;
        try {

            outputStream = response.getOutputStream();
            //写入头文件
            for (Object data : head) {
                StringBuffer sb = new StringBuffer();
                byte[] bytes = sb.append("\"").append(data).append("\",").toString().getBytes("GBK");
                outputStream.write(bytes);
            }
            byte[] bytes = "\r\n".getBytes("GBK");
            outputStream.write(bytes);

            // 写入文件内容（倒叙）
            for (int i = 0; i < dataList.size(); i++) {
                JSONObject oneRow = dataList.get(i);
                List<Object> row = new ArrayList<>();
                //循环遍历key
                for (Object key : head){
                    row.add(oneRow.get(key));
                }
                //写入文件
                int num = 1;
                for (Object data : row) {
                    //防止直接存入null
                    if (data == null){
                        data = "";
                    }
                    StringBuffer sb = new StringBuffer();
                    byte[] byteBody = null;
                    if (row.size() > num) {
                        byteBody = sb.append("\"").append(data).append("\",").toString().getBytes("GBK");
                        num++;
                    }else if (row.size() == num) {
                        byteBody = sb.append("\"").append(data).append("\"").toString().getBytes("GBK");
                    }
                    outputStream.write(byteBody);
                }
                byte[] bytesLine = "\r\n".getBytes("GBK");
                outputStream.write(bytesLine);
            }
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
