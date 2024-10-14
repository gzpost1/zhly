package cn.cuiot.dmp.base.application.dto;

import cn.cuiot.dmp.domain.types.LoginInfo;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author pengjian
 * @create 2024/9/27 17:12
 */
@Data
@Builder
public class ExcelReportDto<T,R> {

    /**
     * 登录信息存储
     */
    private LoginInfo loginInfo;
    /**
     * 过滤数据条件
     */
    private T query;

    /**
     * 导出
     */
    private String title;

    /**
     * 页名称
     */
    private String SheetName;

    /**
     * 类类型
     */
    private Class<?> clazz;

    /**
     * 文件名
     */
    private String fileName;
    /**
     * 请求数据
     */
    private List<R> dataList;
//
//    private DataConsumer<ExcelReportDto, List<R>> consumer;
//
//
//    @FunctionalInterface
//    public interface DataConsumer<ExcelReportDto, R> {
//        /**
//         * 异步消费导出数据
//         *
//         * @throws Exception 抛出异常
//         */
//        List<R> apply(ExcelReportDto dto) throws Exception;
//    }
}
