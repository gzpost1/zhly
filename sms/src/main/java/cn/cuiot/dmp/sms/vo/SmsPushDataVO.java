package cn.cuiot.dmp.sms.vo;

import cn.cuiot.dmp.common.constant.ExternalapiResBaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 第三方推送返回
 *
 * @Author: zc
 * @Date: 2024-09-23
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SmsPushDataVO extends ExternalapiResBaseDTO {

    /**
     * 错误代码：0 成功，其他
     */
    private Integer code;

    /**
     * 错误描述：ok 成功，其他
     */
    private String msg;
}
