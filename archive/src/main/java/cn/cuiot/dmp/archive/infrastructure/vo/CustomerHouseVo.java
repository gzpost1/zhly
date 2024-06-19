package cn.cuiot.dmp.archive.infrastructure.vo;

import cn.cuiot.dmp.archive.infrastructure.entity.CustomerHouseEntity;
import lombok.Data;

/**
 * 客户房屋信息
 * @author: wuyongchong
 * @date: 2024/6/12 14:00
 */
@Data
public class CustomerHouseVo extends CustomerHouseEntity {

    /**
     * 所属楼盘ID
     */
    private Long communityId;

    /**
     * 所属楼盘名称
     */
    private String communityName;

    /**
     * 房屋名称
     */
    private String houseName;

    /**
     * 房屋编码
     */
    private String houseCode;

    /**
     * 房号
     */
    private String roomNum;

    /**
     * 房屋楼层
     */
    private String floorName;

    /**
     * 楼层别名
     */
    private String floorAlias;
}
