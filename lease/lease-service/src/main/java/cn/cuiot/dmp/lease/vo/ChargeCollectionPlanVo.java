package cn.cuiot.dmp.lease.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.cuiot.dmp.lease.enums.ChannelEnum;
import cn.hutool.core.collection.CollectionUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 收费管理-催款计划vo
 *
 * @author zc
 */
@Data
public class ChargeCollectionPlanVo {
    /**
     * id
     */
    @Excel(name = "催缴计划编码", orderNum = "0", width = 20)
    private Long id;

    /**
     * 计划名称
     */
    @Excel(name = "计划名称", orderNum = "1", width = 20)
    private String name;

    /**
     * 通知渠道（1：系统消息；2：短信）
     */
    private List<String> channel;


    @Excel(name = "通知渠道", orderNum = "2", width = 20)
    private String channelName;

    public String getChannelName(){
        List<String> channelList = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(channel)){
            for(String channel :channel){
                String desc = ChannelEnum.getDesc(channel);
                channelList.add(desc);
            }

            return String.join(", ", channelList);
        }

        return null;
    }

    /**
     * 发送日期类型（1:每天，2:每周，3:每月）
     */
    private Byte cronType;

    @Excel(name = "生产频率", orderNum = "5", width = 20)
    private String cornTypeName;

    /**
     * 执行频率-指定日期 1-31
     */
    private Integer cronAppointDay;

    /**
     * 执行频率-指定周数 1-7
     */
    private Integer cronAppointWeek;

    /**
     * 执行频率-指定的小时分
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date cronTime;

    /**
     * 停启用状态（0停用，1启用）
     */
    private Byte status;

    @Excel(name = "状态", orderNum = "6", width = 20)
    private String statusName;

    /**
     * 楼盘id列表
     */
    private List<Long> buildings;

    /**
     * 收费项目列表
     */
    private List<Long> chargeItems;
}