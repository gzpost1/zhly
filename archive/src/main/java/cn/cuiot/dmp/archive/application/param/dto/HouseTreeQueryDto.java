package cn.cuiot.dmp.archive.application.param.dto;

import lombok.Data;

/**
 * @Description 楼盘房屋树查询
 * @Date 2024/6/25 9:45
 * @Created by libo
 */
@Data
public class HouseTreeQueryDto {
    /**
     * 关键字
     */
    private String keyWords;

    /**
     * 是否查询房屋欠款
     */
    private Boolean isSelectHouseArrears = false;

    /**
     * 企业ID
     */
    private Long orgId;

    /**
     * 用户ID
     */
    private Long userId;
}
