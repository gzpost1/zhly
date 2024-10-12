package cn.cuiot.dmp.externalapi.service.vendor.yfwaterelectricity.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 宇泛设备 指令请求
 *
 * @author xiaotao
 * @date 2024/8/21 14:13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class EquipmentEventCommandControllerReq implements Serializable {


    private static final long serialVersionUID = 1569273617518826349L;

    /**
     * 设备序列号列表，多个设备序列号用,隔开
     */
    private String deviceNos;

    /**
     * 设备来源
     */
    private String source;

    /**
     *
     * EquipmentEventEnum
     *
     * getRemainTotal:获取实时数据
     * getHourData:获取小时数据
     * getDayData:获取日用量数据
     * getMonthData:获取月用量数据
     */
    private String action;


    /**
     * 请求时间  {\"startTime\":\"2023-06-01
     * 00:00:00\",\"endTime\":\"2023-06-03 00:00:00\"}
     */
    private String params;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(toBuilder = true)
    public static class ParamTime implements Serializable {

        private LocalDateTime startTime;

        private LocalDateTime endTime;

    }


}
