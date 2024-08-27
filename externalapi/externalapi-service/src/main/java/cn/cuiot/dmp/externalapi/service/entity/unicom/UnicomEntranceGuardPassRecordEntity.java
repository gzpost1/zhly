package cn.cuiot.dmp.externalapi.service.entity.unicom;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 联通格物门禁-通行记录(UnicomEntranceGuardPassRecordEntity)实体类
 *
 * @author Gxp
 * @since 2024-08-22 16:04:56
 */
@Data
@TableName("tb_unicom_entrance_guard_pass_record")
public class UnicomEntranceGuardPassRecordEntity implements Serializable {
    private static final long serialVersionUID = 717140475955161819L;
    /**
     * id
     */
    @TableId("id")
    private Long id;
    /**
     * 人员名称
     */
    private String personName;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 所属组织
     */
    private String organization;
    /**
     * 所属场所
     */
    private String place;
    /**
     * 人员身份
     */
    private String position;


}
