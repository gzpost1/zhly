package cn.cuiot.dmp.message.param;//	模板

import lombok.Data;

import java.util.List;

/**
 * @author hantingyao
 * @Description
 * @data 2024/6/18 9:50
 */
@Data
public class MessagePageQueryReq {

    private List<Long> ids;
}
