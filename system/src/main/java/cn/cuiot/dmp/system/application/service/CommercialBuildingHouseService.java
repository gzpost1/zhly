package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.system.infrastructure.entity.dto.PropertyHouseAddReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.PropertyHouseInfoResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.PropertyHouseListReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.PropertyHouseUpdateReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.vo.PropertyHouseListReqVO;

/**
 * @Author: huw51
 * @Description:
 * @Date: create in 2023/1/6 10:41
 */
public interface CommercialBuildingHouseService {

    /**
     * 查询全部房屋列表
     * @param dto
     * @return
     */
    PageResult<PropertyHouseListReqVO> selectAll(PropertyHouseListReqDto dto);

    /**
     * 添加房屋
     * @param dto
     * @return
     */
    int add(PropertyHouseAddReqDto dto);

    /**
     * 更新
     * @param dto
     * @return
     */
    int update(PropertyHouseUpdateReqDto dto);

    /**
     * 根据id查询房屋
     * @param id
     * @return
     */
    PropertyHouseInfoResDto selectById(Long id, String orgId, String userId);

}
