package cn.cuiot.dmp.content.dal.entity;//	模板

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author hantingyao
 * @Description
 * @data 2024/6/3 10:26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "tb_content_img_text_type")
public class ImgTextType extends YjBaseEntity {

    @TableId("id")
    private Long id;

    /**
     * 类型名
     */
    private String name;

    /**
     * 企业ID
     */
    private Long companyId;
}
