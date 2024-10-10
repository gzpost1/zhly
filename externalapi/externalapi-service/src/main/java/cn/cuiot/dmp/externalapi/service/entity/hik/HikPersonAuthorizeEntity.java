package cn.cuiot.dmp.externalapi.service.entity.hik;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 海康-人员信息授权信息
 *
 * @Author: zc
 * @Date: 2024-10-09
 */
@Data
@TableName("tb_haikang_person_authorize")
public class HikPersonAuthorizeEntity {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 人员id
     */
    private Long personId;

    /**
     * 第三方门禁点id
     */
    private String thirdDoorId;
}
