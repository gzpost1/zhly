package cn.cuiot.dmp.externalapi.service.vo.hik;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 海康门禁点事件分页vo
 *
 * @Author: zc
 * @Date: 2024-10-11
 */
@Data
public class HikAcsDoorEventsPageVO {

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
     * 证件号码
     */
    private String certNo;

    /**
     * 卡号
     */
    private String cardNo;

    /**
     * 人员所属组织编码
     */
    private String orgName;

    /**
     * 门禁点名称
     */
    private String doorName;

    /**
     * 门禁点所在区域编码
     */
    private String doorRegionIndexCode;

    /**
     * 门禁点区域名称
     */
    private String doorRegionName;

    /**
     * 控制器设备名称
     */
    private String devName;

    /**
     * 事件图片
     */
    private String picture;

    /**
     * 事件类型
     */
    private Integer eventType;

    /**
     * 事件类型名称
     */
    private String eventTypeName;

    /**
     * 进出类型（1:进; 0:出; -1:未知）
     */
    private Integer inAndOutType;

    /**
     * 进出类型名称
     */
    private String inAndOutTypeName;

    /**
     * 事件时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime eventTime;

    public String getInAndOutTypeName() {
        String name = null;
        if (Objects.nonNull(inAndOutType)) {
            switch (inAndOutType) {
                case 1:
                    name = "进";
                    break;
                case 0:
                    name = "出";
                    break;
                case -1:
                    name = "未知";
                    break;
                default:
            }
        }
        return name;
    }
}
