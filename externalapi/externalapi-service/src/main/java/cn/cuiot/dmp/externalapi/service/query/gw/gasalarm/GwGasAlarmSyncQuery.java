package cn.cuiot.dmp.externalapi.service.query.gw.gasalarm;

import lombok.Data;

import java.util.List;

/**
 * 格物-燃气报警器同步query
 *
 * @Author: zc
 * @Date: 2024-10-24
 */
@Data
public class GwGasAlarmSyncQuery {

    /**
     * 企业id
     */
    private Long companyId;

    /**
     * 雁飞·格物DMP平台唯一标识码。说明 如果传入该参数，则无需传入productKey和deviceKey。iotId作为设备唯一标识码，
     * 和productKey与deviceKey组合是一一对应的关系。如果您同时传入iotId和productKey与deviceKey组合，则以iotId为准。iotId需属于所选产品。
     */
    private List<String> iotId;
}
