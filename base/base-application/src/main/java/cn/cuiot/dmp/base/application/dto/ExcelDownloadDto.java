package cn.cuiot.dmp.base.application.dto;

import cn.cuiot.dmp.domain.types.LoginInfo;
import lombok.Builder;
import lombok.Data;

/**
 * @author pengjian
 * @create 2024/10/14
 */
@Data
@Builder
public class ExcelDownloadDto<T> {

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
    private String sheetName;

    /**
     * 文件名
     */
    private String fileName;
}
