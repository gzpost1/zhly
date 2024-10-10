package cn.cuiot.dmp.externalapi.service.entity.hik;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 海康数据字典
 * </p>
 *
 * @author wuyongchong
 * @since 2024-10-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_haikang_data_dict")
public class HaikangDataDictEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId("id")
    private Long id;


    /**
     * 字典类型编码
     */
    private String dictTypeCode;


    /**
     * 字典类型名称
     */
    private String dictTypeName;


    /**
     * 字典编码
     */
    private String dataCode;


    /**
     * 字典名称
     */
    private String dataName;


    /**
     * 字典型号/子类型/子分类
     */
    private String dataModel;


    /**
     * 排序值
     */
    private Integer sort;


    /**
     * 状态(0:禁用,1:正常)
     */
    private Byte status;


    /**
     * 备注
     */
    private String remark;


}
