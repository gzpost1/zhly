package cn.cuiot.dmp.lease.vo.export;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.cuiot.dmp.base.application.enums.ContractEnum;
import lombok.Data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static cn.cuiot.dmp.base.application.enums.ContractEnum.AUDIT_WAITING_COMMIT;

/**
 * @author hantingyao
 * @Description
 * @data 2024/9/30 11:15
 */
@Data
public class ContractLeaseExportVo {

    /**
     * 合同名称
     */
    private String name;

    /**
     * 合同编号
     */
    private String contractNo;

    /**
     * 合同状态
     */
    private Integer contractStatus;

    /**
     * 合同信息
     */
    @Excel(name = "合同信息", width = 20, isWrap = true, orderNum = "1")
    private String contractInfo;

    public String getContractInfo() {
        String desc = Objects.requireNonNull(ContractEnum.getEnumByCode(contractStatus)).getDesc();
        return "名称:" + name + "\n编码:" + contractNo + "\n状态:" + desc;
    }

    /**
     * 合同开始日期
     */
    private LocalDate beginDate;

    /**
     * 合同结束日期
     */
    private LocalDate endDate;

    @Excel(name = "合同周期", width = 15, orderNum = "2")
    private String contractCycle;

    public String getContractCycle() {
        return beginDate.toString() + "至" + endDate.toString();
    }

    /**
     * 审核状态
     * 1:审核中待审核 2:审核通过 3:未通过
     */
    private Integer auditStatus;

    @Excel(name = "审核状态", orderNum = "3", replace = {"审核中_1", "审核通过_2", "未通过_3"})
    private String auditStatusName;

    public String getAuditStatusName() {
        if(auditStatus.equals(AUDIT_WAITING_COMMIT.getCode())){
            this.auditStatusName="待提交";
            return "待提交";
        }
        return auditStatus.toString();
    }

    @Excel(name = "跟进人",width = 10,orderNum = "4")
    private String followUpName;

    /**
     * 签订日期
     */
    @Excel(name = "签订日期",width = 10,orderNum = "5",exportFormat = "yyyy-MM-dd")
    private LocalDate cantractDate;

    @Excel(name = "创建人",width = 10,orderNum = "6")
    private String createUserName;

    @Excel(name = "创建时间",width = 15,orderNum = "7",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Excel(name = "更新人",width = 10,orderNum = "8")
    private String updateUserName;

    @Excel(name = "跟新时间",width = 15,orderNum = "9",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
