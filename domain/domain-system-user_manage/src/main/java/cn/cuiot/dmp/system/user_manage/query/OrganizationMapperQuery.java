package cn.cuiot.dmp.system.user_manage.query;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

/**
 * @author pengcg
 * @description 条件查询入参
 * @date 2023/9/4
 */
@Data
@Builder
public class OrganizationMapperQuery {

    /**
     * 分页参数，当前页面显示的数据条数
     */
    private String pageSize;

    /**
     * 分页参数，当前页码
     */
    private String pageNo;


    private int deletedFlag;

    /**
     * 开始时间
     */
    private LocalDateTime deleteStartTime;

    /**
     * 结束时间
     */
    private LocalDateTime deleteEndTime;

}
