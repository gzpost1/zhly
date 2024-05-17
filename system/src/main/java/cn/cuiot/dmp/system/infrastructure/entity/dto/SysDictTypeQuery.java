package cn.cuiot.dmp.system.infrastructure.entity.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * 字典类型查询参数
 * Created by wuyongchong on 2019/8/28.
 */
@Data
public class SysDictTypeQuery implements Serializable {

  /**
   * 名称
   */
  private String dictName;

  /**
   * 类型编码
   */
  private String dictCode;

  /**
   * 关键字
   */
  private String keyword;

}
