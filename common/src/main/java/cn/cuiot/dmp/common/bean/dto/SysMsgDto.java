package cn.cuiot.dmp.common.bean.dto;//	模板

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 系统消息
 *
 * @author hantingyao
 * @Description
 * @data 2024/6/14 10:26
 */
@Data
@Accessors(chain = true)
public class SysMsgDto extends SysMsgBaseDto {

    /**
     * 接收人（非空）
     */
    private List<Long> acceptors;

}
