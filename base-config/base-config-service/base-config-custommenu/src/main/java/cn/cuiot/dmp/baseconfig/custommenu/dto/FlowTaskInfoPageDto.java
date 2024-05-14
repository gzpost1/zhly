package cn.cuiot.dmp.baseconfig.custommenu.dto;

import cn.cuiot.dmp.base.infrastructure.dto.PageInfoBaseDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description 分页dto
 * @Date 2024/4/28 19:33
 * @Created by libo
 */
@Data
public class FlowTaskInfoPageDto extends PageInfoBaseDto {
    /**
     * 流程名称
     */
    private String name;

    /**
     * 任务描述
     */
    private String remark;

    /**
     * 状态 0停用 1启用
     */
    private Byte status;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
