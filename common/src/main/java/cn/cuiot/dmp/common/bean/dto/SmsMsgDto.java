package cn.cuiot.dmp.common.bean.dto;//	模板

import lombok.Data;

import java.util.List;

/**
 * 短信消息
 *
 * @author hantingyao
 * @Description
 * @data 2024/6/14 10:32
 */
@Data
public class SmsMsgDto {

    /**
     * 手机号
     */
    java.util.List<String> telNumbers;

    /**
     * 参数
     */
    List<String> params;

    /**
     * 模板id
     */
    Integer templateId;

}
