package cn.cuiot.dmp.lease.entity.charge;

import cn.cuiot.dmp.base.infrastructure.persistence.handler.ListObjectJsonTypeHandler;
import cn.cuiot.dmp.lease.dto.charge.ChargePayToWechatDetailDto;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@TableName(value = "tb_charge_order", autoResultMap = true, resultMap = "BaseResultMap")
public class TbChargeOrder implements Serializable {
    /**
     * 订单id
     */
    @TableId(value = "order_id", type = IdType.INPUT)
    private Long orderId;

    /**
     * 支付中台返回id
     */
    @TableField(value = "pay_id")
    private Long payId;

    /**
     * 调取详情
     */
    @TableField(value = "order_detail", typeHandler = ListObjectJsonTypeHandler.class)
    private List<ChargePayToWechatDetailDto> orderDetail;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 数据类型 0房屋收费 1房屋押金
     */
    private Byte dataType;

    /**
     * 下单人
     */
    private Long createUser;

    /**
     * 企业ID
     */
    private Long companyId;
}