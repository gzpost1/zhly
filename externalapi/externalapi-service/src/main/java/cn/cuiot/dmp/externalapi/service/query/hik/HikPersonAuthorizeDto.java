package cn.cuiot.dmp.externalapi.service.query.hik;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 人员权限配置
 *
 * @Author: zc
 * @Date: 2024-10-10
 */
@Data
public class HikPersonAuthorizeDto {

    /**
     * id
     */
    @NotNull(message = "id不能为空")
    private Long id;

    /**
     * 权限有效期类型（0:长期有效；1:自定义有效期）
     */
    @NotNull(message = "权限有效期类型不能为空")
    private Byte validityType;

    /**
     * 自定义有效期-开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime beginTime;

    /**
     * 自定义有效期-结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;

    /**
     * 第三方门禁点id
     */
    private List<String> thirdDoorIdList;
}
