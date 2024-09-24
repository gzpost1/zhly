package cn.cuiot.dmp.sms.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 第三方推送返回
 *
 * @Author: zc
 * @Date: 2024-09-23
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SmsPushDataVO {

    /**
     * 错误代码：0 成功，其他
     */
    private Integer code;

    /**
     * 错误描述：ok 成功，其他
     */
    private String msg;
}
