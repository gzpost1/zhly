package cn.cuiot.dmp.system.infrastructure.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * @author zjb
 * @classname GetSysParamResDto
 * @description 描述
 * @date 2022/5/10
 */
@Data
public class GetSysParamResDto {
    /**
     * 系统设置id
     */
    private String id;

    /**
     * 系统名称
     */
    private String title;

    /**
     * 系统logo
     */
    private Integer logoId;

    /**
     * LOGO图片地址
     */
    private String logoPath;

    /**
     * 编辑时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    /**
     * 编辑者所在组织
     */
    private String updaterPath;

    /**
     * 编辑者
     */
    private String createdBy;

    /**
     * 判断是否为当前用户配置（0：是；1：不是）
     */
    private String self;

}
