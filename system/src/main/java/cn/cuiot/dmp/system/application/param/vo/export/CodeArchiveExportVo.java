package cn.cuiot.dmp.system.application.param.vo.export;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hantingyao
 * @date 2024/5/20
 */
@Data
public class CodeArchiveExportVo implements Serializable {

    private static final long serialVersionUID = -8915482810511981655L;

    /**
     * id
     */
    @Excel(name = "二维码编码", orderNum = "1", width = 20)
    private Long id;


    /**
     * 档案名称
     */
    @Excel(name = "档案名称", orderNum = "2", width = 20)
    private String archiveName;

    /**
     * 描述
     */
    @Excel(name = "描述", orderNum = "4", width = 50)
    private String archiveDesc;

    /**
     * 码类型（1:二维码）
     */
    @Excel(name = "码类型", orderNum = "3", width = 5, replace = {"二维码_1"})
    private String codeType;

    /**
     * 停启用状态（0停用，1启用）生成数量
     */
    private Byte status;

    @Excel(name = "停启用状态", orderNum = "5", width = 5, replace = {"启用_1", "停用_0"})
    private String statusName;

    public String getStatusBame() {
        return status.toString();
    }

    /**
     * 创建时间
     */
    @Excel(name = "生成时间", orderNum = "6", width = 15, exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
