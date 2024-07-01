package cn.cuiot.dmp.lease.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 收费管理-催缴管理Vo
 *
 * @Author: zc
 * @Date: 2024-06-25
 */
@Data
public class ChargeCollectionManageVo {

    /**
     * 客户id
     */
    private Long customerUserId;

    /**
     * 客户名称
     */
    private String customerUserName;

    /**
     * 客户手机号
     */
    private String customerUserPhone;

    /**
     * 欠费金额
     */
    private Integer amount;

    /**
     * 上次催款时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastNoticeTime;

    /**
     * 累计催款次数
     */
    private Integer totalNoticeNum = 0;
}