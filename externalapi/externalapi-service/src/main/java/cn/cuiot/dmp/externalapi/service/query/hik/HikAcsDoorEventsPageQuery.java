package cn.cuiot.dmp.externalapi.service.query.hik;

import cn.cuiot.dmp.common.bean.PageQuery;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 门禁点事件分页查询query
 *
 * @Author: zc
 * @Date: 2024-10-11
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HikAcsDoorEventsPageQuery extends PageQuery {

    /**
     * id
     */
    private Long id;

    /**
     * 人员姓名
     */
    private String personName;

    /**
     * 人员唯一编码
     */
    private String personId;

    /**
     * 工号
     */
    private String jobNo;

    /**
     * 卡号
     */
    private String cardNo;

    /**
     * 人员所属组织编码
     */
    private String orgIndexCode;

    /**
     * 门禁点编码
     */
    private String doorIndexCode;

    /**
     * 控制器设备唯一标识
     */
    private String devIndexCode;

    /**
     * 门禁点所在区域编码
     */
    private String doorRegionIndexCode;

    /**
     * 事件类型
     */
    private Integer eventType;

    /**
     * 进出类型（1:进; 0:出; -1:未知）
     */
    private Integer inAndOutType;

    /**
     * 是否存在抓拍图片（0否；1是）
     */
    private Byte isExistPicUri;

    /**
     * 事件开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime eventBeginTime;

    /**
     * 事件结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime eventEndTime;
}
