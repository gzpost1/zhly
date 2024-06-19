package cn.cuiot.dmp.base.infrastructure.dto.req;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 获取客户参数
 *
 * @author: wuyongchong
 * @date: 2024/6/19 10:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CustomerUseReqDto implements Serializable {

    /**
     * 客户ID列表
     */
    private List<Long> customerIdList;
}
