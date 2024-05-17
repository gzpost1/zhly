package cn.cuiot.dmp.system.infrastructure.entity;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author caorui
 * @date 2024/5/16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "tb_code_archives", autoResultMap = true)
public class CodeArchivesEntity extends YjBaseEntity {

    private static final long serialVersionUID = 8918281994234218807L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 编码
     */
    private String code;

    /**
     * 档案ID
     */
    private Long archiveId;

    /**
     * 档案类型(1:楼盘,2:房屋,3:空间,4:车位,5:设备)
     * @see cn.cuiot.dmp.system.application.enums.ArchiveTypeEnum
     */
    private Byte archiveType;

    /**
     * 描述
     */
    private String desc;

    /**
     * 码类型（1:二维码）
     */
    private Byte codeType;

    /**
     * 停启用状态（0停用，1启用）
     */
    private Byte status;

}
