package cn.cuiot.dmp.app.dto;

import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

/**
 * 用户反馈回复参数
 *
 * @author wuyongchong
 * @since 2024-06-14
 */
@Getter
@Setter
public class UserFeedbackReplyDto implements Serializable {

    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空")
    private Long id;

    /**
     * 回复内容
     */
    @Length(max = 500, message = "回复内容限500字")
    @NotBlank(message = "请输入回复内容")
    private String replyContent;
}
