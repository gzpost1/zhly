package cn.cuiot.dmp.content.dal.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * @author hantingyao
 * @Description 小程序配置，模块管理
 * @data 2024/6/3 16:29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_content_module")
@Accessors(chain = true)
public class ContentModule implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Long id;

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 系统模块
     */
    private String systemModule;

    /**
     * 模块类型
     */
    private String moduleType;

    /**
     * 功能模块
     */
    private String functionModule;

    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 是否展示
     */
    private Byte showed;

    /**
     * 排序值
     */
    private Integer sort;


}
