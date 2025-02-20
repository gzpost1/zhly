package cn.cuiot.dmp.externalapi.service.vo.park;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.cuiot.dmp.common.bean.PageQuery;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author pengjian
 * @create 2024/7/18 20:37
 */
@Data
public class PortraitInputVo extends PageQuery {

    /**
     * id
     */
    private Long id;

    /**
     * 名字
     */
    @Excel(name = "人员名称", orderNum = "0", width = 20)
    private String name;
    /**
     * url
     */
    @Excel(name = "人员照片", orderNum = "1", width = 20)
    private String url;

    /**
     * 手机号
     */
    @Excel(name = "手机号", orderNum = "3", width = 20)
    private String phone;

    /**
     * 卡号
     */
    @Excel(name = "卡号", orderNum = "4", width = 20)
    private String cardNo;

    /**
     * 身份证号
     */
    @Excel(name = "身份证号", orderNum = "5", width = 20)
    private String idCardNo;

    /**
     * 密码
     */

    private String password;

    /**
     * 备注
     */
    @Excel(name = "备注", orderNum = "6", width = 20)
    private String tag;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "录入时间",orderNum = "8",  width = 20,exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 创建人
     */

    private Long createUser;

    /**
     * 创建人名称
     */
    @Excel(name = "录入人", orderNum = "7", width = 20)
    private String createUserName;

    /**
     * 人员分组id
     */
    private Long personGroupId;

    /**
     * 人员分组名称
     */
    private String personGroupName;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 创建人
     */
    private Long updateUser;
    /**
     * 更新人
     */
    private String updateName;
}
