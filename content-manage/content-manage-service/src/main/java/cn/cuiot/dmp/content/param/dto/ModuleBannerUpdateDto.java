package cn.cuiot.dmp.content.param.dto;//	模板

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author hantingyao
 * @Description
 * @data 2024/6/4 14:55
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ModuleBannerUpdateDto extends ModuleBannerCreateDto {

    private Long id;
}
