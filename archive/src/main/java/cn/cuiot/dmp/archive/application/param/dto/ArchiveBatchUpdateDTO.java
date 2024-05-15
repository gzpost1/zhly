package cn.cuiot.dmp.archive.application.param.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author liujianyu
 * @description 档案批量修改
 * @since 2024-05-15 16:14
 */
@Data
public class ArchiveBatchUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 所属楼盘id
     */
    @NotNull(message = "请选择所属楼盘")
    private Long loupanId;

    /**
     * 勾选的数据主键id
     */
    @NotEmpty(message = "请选择指定编辑数据")
    private List<Long> ids;
}
