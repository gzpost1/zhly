package cn.cuiot.dmp.externalapi.service.vo.hik;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 用户信息分页VO
 *
 * @Author: zc
 * @Date: 2024-10-10
 */
@Data
public class HikPersonPageVO {

    /**
     * 人员名称，1~32个字符；不能包含特殊字符
     */
    @Excel(name = "人员姓名", orderNum = "0", width = 20)
    private String personName;

    /**
     * 人员编码
     */
    @Excel(name = "人员编码", orderNum = "1", width = 20)
    private Long id;

    /**
     * 性别，1：男；2：女；0：未知
     */
    @Excel(name = "性别", orderNum = "2", replace = {"男_1", "女_2", "未知_0"}, width = 10)
    private String gender;

    /**
     * 所属组织标识，必须是已存在组织
     */
    private String orgIndexCode;

    /**
     * 人员所属组织名称
     */
    @Excel(name = "所属组织", orderNum = "3", width = 20)
    private String orgName;

    /**
     * 手机号，1-20位数字
     */
    @Excel(name = "手机号", orderNum = "4", width = 20)
    private String phoneNo;

    /**
     * 工号，1-32个字符
     */
    @Excel(name = "工号", orderNum = "5", width = 20)
    private String jobNo;

    /**
     * 人员照片
     */
    @Excel(name = "人员照片", orderNum = "6", type = 2, width = 20)
    private String faceData;

    /**
     * 人员照片状态（0:未添加，1:已添加）
     */
    @Excel(name = "照片状态", orderNum = "7", replace = {"未添加_0", "已添加_1"}, width = 10)
    private Byte faceDataStatus;

    /**
     * 权限配置状态（0:未配置，1:已配置）
     */
    @Excel(name = "权限状态", orderNum = "8", replace = {"未配置_0", "已配置_1"}, width = 10)
    private Byte authorizeStatus;

    /**
     * 创建时间
     */
    @Excel(name = "创建时间", orderNum = "9", exportFormat = "yyyy-MM-dd HH:mm:ss", width = 20)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @Excel(name = "更新时间", orderNum = "10", exportFormat = "yyyy-MM-dd HH:mm:ss", width = 20)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
