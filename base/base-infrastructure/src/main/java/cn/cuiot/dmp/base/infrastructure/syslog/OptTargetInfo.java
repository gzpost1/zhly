package cn.cuiot.dmp.base.infrastructure.syslog;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 操作对象
 *
 * @author: wuyongchong
 * @date: 2024/6/17 14:09
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class OptTargetInfo implements Serializable {

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 操作名称
     */
    private String operationName;

    /**
     * 对象类型名称
     */
    private String name;

    /**
     * 对象数据内容
     */
    private List<OptTargetData> targetDatas;

}
