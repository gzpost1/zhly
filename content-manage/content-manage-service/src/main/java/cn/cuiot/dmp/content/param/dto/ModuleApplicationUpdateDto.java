package cn.cuiot.dmp.content.param.dto;//	模板

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author hantingyao
 * @Description
 * @data 2024/6/4 19:00
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ModuleApplicationUpdateDto extends ModuleApplicationCreateDto {

    private Long id;
}
