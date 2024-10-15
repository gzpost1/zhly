package cn.cuiot.dmp.largescreen.service.vo;

import lombok.Data;

/**
 * 基础档案统计
 * @author xiaotao
 * @date 2024/10/08
 */
@Data
public class ArchivesStatisticVO {

    /**
     * 楼盘数
     */
    private Integer parks;

    /**
     * 楼栋数
     */
    private Long buildings;

    /**
     * 房间数
     */
    private Long houses;

    /**
     * 已租
     */
    private Long leased;

    /**
     * 未租
     */
    private Long leasing;



}
