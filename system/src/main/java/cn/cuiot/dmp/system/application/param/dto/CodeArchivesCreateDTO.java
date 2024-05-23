package cn.cuiot.dmp.system.application.param.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/5/20
 */
@Data
public class CodeArchivesCreateDTO implements Serializable {

    private static final long serialVersionUID = 7682700540459733686L;

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 编码类型
     */
    @NotNull(message = "编码类型不能为空")
    private Byte codeType;

    /**
     * 生成个数
     */
    @NotNull(message = "生成个数不能为空")
    private Integer generateNum;

}
