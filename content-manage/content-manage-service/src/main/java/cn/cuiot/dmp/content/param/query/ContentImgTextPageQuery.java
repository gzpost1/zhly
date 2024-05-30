package cn.cuiot.dmp.content.param.query;//	模板

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/29 15:37
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ContentImgTextPageQuery extends PageQuery {

    /**
     * 企业ID
     */
    private Long companyId;
}
