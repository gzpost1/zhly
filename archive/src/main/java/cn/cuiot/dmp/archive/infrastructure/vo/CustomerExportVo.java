package cn.cuiot.dmp.archive.infrastructure.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.cuiot.dmp.archive.infrastructure.entity.CustomerEntity;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * 客户信息导出
 * @author: wuyongchong
 * @date: 2024/6/12 11:22
 */
@Data
public class CustomerExportVo implements Serializable{

    @Excel(name = "客户名称", orderNum = "0", width = 20)
    private String customerName;

    @Excel(name = "客户ID", orderNum = "1", width = 20)
    private String customerId;

    @Excel(name = "客户类型", orderNum = "2", width = 20)
    private String customerTypeName;

    @Excel(name = "公司性质", orderNum = "3", width = 20)
    private String companyNatureName;

    @Excel(name = "所属行业", orderNum = "4", width = 20)
    private String companyIndustryName;

    @Excel(name = "证件类型", orderNum = "5", width = 20)
    private String certificateTypeName;

    @Excel(name = "证件号码", orderNum = "6", width = 20)
    private String certificateCdoe;

    @Excel(name = "联系人", orderNum = "7", width = 20)
    private String contactName;

    @Excel(name = "联系人手机号", orderNum = "8", width = 20)
    private String contactPhone;

    @Excel(name = "邮箱", orderNum = "9", width = 20)
    private String email;

    @Excel(name = "性别", orderNum = "10", width = 20)
    private String sexName;

    @Excel(name = "客户分类", orderNum = "11", width = 20)
    private String customerCateName;

    @Excel(name = "客户级别", orderNum = "12", width = 20)
    private String customerLevelName;

    @Excel(name = "信用等级", orderNum = "13", width = 20)
    private String creditLevelName;

}
