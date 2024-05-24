package cn.cuiot.dmp.archive.application.param.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/5/23
 */
@Data
public class ArchivesApiQueryDTO implements Serializable {

    private static final long serialVersionUID = 1663821908925189742L;

    /**
     * 档案id
     */
    @NotNull(message = "档案id不能为空")
    private Long archiveId;

    /**
     * 档案类型
     */
    @NotNull(message = "档案类型不能为空")
    private Byte archiveType;

}
