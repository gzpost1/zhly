package cn.cuiot.dmp.system.infrastructure.entity.dto;

import cn.cuiot.dmp.system.infrastructure.entity.Notice;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * @author shixh
 * @version 1.0
 * @date 2022/8/5 10:29
 */
@Data
public class NoticeDto extends Notice {

    private String userId;
    /**
     * 当前页
     */
    @Range(min = 1, message = "当前页码范围不合法")
    private Integer currentPage;

    /**
     * 分页大小
     */
    @Range(min = 1, max = 200, message = "分页大小范围不合法")
    private Integer pageSize;
}
