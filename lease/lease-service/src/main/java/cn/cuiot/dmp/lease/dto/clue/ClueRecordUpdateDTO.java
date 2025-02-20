package cn.cuiot.dmp.lease.dto.clue;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/6/3
 */
@Data
public class ClueRecordUpdateDTO implements Serializable {

    private static final long serialVersionUID = 190287007154868606L;

    /**
     * 线索跟进id
     */
    @NotNull(message = "线索跟进id不能为空")
    private Long id;

    /**
     * 跟进状态（系统配置自定义）
     */
    @NotNull(message = "跟进状态不能为空")
    private Long followStatusId;

    /**
     * 线索表单配置数据
     */
    private JSONObject formData;

    /**
     * 当前线索表单配置快照
     */
    private String formConfigDetail;

}
