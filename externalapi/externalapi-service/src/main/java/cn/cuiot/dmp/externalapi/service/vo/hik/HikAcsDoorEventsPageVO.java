package cn.cuiot.dmp.externalapi.service.vo.hik;

import cn.afterturn.easypoi.excel.annotation.Excel;
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
    @Excel(name = "人员姓名", orderNum = "0", width = 20)
    private String personName;

    /**
     * 人员唯一编码
     */
    @Excel(name = "人员编码", orderNum = "1", width = 20)
    private String personId;

    /**
     * 工号
     */
    @Excel(name = "工号", orderNum = "2", width = 20)
    private String jobNo;

    /**
     * 证件号码
     */
    @Excel(name = "证件号", orderNum = "3", width = 20)
    private String certNo;

    /**
     * 卡号
     */
    @Excel(name = "卡号", orderNum = "4", width = 20)
    private String cardNo;

    /**
     * 人员所属组织
     */
    @Excel(name = "人员所属组织", orderNum = "5", width = 20)
    private String orgName;

    /**
     * 门禁点名称
     */
    @Excel(name = "门禁点", orderNum = "6", width = 20)
    private String doorName;

    /**
     * 门禁点所在区域编码
     */
    private String doorRegionIndexCode;

    /**
     * 门禁点区域名称
     */
    @Excel(name = "门禁点区域", orderNum = "7", width = 20)
    private String doorRegionName;

    /**
     * 控制器设备名称
     */
    @Excel(name = "控制器", orderNum = "8", width = 20)
    private String devName;

    /**
     * 事件图片
     */
    @Excel(name = "抓拍图片", type = 2, orderNum = "9", width = 20)
    private String picture;

    /**
     * 事件类型
     */
    private Integer eventType;

    /**
     * 事件类型名称
     */
    @Excel(name = "事件类型", orderNum = "10", width = 20)
    private String eventTypeName;

    /**
     * 进出类型（1:进; 0:出; -1:未知）
     */
    private Integer inAndOutType;

    /**
     * 进出类型名称
     */
    @Excel(name = "进出类型", orderNum = "11", width = 10)
    private String inAndOutTypeName;

    /**
     * 事件时间
     */
    @Excel(name = "事件时间", orderNum = "12", exportFormat = "yyyy-MM-dd HH:mm:ss", width = 20)
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
