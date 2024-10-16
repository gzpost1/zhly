package cn.cuiot.dmp.externalapi.service.vo.park;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author pengjian
 * @create 2024/10/16 9:34
 */
@Data
public class PersonManagementVo {

    @Excel(name = "人员ID", orderNum = "1", width = 20)
    private Long id;
    /**
     * 手机号
     */
    @Excel(name = "手机号", orderNum = "0", width = 20)
    private String phone;

    /**
     * url
     */
    @Excel(name = "人员照片", orderNum = "2", width = 20)
    private String url;


    @Excel(name = "人员名称", orderNum = "3", width = 20)
    private String name;

    /**
     * 人员分组名称
     */
    @Excel(name = "人员分组", orderNum = "4", width = 20)
    private String personGroupName;

    /**
     * 身份证号
     */
    @Excel(name = "身份证号", orderNum = "5", width = 20)
    private String idCardNo;

    /**
     * 卡号
     */
    @Excel(name = "卡号", orderNum = "6", width = 20)
    private String cardNo;


    /**
     * 备注
     */
    @Excel(name = "备注", orderNum = "7", width = 20)
    private String tag;



    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "录入时间",orderNum = "8",  width = 20,exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    /**
     * 创建人名称
     */
    @Excel(name = "创建人", orderNum = "9", width = 20)
    private String createUserName;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "编辑时间",orderNum = "10",  width = 20,exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 更新人
     */
    @Excel(name = "最后编辑人", orderNum = "11", width = 20)
    private String updateName;

}
