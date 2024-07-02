package cn.cuiot.dmp.common.bean.dto;//	模板

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 系统消息-业务消息
 *
 * @Author: zc
 * @Date: 2024-06-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class SysBusinessMsgDto extends SysMsgBaseDto{

    /**
     * 接收人（非空）
     */
    private Long accepter;
}