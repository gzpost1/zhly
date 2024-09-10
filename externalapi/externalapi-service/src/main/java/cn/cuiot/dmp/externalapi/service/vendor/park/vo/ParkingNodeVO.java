package cn.cuiot.dmp.externalapi.service.vendor.park.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;

import java.util.List;

/**
 * @author pengjian
 * @create 2024/9/9 14:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingNodeVO {
    /**
     * 下标
     */
    private Integer pageIndex;

    /**
     * 页数
     */
    private Integer pageSize;

    /**
     * 总记录数
     */
    private Integer totalCount;

    private List<NodeListVO> nodeList;


}
