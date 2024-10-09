package cn.cuiot.dmp.externalapi.service.vendor.hik.bean.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 查询门禁点事件v2
 *
 * @Author: zc
 * @Date: 2024-10-09
 */
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class HikDoorEventsReq extends HikBaseReq {

    /**
     * 门禁点唯一标识数组，最大支持10个门禁点
     */
    private List<String> doorIndexCodes;

    /**
     * 门禁点名称，支持模糊查询
     */
    private String doorName;

    /**
     * 读卡器唯一标识数组，最大支持50个读卡器
     */
    private List<String> readerDevIndexCodes;

    /**
     * 开始时间（事件开始时间）
     */
    private String startTime;

    /**
     * 结束时间（事件结束时间）
     */
    private String endTime;

    /**
     * 入库开始时间
     */
    private String receiveStartTime;

    /**
     * 入库结束时间
     */
    private String receiveEndTime;

    /**
     * 门禁点所在区域
     */
    private String doorRegionIndexCode;

    /**
     * 事件类型
     */
    private List<Integer> eventTypes;

    /**
     * 人员姓名，支持中英文字符，不能包含特殊字符
     */
    private String personName;

    /**
     * 排序字段（支持personName、doorName、eventTime）
     */
    private String sort;

    /**
     * 升/降序
     */
    private String order;
}
