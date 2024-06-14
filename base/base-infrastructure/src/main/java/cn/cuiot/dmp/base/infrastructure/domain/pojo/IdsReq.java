package cn.cuiot.dmp.base.infrastructure.domain.pojo;//	模板

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/30 16:56
 */
@Data
@Accessors(chain = true)
public class IdsReq implements Serializable {


    @NotEmpty
    private List<Long> ids;
}
