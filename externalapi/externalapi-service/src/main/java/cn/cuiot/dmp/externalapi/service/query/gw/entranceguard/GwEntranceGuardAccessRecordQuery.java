package cn.cuiot.dmp.externalapi.service.query.gw.entranceguard;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 格物门禁-通行记录 query
 *
 * @Author: zc
 * @Date: 2024-09-10
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GwEntranceGuardAccessRecordQuery extends PageQuery {
    /**
     * sn门禁
     */
    private String sn;

    /**
     * 人员姓名
     */
    private String personName;

    /**
     * 识别开始时间
     */
    private Date snapBeginTime;

    /**
     * 识别结束时间
     */
    private Date snapEndTime;

    /**
     * 楼盘id
     */
    private Long building;

    /**
     * 企业id(前端不用传)
     */
    private Long companyId;
}
