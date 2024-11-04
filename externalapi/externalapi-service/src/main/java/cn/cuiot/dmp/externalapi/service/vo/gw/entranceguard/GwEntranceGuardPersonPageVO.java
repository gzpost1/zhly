package cn.cuiot.dmp.externalapi.service.vo.gw.entranceguard;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.cuiot.dmp.externalapi.service.entity.gw.entranceguard.GwEntranceGuardPersonEntity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 格物门禁-人员分组信息 VO
 *
 * @Author: zc
 * @Date: 2024-09-10
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GwEntranceGuardPersonPageVO extends GwEntranceGuardPersonEntity {

    /**
     * 楼盘
     */
    @Excel(name = "所属楼盘", orderNum = "6", width = 20)
    private String buildingName;

    /**
     * 所属组织
     */
    @Excel(name = "所属组织", orderNum = "5", width = 20)
    private String deptPathName;

    /**
     * 分组信息
     */
    @Excel(name = "人员分组", orderNum = "7", width = 20)
    private String personGroupName;
    /**
     * 人员性别
     */
    @Excel(name = "人员性别", orderNum = "3", width = 20)
    private String sexName;

    /**
     * 授权状态
     */
    @Excel(name = "授权状态", orderNum = "10", width = 20)
    private String authorizeName;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间",orderNum = "11",  width = 20,exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createExcelTime;
}
