package cn.cuiot.dmp.largescreen.service.dto.content;

import cn.cuiot.dmp.largescreen.service.dto.StatisInfoReqDTO;
import lombok.Data;

@Data
public class NoticeStatisInfoReqDTO extends StatisInfoReqDTO {

    /**
     * 区分是管理端，还是移动端
     */
    private Integer type;

}
