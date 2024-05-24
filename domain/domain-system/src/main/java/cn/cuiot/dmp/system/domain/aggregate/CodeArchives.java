package cn.cuiot.dmp.system.domain.aggregate;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author caorui
 * @date 2024/5/20
 */
@Data
public class CodeArchives implements Serializable {

    private static final long serialVersionUID = 3276272466145299014L;

    /**
     * id
     */
    private Long id;

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 档案ID
     */
    private Long archiveId;

    /**
     * 档案类型(1:楼盘,2:房屋,3:空间,4:车位,5:设备)
     */
    private Byte archiveType;

    /**
     * 描述
     */
    private String archiveDesc;

    /**
     * 码类型（1:二维码）
     */
    private Byte codeType;

    /**
     * 停启用状态（0停用，1启用）生成数量
     */
    private Byte status;

    /**
     * 生成个数
     */
    private Integer generateNum;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
