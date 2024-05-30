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
public class SysDictTypeParam implements Serializable {

  private Long dictId;

  /**
   * 字典名称
   */
  @NotBlank(message = "字典名称不能为空")
  @Length(max = 100, message = "字典名称长度不能大于100个字符")
  private String dictName;

  /**
   * 字典代码
   */
  @NotBlank(message = "字典代码")
  @Length(max = 100, message = "字典代码长度不能大于100个字符")
  private String dictCode;

  /**
   * 排序
   */
  @NotNull(message = "排序值不能为空")
  private Integer sort;

  /**
   * 备注
   */
  @Length(max = 200, message = "备注长度不能大于200个字符")
  private String remark;

}
