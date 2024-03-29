package cn.cuiot.dmp.system.infrastructure.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * @author cds
 * 敏感词
 */
@Data
public class SensitivityEntity implements Serializable {
    /**
     * 主键自增id
     */
    private Long sensitiveId;

    /**
     * 用户id
     */
    private String sensitiveWord;

    /**
     * 敏感词类型
     */
    private int type;
}
