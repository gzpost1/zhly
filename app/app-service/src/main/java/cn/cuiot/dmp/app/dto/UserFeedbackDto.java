package cn.cuiot.dmp.app.dto;

import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

/**
 * 用户反馈提交参数
 *
 * @author wuyongchong
 * @since 2024-06-14
 */
@Getter
@Setter
public class UserFeedbackDto implements Serializable {

    /**
     * 楼盘ID
     */
    @NotNull(message = "楼盘ID不能为空")
    private Long buildingId;

    /**
     * 反馈内容
     */
    @NotBlank(message = "请输入您的意见或建议")
    @Length(max = 500, message = "反馈内容限500字")
    private String feedbackContent;

    /**
     * 图片
     */
    private List<String> images;

}
