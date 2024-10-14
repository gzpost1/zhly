package cn.cuiot.dmp.content.param.vo.export;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.hutool.core.collection.CollUtil;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author hantingyao
 * @Description
 * @data 2024/9/29 16:27
 */
@Data
public class NoticeExportVo {

    /**
     * 标题
     */
    @Excel(name = "标题", orderNum = "1", width = 20)
    private String title;

    /**
     * 发布端
     */
    private Byte publishSource;

    /**
     * 发布端
     */
    @Excel(name = "发布端", width = 10, orderNum = "2", replace = {"管理端_1", "客户端_2"})
    private String publishSourceName;

    public String getPublishSourceName() {
        return publishSource.toString();
    }

    private List<String> departmentNames;

    @Excel(name = "发布组织", orderNum = "3")
    private String departmentNamesStr;

    public String getDepartmentNamesStr() {
        if (CollUtil.isEmpty(departmentNames)) {
            return null;
        }
        return String.join(",", departmentNames);
    }

    private List<String> buildingNames;

    @Excel(name = "发布楼盘", orderNum = "4", width = 20)
    private String buildingNamesStr;

    public String getBuildingNamesStr() {
        if (CollUtil.isEmpty(buildingNames)) {
            return null;
        }
        return String.join(",", buildingNames);
    }

    /**
     * 公告类型
     */
    @Excel(name = "公告类型", orderNum = "5")
    private String typeName;

    /**
     * 生效开始时间
     */
    @Excel(name = "生效开始时间", orderNum = "6", exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date effectiveStartTime;

    /**
     * 生效结束时间
     */
    @Excel(name = "生效结束时间", orderNum = "7", exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date effectiveEndTime;

    @Excel(name = "创建人", orderNum = "8", width = 10)
    private String creatUserName;

    @Excel(name = "创建时间", orderNum = "9", width = 15, exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 审核状态
     */
    private Byte auditStatus;

    @Excel(name = "审核状态", orderNum = "10", width = 8, replace = {"未审核_1", "审核通过_2", "审核不通过_3"})
    private String auditStatusName;

    public String getAuditStatusName() {
        return auditStatus.toString();
    }
}
