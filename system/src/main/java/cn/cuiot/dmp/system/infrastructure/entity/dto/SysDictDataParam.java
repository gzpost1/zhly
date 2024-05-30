package cn.cuiot.dmp.system.infrastructure.entity.dto;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * Created by wuyongchong on 2019/8/28.
 */
@Data
public class SysDictDataParam implements Serializable {

  private Long dataId;

  @NotNull(message = "所属字典dictId不能为空")
  private Long dictId;

  /**
   * 字典名称
   */
  @NotBlank(message = "名称不能为空")
  @Length(max = 100, message = "名称长度不能大于100个字符")
  private String dataName;

  /**
   * 字典代码
   */
  @NotBlank(message = "字典值")
  @Length(max = 100, message = "字典值长度不能大于100个字符")
  private String dataValue;

  /**
   * 排序
   */
  @NotNull(message = "排序值不能为空")
  private Integer sort;

  /**
   * 状态(0:禁用,1:正常)
   */
  @NotNull(message = "状态值不能为空")
  private Byte status;

  /**
   * 备注
   */
  @Length(max = 200, message = "备注长度不能大于200个字符")
  private String remark;
}
