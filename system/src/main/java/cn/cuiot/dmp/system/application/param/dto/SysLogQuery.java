package cn.cuiot.dmp.system.application.param.dto;

import cn.cuiot.dmp.common.bean.PageQuery;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 系统日志查询参数
 * @author: wuyongchong
 * @date: 2024/6/18 10:19
 */
@Data
public class SysLogQuery extends PageQuery {

    /**
     * 操作端 1:web端 2:小程序-管理端 3:小程序-客户端
     */
    private String operationSource;

    /**
     * 操作类型名称
     */
    private String serviceTypeName;

    /**
     * 操作开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date optStartTime;

    /**
     * 操作结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date optEndTime;

}
