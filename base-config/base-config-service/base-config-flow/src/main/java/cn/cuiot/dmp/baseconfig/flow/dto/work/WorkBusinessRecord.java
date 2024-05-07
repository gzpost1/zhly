package cn.cuiot.dmp.baseconfig.flow.dto.work;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author pengjian
 * @create 2024/4/30 16:23
 */
@Data
public class WorkBusinessRecord {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 人员名称
     */
    private String  userName;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String createTime;

    /**
     * 操作类型
     */
    private Byte businessType;

    /**
     * 内容
     */
    private String content;
}
