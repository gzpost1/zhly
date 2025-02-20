package cn.cuiot.dmp.externalapi.service.feign;

import cn.cuiot.dmp.base.infrastructure.domain.pojo.BuildingArchiveReq;
import cn.cuiot.dmp.base.infrastructure.dto.BaseUserDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.BaseUserReqDto;
import cn.cuiot.dmp.base.infrastructure.feign.ArchiveFeignService;
import cn.cuiot.dmp.base.infrastructure.feign.SystemApiFeignService;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * ApiSystemService实现
 *
 * @Author: zc
 * @Date: 2024-08-21
 */
@Slf4j
@Service
public class SystemApiService {

    @Autowired
    private SystemApiFeignService systemApiFeignService;
    @Autowired
    private ArchiveFeignService archiveFeignService;

    /**
     * 查询楼房信息
     */
    public List<BuildingArchive> buildingArchiveQueryForList(@RequestBody @Valid BuildingArchiveReq buildingArchiveReq){
        try {
            IdmResDTO<List<BuildingArchive>> listIdmResDTO = archiveFeignService
                    .buildingArchiveQueryForList(buildingArchiveReq);
            if (Objects.nonNull(listIdmResDTO) && Objects.equals(ResultCode.SUCCESS.getCode(), listIdmResDTO.getCode())) {
                return listIdmResDTO.getData();
            }
            String message = null;
            if (Objects.nonNull(listIdmResDTO)) {
                message = listIdmResDTO.getMessage();
            }
            throw new RuntimeException(message);
        } catch (Exception ex) {
            log.info("ApiSystemServiceImpl==buildingArchiveQueryForList==fail", ex);
            throw new BusinessException(ResultCode.QUERY_FORM_CONFIG_ERROR);
        }
    }

    /**
     * 查询用户
     */
    public List<BaseUserDto> lookUpUserList(BaseUserReqDto query) {
        try {
            IdmResDTO<List<BaseUserDto>> idmResDTO = systemApiFeignService
                    .lookUpUserList(query);
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
            log.info("ApiSystemServiceImpl==lookUpUserList==fail", ex);
            throw new BusinessException(ResultCode.QUERY_USER_DEPT_ERROR);
        }
    }
}
