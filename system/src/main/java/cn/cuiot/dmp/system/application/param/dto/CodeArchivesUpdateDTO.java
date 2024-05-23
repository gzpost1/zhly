package cn.cuiot.dmp.system.application.param.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/5/20
 */
@Data
public class CodeArchivesUpdateDTO implements Serializable {

    private static final long serialVersionUID = -4633218677702402607L;

    /**
     * id
     */
    @NotNull(message = "二维码档案id不能为空")
    private Long id;

    /**
     * 描述
     */
    private String desc;

    /**
     * 档案类型(1:楼盘,2:房屋,3:空间,4:车位,5:设备)
     */
    private Byte archiveType;

}
