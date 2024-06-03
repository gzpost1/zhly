package cn.cuiot.dmp.base.application.service.impl;

import cn.cuiot.dmp.base.application.service.ApiArchiveService;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.feign.ArchiveFeignService;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 档案信息查询实现
 *
 * @author: wuyongchong
 * @date: 2024/6/3 17:44
 */
@Slf4j
@Service
public class ApiArchiveServiceImpl implements ApiArchiveService {

    @Autowired
    private ArchiveFeignService archiveFeignService;

    /**
     * 根据ID获取楼盘信息
     */
    @Override
    public BuildingArchive lookupBuildingArchiveInfo(Long id) {
        IdParam idParam = new IdParam();
        idParam.setId(id);
        try {
            IdmResDTO<BuildingArchive> idmResDTO = archiveFeignService
                    .lookupBuildingArchiveInfo(idParam);
            if (Objects.nonNull(idmResDTO) && ResultCode.SUCCESS.getCode()
                    .equals(idmResDTO.getCode())) {
                return idmResDTO.getData();
            }
            String message = null;
            if (Objects.nonNull(idmResDTO)) {
                message = idmResDTO.getMessage();
            }
            throw new RuntimeException(message);
        } catch (Exception ex) {
            log.info("ApiArchiveServiceImpl==lookupBuildingArchiveInfo==fail", ex);
            throw new BusinessException(ResultCode.QUERY_USER_DEPT_ERROR);
        }
    }

}
