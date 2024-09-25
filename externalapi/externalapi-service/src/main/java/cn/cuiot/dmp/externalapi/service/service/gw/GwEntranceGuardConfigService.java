package cn.cuiot.dmp.externalapi.service.service.gw;

import cn.cuiot.dmp.base.infrastructure.dto.req.PlatfromInfoReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.PlatfromInfoRespDTO;
import cn.cuiot.dmp.common.bean.external.GWEntranceGuardBO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.enums.FootPlateInfoEnum;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.externalapi.service.feign.SystemApiService;
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
    private SystemApiService systemApiService;

    /**
     * 获取对接配置
     *
     * @return productKey
     * @Param companyId 企业id
     */
    public GWEntranceGuardBO getConfigInfo(Long companyId) {
        // 构建请求DTO
        PlatfromInfoReqDTO dto = new PlatfromInfoReqDTO();
        dto.setCompanyId(companyId);
        dto.setPlatformId(FootPlateInfoEnum.GW_ENTRANCE_GUARD.getId());

        // 查询平台信息
        List<PlatfromInfoRespDTO> list = systemApiService.queryPlatfromInfoList(dto);
        if (CollectionUtils.isEmpty(list)) {
            throw new BusinessException(ResultCode.ERROR, "【格物门禁】对接参数配置为空，请先配置后再创建数据");
        }

        // 获取第一条记录
        PlatfromInfoRespDTO respDTO = list.get(0);

        return checkConfig(respDTO);
    }

    public List<GWEntranceGuardBO> getConfigInfo(List<Long> companyId) {
        // 构建请求DTO
        PlatfromInfoReqDTO dto = new PlatfromInfoReqDTO();
        dto.setCompanyIds(companyId);
        dto.setPlatformId(FootPlateInfoEnum.GW_ENTRANCE_GUARD.getId());

        // 查询平台信息
        List<PlatfromInfoRespDTO> list = systemApiService.queryPlatfromInfoList(dto);
        if (CollectionUtils.isEmpty(list)) {
            throw new BusinessException(ResultCode.ERROR, "【格物门禁】对接参数配置为空，请先配置后再创建数据");
        }

        List<GWEntranceGuardBO> result = Lists.newArrayList();
        for (PlatfromInfoRespDTO respDTO : list) {
            try {
                GWEntranceGuardBO gwEntranceGuardBO = checkConfig(respDTO);
                result.add(gwEntranceGuardBO);
            } catch (Exception e) {
                log.error("企业id【" + respDTO.getCompanyId() + "】配置为空");
            }
        }
        return result;
    }

    private GWEntranceGuardBO checkConfig(PlatfromInfoRespDTO respDTO) {
        // 检查返回的数据是否为空
        if (StringUtils.isBlank(respDTO.getData())) {
            throw new BusinessException(ResultCode.ERROR, "【格物门禁】对接参数配置为空，请先配置后再创建数据");
        }
        // 从JSON数据中解析出GWEntranceGuardBO对象
        GWEntranceGuardBO gwEntranceGuardBO = FootPlateInfoEnum.getObjectFromJsonById(FootPlateInfoEnum.GW_ENTRANCE_GUARD.getId(), respDTO.getData());
        if (Objects.isNull(gwEntranceGuardBO)) {
            throw new BusinessException(ResultCode.ERROR, "【格物门禁】对接参数配置为空，请先配置后再创建数据");
        }
        if (StringUtils.isBlank(gwEntranceGuardBO.getAppId())) {
            throw new BusinessException(ResultCode.ERROR, "【格物门禁】对接参数【appId】配置为空，请先配置后再创建数据");
        }
        if (StringUtils.isBlank(gwEntranceGuardBO.getAppSecret())) {
            throw new BusinessException(ResultCode.ERROR, "【格物门禁】对接参数【appSecret】配置为空，请先配置后再创建数据");
        }
        if (StringUtils.isBlank(gwEntranceGuardBO.getProductKey())) {
            throw new BusinessException(ResultCode.ERROR, "【格物门禁】对接参数【productKey】配置为空，请先配置后再创建数据");
        }
        gwEntranceGuardBO.setCompanyId(respDTO.getCompanyId());
        return gwEntranceGuardBO;
    }
}
