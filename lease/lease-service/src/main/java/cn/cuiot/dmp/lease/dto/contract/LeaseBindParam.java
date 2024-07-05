package cn.cuiot.dmp.lease.dto.contract;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Description 审核入参
 * @Date 2024/06/17 16:40
 * @Created by Mujun
 */
@Data
public class LeaseBindParam extends PageQuery implements Serializable {
    @NotNull(message = "ID不能为空")
    private Long id;



}
