package cn.cuiot.dmp.content.param.dto;//	模板

import lombok.Data;

import java.util.List;

/**
 * @author hantingyao
 * @Description
 * @data 2024/7/9 19:54
 */

@Data
public class DepartBuildDto {

    /**
     * 组织
     */
    private Long department;

    /**
     * 楼盘
     */
    private Long building;
}
