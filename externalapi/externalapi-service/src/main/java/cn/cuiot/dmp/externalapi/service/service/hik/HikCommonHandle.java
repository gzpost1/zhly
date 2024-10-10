package cn.cuiot.dmp.externalapi.service.service.hik;

import cn.cuiot.dmp.base.infrastructure.dto.req.PlatfromInfoReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.PlatfromInfoRespDTO;
import cn.cuiot.dmp.common.bean.external.HIKEntranceGuardBO;
import cn.cuiot.dmp.common.enums.FootPlateInfoEnum;
import cn.cuiot.dmp.externalapi.service.service.park.PlatfromInfoService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 公共处理方法
 *
 * @Author: zc
 * @Date: 2024-10-09
 */
@Service
public class HikCommonHandle {

    @Autowired
    private PlatfromInfoService platfromInfoService;

    /**
     * 根据企业id获取配置海康信息
     *
     * @return HIKEntranceGuardBO
     * @Param companyId 企业id
     */
    public HIKEntranceGuardBO queryHikConfigByPlatfromInfo(Long companyId) {
        if (Objects.nonNull(companyId)) {
            // 海康平台id
            Long platformId = FootPlateInfoEnum.HIK_ENTRANCE_GUARD.getId();

            // 获取平台信息
            PlatfromInfoReqDTO dto = new PlatfromInfoReqDTO();
            dto.setCompanyId(companyId);
            dto.setPlatformId(platformId);
            List<PlatfromInfoRespDTO> list = platfromInfoService.queryForList(dto);

            if (CollectionUtils.isNotEmpty(list)) {
                PlatfromInfoRespDTO respDTO = list.get(0);
                if (StringUtils.isNotBlank(respDTO.getData())) {
                    // json转HIKEntranceGuardBO
                    HIKEntranceGuardBO bo = FootPlateInfoEnum.getObjectFromJsonById(platformId, respDTO.getData());
                    bo.setCompanyId(companyId);
                    return bo;
                }
            }
        }
        return null;
    }
}
