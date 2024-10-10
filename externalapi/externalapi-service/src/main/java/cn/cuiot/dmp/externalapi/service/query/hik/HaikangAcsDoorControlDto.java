package cn.cuiot.dmp.externalapi.service.query.hik;

import cn.cuiot.dmp.common.bean.PageQuery;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * 门禁点查询参数
 *
 * @author: wuyongchong
 * @date: 2024/10/10 14:18
 */
@Data
public class HaikangAcsDoorControlDto implements Serializable {

    /**
     * 企业ID-前端不用管
     */
    private Long companyId;

    /**
     * 反馈类型：0-常开 1-门闭 2-门开 3-常闭
     */
    @NotBlank(message = "反馈类型不能为空")
    private String controlType;

    /**
     * 资源编码
     */
    @NotEmpty(message = "请先选择资源")
    private List<String> indexCodes;
}
