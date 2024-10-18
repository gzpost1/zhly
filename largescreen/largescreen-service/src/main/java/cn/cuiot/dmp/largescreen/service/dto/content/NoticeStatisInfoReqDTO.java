package cn.cuiot.dmp.largescreen.service.dto.content;

import cn.cuiot.dmp.largescreen.service.dto.StatisInfoReqDTO;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class NoticeStatisInfoReqDTO extends StatisInfoReqDTO {

    /**
     * 区分是管理端，还是移动端
     */
    @NotNull(message = "类别不能为空")
    private Integer type;

}
