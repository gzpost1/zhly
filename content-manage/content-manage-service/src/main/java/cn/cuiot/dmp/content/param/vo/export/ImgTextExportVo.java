package cn.cuiot.dmp.content.param.vo.export;//	模板

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.cuiot.dmp.content.dal.entity.ContentImgTextEntity;
import cn.hutool.core.collection.CollUtil;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/28 15:32
 */
@Data
public class ImgTextExportVo extends ContentImgTextEntity {

    /**
     * 图文标题
     */
    @Excel(name = "图文标题", orderNum = "1", width = 30)
    private String title;

    /**
     * 图文类型
     */
    @Excel(name = "图文类型", orderNum = "2", width = 30)
    private String typeName;

    /**
     * 楼盘
     */
    private List<String> buildingNames;

    /**
     * 楼盘
     */
    @Excel(name = "楼盘", orderNum = "3", width = 30)
    private String building;

    public String getBuilding() {
        if (CollUtil.isEmpty(buildingNames)) {
            return null;
        }
        return String.join(",", buildingNames);
    }

    @Excel(name = "创建人", orderNum = "4")
    private String creatUserName;

    @Excel(name = "创建时间", orderNum = "5", width = 30, exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Excel(name = "更新时间", orderNum = "6", width = 30, exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 审核状态
     */
    private Byte auditStatus;

    /**
     * 审核状态
     */
    @Excel(name = "审核状态", orderNum = "7", width = 3, replace = {"未审核_1", "审核通过_2", "审核不通过_3"})
    private String auditStatusName;

    public String getAuditStatusName() {
        return auditStatus.toString();
    }

    /**
     * 状态
     */
    private Byte status;

    /**
     * 状态
     */
    @Excel(name = "状态", orderNum = "8", width = 3, replace = {"启用_1", "停用_0"})
    private String statusName;

    public String getStatusName() {
        return status.toString();
    }
}
