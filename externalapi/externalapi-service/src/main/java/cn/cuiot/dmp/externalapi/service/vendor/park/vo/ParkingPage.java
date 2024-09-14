package cn.cuiot.dmp.externalapi.service.vendor.park.vo;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author pengjian
 * @create 2024/9/3 15:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingPage {
    /**
     * 页数：必须大于等于1
     */
    private int pageIndex;
    /**
     * 每页显示的记录数
     */
    private int pageSize;
    /**
     * 总记录数
     */
    private int totalCount;
    /**
     * 车场列表
     */
    private List<ParkingLotVO> parkingList;



}
