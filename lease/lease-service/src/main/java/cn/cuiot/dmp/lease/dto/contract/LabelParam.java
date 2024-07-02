package cn.cuiot.dmp.lease.dto.contract;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Description
 * @Date 2024/06/17 16:40
 * @Created by Mujun
 */
@Data
public class LabelParam implements Serializable {
    @NotNull(message = "ID不能为空")
    private Long id;
    /**
     * 标签,多个","分割
     */
    @NotNull(message = "标签不能为空")
    private String label;
}
