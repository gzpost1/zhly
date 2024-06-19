package cn.cuiot.dmp.content.dal.entity;//	模板

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/29 17:19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_content_data_relevance")
public class ContentDataRelevance {

    @TableId("id")
    private Long id;

    /**
     * 数据id
     */
    private Long dataId;

    /**
     * 数据类型
     */
    private Byte dataType;

    /**
     * 组织id
     */
    private Long departmentId;

    /**
     * 楼盘id
     */
    private Long buildId;
}
