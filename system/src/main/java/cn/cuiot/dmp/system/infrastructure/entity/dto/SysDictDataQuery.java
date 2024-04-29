package cn.cuiot.dmp.system.infrastructure.entity.dto;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Created by wuyongchong on 2019/8/28.
 */
@Data
public class SysDictDataQuery implements Serializable {

  @NotBlank(message = "dictId参数不能为空")
  private String dictId;

  private String dataName;
}
