package cn.cuiot.dmp.lease.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.DecimalFormat;
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
    @Excel(name = "客户名称", orderNum = "0", width = 20)
    private String customerUserName;

    /**
     * 客户手机号
     */
    @Excel(name = "客户手机号", orderNum = "1", width = 20)
    private String customerUserPhone;

    /**
     * 欠费金额
     */

    private Integer amount;
    @Excel(name = "欠费金额", orderNum = "2", width = 20)
    private  String amountStr;

    /**
     * 上次催款时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "上次催款时间",orderNum = "3",  width = 20,exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date lastNoticeTime;

    /**
     * 累计催款次数
     */
    @Excel(name = "累计催款次数", orderNum = "4", width = 20)
    private Integer totalNoticeNum = 0;

    public String getAmountStr(){
        Double num = amount / 100.0;

        DecimalFormat decimalFormat = new DecimalFormat("#.00"); //定义格式，小数点后两位

        String formattedAmount = decimalFormat.format(num);
        if(formattedAmount.startsWith(".")){
            return "0.00";
        }
        return formattedAmount;
    }
}