package cn.cuiot.dmp.pay.service.service.entity;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * <p>
 * 余额明细变动表
 * </p>
 *
 * @author wuyongchong
 * @since 2023-11-29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tb_house_balance")
public class BalanceEntity extends YjBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 房屋id
     */
    @TableId("house_id")
    private Long houseId;


    /**
     * 余额
     */
    private Integer balance;


    /**
     * 版本号
     */
    private Integer version;
}
