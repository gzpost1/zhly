package cn.cuiot.dmp.lease.dto.clue;

import cn.cuiot.dmp.common.bean.PageQuery;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author caorui
 * @date 2024/6/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CluePageQueryDTO extends PageQuery {

    private static final long serialVersionUID = 6310314043408388182L;

    /**
     * 线索名称
     */
    private String name;

    /**
     * 部门ID
     */
    private Long departmentId;

    /**
     * 楼盘ID
     */
    private Long buildingId;

    /**
     * 线索来源（系统配置自定义）
     */
    private Long sourceId;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

}
