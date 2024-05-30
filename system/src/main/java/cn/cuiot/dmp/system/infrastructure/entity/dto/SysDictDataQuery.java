package cn.cuiot.dmp.system.infrastructure.entity.dto;

import cn.cuiot.dmp.common.bean.PageQuery;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Created by wuyongchong on 2019/8/28.
 */
@Data
public class SysDictDataQuery extends PageQuery {

  @NotBlank(message = "dictId参数不能为空")
  private String dictId;

  private String dataName;
}
