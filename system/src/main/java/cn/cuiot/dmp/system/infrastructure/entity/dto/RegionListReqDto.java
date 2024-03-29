package cn.cuiot.dmp.system.infrastructure.entity.dto;

import cn.cuiot.dmp.common.serialize.LocalDateTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.LocalDateTime;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.Data;

/**
 * @author wqd
 * @classname RegionListReqDto
 * @description
 * @date 2023/1/12
 */
@Data
public class RegionListReqDto {

    @JsonIgnore
    private String orgId;

    @JsonIgnore
    private String userId;

    @JsonIgnore
    private Integer group;

    /**
     * 标签id
     */
    @JsonIgnore
    private Integer labelId;

    private String deptTreePath;

    private String keyword;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime startTime;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime endTime;

    /**
     * 当前页
     */
    @Min(value = 1, message = "当前页需要大于1")
    private Integer currentPage;

    /**
     * 分页参数
     */
    @Max(value = 100, message = "单页最大数据为100")
    private Integer pageSize;

    private Long parkId;
}
