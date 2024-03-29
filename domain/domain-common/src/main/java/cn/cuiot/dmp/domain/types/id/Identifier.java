package cn.cuiot.dmp.domain.types.id;

import java.io.Serializable;

/**
 * @Description ID类型DP的Marker接口
 * DP：Domain Primitive 是一个在特定领域里，拥有精准定义的、可自我验证的、拥有行为的 Value Object
 * @Author yth
 * @Date 2023/8/28 09:17
 * @Version V1.0
 */
public interface Identifier extends Serializable {
    Serializable getValue();
}
