package cn.cuiot.dmp.lease.dto.price;

import cn.cuiot.dmp.common.bean.PageQuery;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author caorui
 * @date 2024/6/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PriceManagePageQueryDTO extends PageQuery {

    private static final long serialVersionUID = 6572509826847582204L;

    /**
     * 定价单编码
     */
    private Long id;

    /**
     * 定价单名称
     */
    private String name;

    /**
     * 房屋编码
     */
    private Long houseId;

    /**
     * 定价单类别（系统配置自定义）
     */
    private Long categoryId;

    /**
     * 定价单类型（系统配置自定义）
     */
    private Long typeId;

    /**
     * 定价开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date priceBeginTime;

    /**
     * 定价结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date priceEndTime;

    /**
     * 执行开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date executeBeginTime;

    /**
     * 执行结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date executeEndTime;

    /**
     * 状态(1:草稿,2:审核中,3:审核通过,4:审核不通过,5:已执行,6:已作废)
     */
    private Byte status;

}
