package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.system.infrastructure.entity.dto.FactoryParkParkAddReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.FactoryParkParkListReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.FactoryParkParkListResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.ParkDeleteReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.ParkDetailReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.ParkListResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.ParkUpdateReqDto;
import java.util.List;

/**
 * @author wqd
 * @classname FactoryParkParkService
 * @description
 * @date 2023/1/13
 */
public interface FactoryParkParkService {

    /**
     * 园区添加
     * @param dto
     */
    void parkAdd(FactoryParkParkAddReqDto dto);

    /**
     * 园区列表
     * @param dto
     * @return
     */
    PageResult<FactoryParkParkListResDto> parkList(FactoryParkParkListReqDto dto);

    /**
     * 根据组织获取园区列表
     * @param dto
     * @return
     */
    List<FactoryParkParkListResDto> parkListByDeptTreePath(FactoryParkParkListReqDto dto);

    /**
     * 园区详情
     * @param dto
     * @return
     */
    ParkListResDto parkDetail(ParkDetailReqDto dto);

    /**
     * 园区修改
     * @param dto
     */
    void parkUpdate(ParkUpdateReqDto dto);

    /**
     * 园区删除
     * @param dto
     */
    void parkDelete(ParkDeleteReqDto dto);

}
