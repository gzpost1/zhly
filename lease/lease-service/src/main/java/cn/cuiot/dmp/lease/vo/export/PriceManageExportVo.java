package cn.cuiot.dmp.lease.vo.export;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author hantingyao
 * @Description
 * @data 2024/10/9 10:50
 */
@Data
public class PriceManageExportVo {

    /**
     * 定价单编码
     */
    @Excel(name = "定价单编码", orderNum = "0")
    private Long id;

    /**
     * 定价单名称
     */
    @Excel(name = "定价单名称", orderNum = "1")
    private String name;

    /**
     * 定价单类别名称（系统配置自定义）
     */
    @Excel(name = "定价单类别", orderNum = "2")
    private String categoryName;

    /**
     * 定价单类型名称（系统配置自定义）
     */
    @Excel(name = "定价单类型", orderNum = "3")
    private String typeName;

    /**
     * 状态(1:草稿,2:审核中,3:审核通过,4:审核不通过,5:已执行,6:已作废)
     */
    private Byte status;

    @Excel(name = "定价单状态", orderNum = "4",replace = {"草稿_1", "审核中_2", "审核通过_3", "审核不通过_4", "已执行_5", "已作废_6"})
    private String statusName;

    public String getStatusName() {
        return status.toString();
    }

    /**
     * 定价日期，格式为yyyy-MM-dd
     */
    @Excel(name = "定价日期", orderNum = "5", exportFormat = "yyyy-MM-dd")
    private Date priceDate;

    /**
     * 执行日期，格式为yyyy-MM-dd
     */
    @Excel(name = "执行日期", orderNum = "6", exportFormat = "yyyy-MM-dd")
    private Date executeDate;

    /**
     * 定价资源数
     */
    @Excel(name = "定价资源数", orderNum = "7")
    private Integer priceResourceNum;

    /**
     * 定价人名称
     */
    @Excel(name = "定价人", orderNum = "8")
    private String priceUserName;

    /**
     * 创建时间
     */
    @Excel(name = "创建时间", orderNum = "9", exportFormat = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    /**
     * 更新者。取值：{userKey}：Portal用户更新的；{appKey}：API更新的。
     */
    @Excel(name = "更新人", orderNum = "10")
    private String updatedName;

    /**
     * 更新时间
     */
    @Excel(name = "更新时间", orderNum = "11", exportFormat = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedOn;
}
