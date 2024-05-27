package cn.cuiot.dmp.baseconfig.flow.dto.app;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pengjian
 * @create 2024/5/27 11:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class BaseDto {

    /**
     * key名称
     */
    private String name;

    /**
     * value 名称
     */
    private Integer number;
}
