package cn.cuiot.dmp.common.bean.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * 名称与URL对象DTO
 * @author: wuyongchong
 * @date: 2024/7/10 13:43
 */
@Data
public class NameUrlDto implements Serializable {

    /**
     * 名称
     */
    private String name;
    /**
     * 对象地址
     */
    private String url;
}
