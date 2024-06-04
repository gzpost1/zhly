package cn.cuiot.dmp.base.infrastructure.stream.messaging;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息包装类
 *
 * @author: wuyongchong
 * @date: 2024/6/4 15:28
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class SimpleMsg<T> implements Serializable {

    /**
     * 操作标签
     */
    private String operateTag;

    /**
     * 数据
     */
    private T data;
}
