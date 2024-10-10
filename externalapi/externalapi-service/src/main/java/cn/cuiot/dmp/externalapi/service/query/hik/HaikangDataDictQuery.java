package cn.cuiot.dmp.externalapi.service.query.hik;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 海康数据字典查询参数
 * @author: wuyongchong
 * @date: 2024/10/10 14:04
 */
@Data
public class HaikangDataDictQuery implements Serializable {

    @NotBlank(message = "字典类型编码不能为空")
    private String dictTypeCode;

    private String dataModel;

}
