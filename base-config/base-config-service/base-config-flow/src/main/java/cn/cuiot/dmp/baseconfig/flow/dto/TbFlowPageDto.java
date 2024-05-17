package cn.cuiot.dmp.baseconfig.flow.dto;

import cn.cuiot.dmp.base.infrastructure.dto.PageInfoBaseDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description 分页对象
 * @Date 2024/4/23 17:11
 * @Created by libo
 */
@Data
public class TbFlowPageDto extends PageInfoBaseDto implements Serializable {

    /**
     * 流程名称
     */
    private String name;

    /**
     * 流程说明
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

    /**
     * 图片
     */
    private String logo;

    /**
     * 流程定义ID
     */
    private String processDefinitionId;

}
