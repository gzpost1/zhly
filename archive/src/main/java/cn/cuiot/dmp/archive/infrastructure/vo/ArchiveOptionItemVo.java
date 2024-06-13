package cn.cuiot.dmp.archive.infrastructure.vo;

import java.io.Serializable;
import lombok.Data;

/**
 * @author: wuyongchong
 * @date: 2024/6/13 11:32
 */
@Data
public class ArchiveOptionItemVo implements Serializable {

    /**
     * 类别
     */
    private Long systemOptionType;

    /**
     * 配置名
     */
    private String configName;

    /**
     * 配置选项ID
     */
    private Long itemId;

    /**
     * 配置选项名称
     */
    private String itemName;
}
