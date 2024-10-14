package cn.cuiot.dmp.lease.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 收费管理-通知单分页vo
 *
 * @author zc
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChargeNoticePageVo extends ChargeNoticeVo {
    /**
     * 操作人
     */
    @Excel(name = "操作人", orderNum = "6", width = 20)
    private String operatorName;

    /**
     * 创建人id
     */
    private Long createUser;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "创建时间",orderNum = "5",  width = 20,exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 收费项目名称列表
     */
    @Excel(name = "收费项目", orderNum = "4", width = 20)
    private String chargeItemsName;

    /**
     * 楼盘名称列表
     */
    @Excel(name = "通知对象", orderNum = "1", width = 20)
    private String buildingsName;
}