package cn.cuiot.dmp.externalapi.service.vo.hik;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 人员授权时效 VO
 *
 * @Author: zc
 * @Date: 2024-10-10
 */
@Data
public class HikPersonAuthorizeValidityVO {

    /**
     * 权限有效期类型（0:长期有效；1:自定义有效期）
     */
    private Byte validityType;

    /**
     * 自定义有效期-开始时间
     */
    private LocalDateTime beginTime;

    /**
     * 自定义有效期-结束时间
     */
    private LocalDateTime endTime;
}
