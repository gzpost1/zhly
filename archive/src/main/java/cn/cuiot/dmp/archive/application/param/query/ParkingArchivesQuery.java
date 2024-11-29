package cn.cuiot.dmp.archive.application.param.query;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author liujianyu
 * @description 车位档案查询条件
 * @since 2024-05-15 10:38
 */
@Data
public class ParkingArchivesQuery extends PageQuery {

    /**
     * 所属楼盘id
     */
    private Long loupanId;

    /**
     * 车位编号
     */
    private String code;

    /**
     * 使用情况（下拉选择自定义配置中数据）
     */
    private Long usageStatus;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 车位类型（下拉选择自定义配置中数据）
     */
    private Long parkingType;

    /**
     * 部门id
     */
    private Long departmentId;

}
