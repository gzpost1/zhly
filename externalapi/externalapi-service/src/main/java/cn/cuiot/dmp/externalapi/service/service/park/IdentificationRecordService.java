package cn.cuiot.dmp.externalapi.service.service.park;


import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.externalapi.service.constant.PortraitInputConstant;
import cn.cuiot.dmp.externalapi.service.entity.park.AccessControlEntity;
import cn.cuiot.dmp.externalapi.service.entity.park.IdentificationRecordEntity;
import cn.cuiot.dmp.externalapi.service.mapper.park.IdentificationRecordMapper;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 识别记录上报记录 服务类
 *
 * @author pengjian
 * @since 2024-11-06
 */
@Service
public class IdentificationRecordService extends ServiceImpl<IdentificationRecordMapper, IdentificationRecordEntity>{


    @Autowired
    private AccessControlService accessControlService;
    /**
     * 识别记录上报
     * @param params
     * @return
     */
    public IdmResDTO identificationRecordReport(Map<String, String> params) {
        String eventCode = params.get("eventCode");
        String eventGuid = params.get("eventGuid");
        String eventMsg = params.get("eventMsg");
        if(!StringUtils.equals(eventCode, PortraitInputConstant.RECORD_EVENT_CODE)){
            throw new RuntimeException("接口编码错误"+eventCode);
        }
        IdentificationRecordEntity identificationRecordEntity = JsonUtil.readValue(eventMsg, IdentificationRecordEntity.class);
        identificationRecordEntity.setCreateTime(new Date());
        identificationRecordEntity.setShowDate(new Date(Long.parseLong(identificationRecordEntity.getShowTime())));
        identificationRecordEntity.setEventGuid(eventGuid);
        //根据设备编码查询所属楼盘信息与公司信息
        LambdaQueryWrapper<AccessControlEntity> lw = new LambdaQueryWrapper<>();
        lw.eq(AccessControlEntity::getDeviceNo,identificationRecordEntity.getDeviceNo());
        List<AccessControlEntity> list = accessControlService.list(lw);
        if(CollectionUtil.isNotEmpty(list)){
            identificationRecordEntity.setCompanyId(list.get(0).getCompanyId());
            identificationRecordEntity.setCommunityId(list.get(0).getCommunityId());
        }
        this.saveOrUpdate(identificationRecordEntity);
        return IdmResDTO.success();
    }
}
