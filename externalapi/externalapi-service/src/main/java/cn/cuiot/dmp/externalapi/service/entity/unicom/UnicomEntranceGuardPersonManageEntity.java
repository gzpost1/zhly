package cn.cuiot.dmp.externalapi.service.entity.unicom;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 联通格物门禁-人员管理(UnicomEntranceGuardPersonManageEntity)实体类
 *
 * @author Gxp
 * @since 2024-08-22 15:47:11
 */
@Data
@TableName("tb_unicom_entrance_guard_person_manage")
public class UnicomEntranceGuardPersonManageEntity implements Serializable {
    private static final long serialVersionUID = 801527900202039486L;
    /**
     * id
     */
    @TableId("id")
    private Long id;
    /**
     * 人员编号
     */
    private String no;
    /**
     * 人员姓名
     */
    private String name;
    /**
     * 性别
     */
    private String gender;
    /**
     * 所属组织
     */
    private String organization;
    /**
     * 所属场所
     */
    private String place;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 人员身份
     */
    private String position;
    /**
     * 人员照片地址
     */
    private String photoUrl;

    
}
