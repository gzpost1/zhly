package cn.cuiot.dmp.largescreen.service.dto.archive;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author caorui
 * @date 2024/5/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BuildingArchivesPageQuery extends PageQuery {

    private static final long serialVersionUID = -5291447441996967650L;

    /**
     * 企业id
     */
    private Long companyId;

    /**
     * 部门id
     */
    private Long departmentId;

    /**
     * 楼盘id
     */
    private Long id;

    /**
     * 楼盘名称
     */
    private String name;

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 部门id列表
     */
    private List<Long> departmentIdList;

    /**
     * 楼盘id列表
     */
    private List<Long> loupanIds;


    /**
     * 楼盘id列表
     */
    private List<Long> idList;

    /**
     * 区域编码
     */
    private String areaCode;

}
