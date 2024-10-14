package cn.cuiot.dmp.archive.application.param.vo.export;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.cuiot.dmp.archive.infrastructure.entity.CustomerEntity;
import cn.cuiot.dmp.archive.infrastructure.vo.CustomerHouseVo;
import cn.cuiot.dmp.archive.infrastructure.vo.CustomerMemberVo;
import cn.cuiot.dmp.archive.infrastructure.vo.CustomerVehicleVo;
import lombok.Data;

import java.util.List;

/**
 * 客户信息
 * @author: wuyongchong
 * @date: 2024/6/12 11:22
 */
@Data
public class CustomerExportVo   {

    @Excel(name = "客户ID", orderNum = "1", width = 20)
    private Long id;

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 客户名称
     */
    @Excel(name = "客户名称", orderNum = "2", width = 20)
    private String customerName;

    /**
     * 联系人
     */
    @Excel(name = "联系人", orderNum = "3", width = 20)
    private String contactName;


    /**
     * 联系人手机号
     */
    @Excel(name = "联系人手机号", orderNum = "4", width = 20)
    private String contactPhone;
}
