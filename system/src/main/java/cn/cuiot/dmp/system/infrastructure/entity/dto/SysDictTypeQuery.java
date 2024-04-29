package cn.cuiot.dmp.system.infrastructure.entity.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * Created by wuyongchong on 2019/8/28.
 */
@Data
public class SysDictTypeQuery implements Serializable {

  private String dictName;

  private String dictCode;

  private String keyword;

}
