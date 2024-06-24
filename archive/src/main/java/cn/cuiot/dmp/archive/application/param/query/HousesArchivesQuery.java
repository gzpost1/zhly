package cn.cuiot.dmp.archive.application.param.query;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author liujianyu
 * @description 房屋查询条件
 * @since 2024-05-15 10:37
 */
@Data
public class HousesArchivesQuery extends PageQuery {

    /**
     * 所属楼盘id
     */
    @NotNull(message = "请选择所属楼盘")
    private Long loupanId;

    /**
     * 房屋编码, 产权单位
     */
    private String codeAndOwnershipUnit;

    /**
     * 房屋户型
     */
    private Long houseType;

    /**
     * 朝向
     */
    private Long orientation;

    /**
     * 物业业态
     */
    private Long propertyType;

    /**
     * 状态
     */
    private Long status;

    /**
     * 产权属性
     */
    private Long ownershipAttribute;
    /**
     * 房号
     */
    private String roomNum;
    /**
     * 房屋编码
     */
    private String code;

    /**
     * 房屋名称
     */
    private String name;

}
