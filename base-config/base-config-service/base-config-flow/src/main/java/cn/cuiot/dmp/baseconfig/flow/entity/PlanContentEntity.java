package cn.cuiot.dmp.baseconfig.flow.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author pengjian
 * @create 2024/5/15 14:10
 */
@Data
@TableName("tb_plan_content")
public class PlanContentEntity {

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;


    private String content;
}
