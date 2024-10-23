package cn.cuiot.dmp.externalapi.service.service.gw;

import cn.cuiot.dmp.base.infrastructure.dto.req.PlatfromInfoReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.PlatfromInfoRespDTO;
import cn.cuiot.dmp.common.bean.external.GWCurrencyBO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.enums.FootPlateInfoEnum;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.externalapi.service.service.park.PlatfromInfoService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 格物获取对接配置 业务处理
 *
 * @Author: zc
 * @Date: 2024-09-10
 */
@Slf4j
@Service
public class GwEntranceGuardConfigService {

    @Autowired
    private PlatfromInfoService platfromInfoService;

    /**
     * 获取对接配置
     *
     * @return productKey
     * @Param companyId 企业id
     */
    public GWCurrencyBO getConfigInfo(Long companyId) {
        if (Objects.nonNull(companyId)) {
            // 平台id
            Long platformId = FootPlateInfoEnum.GW_ENTRANCE_GUARD.getId();

            // 构建请求DTO
            PlatfromInfoReqDTO dto = new PlatfromInfoReqDTO();
            dto.setCompanyId(companyId);
            dto.setPlatformId(platformId);
            List<PlatfromInfoRespDTO> list = platfromInfoService.queryForList(dto);

            if (CollectionUtils.isNotEmpty(list)) {
                PlatfromInfoRespDTO respDTO = list.get(0);
                if (StringUtils.isNotBlank(respDTO.getData())) {
                    GWCurrencyBO bo = FootPlateInfoEnum.getObjectFromJsonById(platformId, respDTO.getData());
                    bo.setCompanyId(companyId);
                    return bo;
                }
            }
        }
        return null;
    }

    public List<GWCurrencyBO> getConfigInfo(List<Long> companyId) {
        // 平台id
        Long platformId = FootPlateInfoEnum.GW_ENTRANCE_GUARD.getId();

        // 构建请求DTO
        PlatfromInfoReqDTO dto = new PlatfromInfoReqDTO();
        dto.setCompanyIds(companyId);
        dto.setPlatformId(platformId);

        // 查询平台信息
        List<PlatfromInfoRespDTO> list = platfromInfoService.queryForList(dto);
        if (CollectionUtils.isEmpty(list)) {
            throw new BusinessException(ResultCode.ERROR, "【格物门禁】对接参数配置为空，请先配置后再创建数据");
        }

        List<GWCurrencyBO> result = Lists.newArrayList();
        for (PlatfromInfoRespDTO respDTO : list) {
            if (StringUtils.isNotBlank(respDTO.getData())) {
                GWCurrencyBO bo = FootPlateInfoEnum.getObjectFromJsonById(platformId, respDTO.getData());
                bo.setCompanyId(respDTO.getCompanyId());
                result.add(bo);
            }
        }
        return result;
    }

    /**
     * 获取对接配置
     *
     * @return productKey
     * @Param companyId 企业id
     */
    public GWCurrencyBO getConfigInfo(Long companyId,Long platformId) {
        if (Objects.nonNull(companyId) && Objects.nonNull(platformId) ) {

            // 构建请求DTO
            PlatfromInfoReqDTO dto = new PlatfromInfoReqDTO();
            dto.setCompanyId(companyId);
            dto.setPlatformId(platformId);
            List<PlatfromInfoRespDTO> list = platfromInfoService.queryForList(dto);

            if (CollectionUtils.isNotEmpty(list)) {
                PlatfromInfoRespDTO respDTO = list.get(0);
                if (StringUtils.isNotBlank(respDTO.getData())) {
                    GWCurrencyBO bo = FootPlateInfoEnum.getObjectFromJsonById(platformId, respDTO.getData());
                    bo.setCompanyId(companyId);
                    return bo;
                }
            }
        }
        return null;
    }

    public List<GWCurrencyBO> getConfigInfoByPlatformId(Long platformId) {

        // 构建请求DTO
        PlatfromInfoReqDTO dto = new PlatfromInfoReqDTO();
        dto.setPlatformId(platformId);

        // 查询平台信息
        List<PlatfromInfoRespDTO> list = platfromInfoService.queryForList(dto);
        if (CollectionUtils.isEmpty(list)) {
            throw new BusinessException(ResultCode.ERROR, "【格物门禁】对接参数配置为空，请先配置后再创建数据");
        }

        List<GWCurrencyBO> result = Lists.newArrayList();
        for (PlatfromInfoRespDTO respDTO : list) {
            if (StringUtils.isNotBlank(respDTO.getData())) {
                GWCurrencyBO bo = FootPlateInfoEnum.getObjectFromJsonById(platformId, respDTO.getData());
                bo.setCompanyId(respDTO.getCompanyId());
                result.add(bo);
            }
        }
        return result;
    }
}
