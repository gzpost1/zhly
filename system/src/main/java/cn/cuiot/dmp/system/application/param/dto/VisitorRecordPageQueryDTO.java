package cn.cuiot.dmp.system.application.param.dto;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author caorui
 * @date 2024/6/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class VisitorRecordPageQueryDTO extends PageQuery {

    private static final long serialVersionUID = -359366012790485074L;

    /**
     * 创建人id
     */
    private Long creatorId;

}
