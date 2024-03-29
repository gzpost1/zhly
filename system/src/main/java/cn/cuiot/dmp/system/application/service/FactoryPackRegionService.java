package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.system.infrastructure.entity.dto.RegionAddReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.RegionDeleteReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.RegionDetailReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.RegionListReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.RegionListResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.RegionUpdateReqDto;
import java.util.List;

/**
 * @author wqd
 * @classname PropertyRegionService
 * @description
 * @date 2023/1/12
 */
public interface FactoryPackRegionService {

    /**
     * 添加区域
     * @param dto
     */
    void regionAdd(RegionAddReqDto dto);

    /**
     * 查询区域
     * @param dto
     * @return
     */
    PageResult<RegionListResDto> regionList(RegionListReqDto dto);

    /**
     * 根据园区id查询区域
     * @param dto
     * @return
     */
    List<RegionListResDto> regionListByParkId(RegionListReqDto dto);

    /**
     * 详情
     * @param dto
     * @return
     */
    RegionListResDto regionDetail(RegionDetailReqDto dto);

    /**
     * 编辑
     * @param dto
     */
    void regionUpdate(RegionUpdateReqDto dto);

    /**
     * 删除
     * @param dto
     */
    void regionDelete(RegionDeleteReqDto dto);

}
