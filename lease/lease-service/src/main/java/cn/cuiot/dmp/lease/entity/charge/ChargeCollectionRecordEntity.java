package cn.cuiot.dmp.lease.entity.charge;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 收费管理-催款记录
 *
 * @author zc
 */
@Data
@Document("tb_charge_collection_record")
public class ChargeCollectionRecordEntity {
    /**
     * id
     */
    @TableId("id")
    private Long id;

    /**
     * 企业ID
     */
    @TableField(value = "company_id")
    private Long companyId;

    /**
     * 客户ID
     */
    @TableField(value = "customer_user_id")
    private Long customerUserId;

    /**
     * 通知渠道（1:短信，2:微信）
     */
    @TableField(value = "channel")
    private Byte channel;

    /**
     * 催缴类型（1:手动催缴，2:计划催缴）
     */
    @TableField(value = "`type`")
    private Byte type;

    /**
     * 催款时间
     */
    @TableField(value = "date")
    private Date date;

    /**
     * 创建人
     */
    @TableField(value = "create_user")
    private Long createUser;

    public static final String MONGODB_COMPANY_ID = "companyId";
    public static final String MONGODB_CUSTOMER_USER_ID = "customerUserId";
    public static final String MONGODB_DATE = "date";
}