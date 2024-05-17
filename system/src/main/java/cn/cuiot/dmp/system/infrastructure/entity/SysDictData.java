package cn.cuiot.dmp.system.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * tb_sys_dict_data
 *
 * @author
 */
@Data
@TableName("tb_sys_dict_data")
public class SysDictData implements Serializable {

  /**
   * 主键ID
   */
  @TableId(value = "data_id")
  private Long dataId;

  /**
   * 所属系统域
   */
  private Long domainId;

  /**
   * 字典类型ID
   */
  private Long dictId;

  /**
   * 名称
   */
  private String dataName;

  /**
   * 值
   */
  private String dataValue;

  /**
   * 排序
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

  /**
   * 创建时间
   */
  @TableField(fill = FieldFill.INSERT)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;

  /**
   * 创建人ID
   */
  @TableField(fill = FieldFill.INSERT)
  private Long createUser;

  /**
   * 更新时间
   */
  @TableField(fill = FieldFill.UPDATE)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date updateTime;


  @TableField(fill = FieldFill.UPDATE)
  private Long updateUser;
}