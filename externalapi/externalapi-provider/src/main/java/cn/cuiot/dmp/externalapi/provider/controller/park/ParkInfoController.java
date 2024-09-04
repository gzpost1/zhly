package cn.cuiot.dmp.externalapi.provider.controller.park;

import cn.cuiot.dmp.base.infrastructure.dto.DeleteParam;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.utils.BeanMapper;
import cn.cuiot.dmp.externalapi.service.entity.park.ParkInfoEntity;
import cn.cuiot.dmp.externalapi.service.service.park.ParkInfoService;
import cn.cuiot.dmp.externalapi.service.vendor.park.vo.ParkInfoVo;
import cn.cuiot.dmp.externalapi.service.vendor.park.query.ParkInfoQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import javax.validation.Valid;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


import org.springframework.web.bind.annotation.RestController;
/**
 * 车辆管理
 * @author pengjian
 * @since 2024-09-03
 */
@RestController
@RequestMapping("/park-info")
public class ParkInfoController {

    @Autowired
    private ParkInfoService parkInfoService;

    /**
     * 同步停车场数据
     * @return
     */
    @PostMapping("/syncParkInfo")
    public IdmResDTO syncParkInfo(){
        parkInfoService.syncParkInfo();
        return IdmResDTO.success();
    }

    /**
     * 分页查询
     * @param query
     * @return
     */
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<ParkInfoEntity>> queryForPage(@RequestBody ParkInfoQuery query) {
     QueryWrapper<ParkInfoEntity> queryWrapper = new QueryWrapper<>();

     IPage<ParkInfoEntity> pageResult = parkInfoService.page(new Page<>(query.getPageNo(), query.getPageSize()),queryWrapper);
     return IdmResDTO.success(pageResult);
    }

    /**
     * 更新楼盘信息
     * @return
     */
    @PostMapping("/updateCommunity")
    public IdmResDTO updateCommunity(@RequestBody @Valid ParkInfoVo parkInfoVo){
        ParkInfoEntity map = BeanMapper.map(parkInfoVo, ParkInfoEntity.class);
        parkInfoService.updateById(map);
        return IdmResDTO.success();
    }

    /**
     * 查看详情
     * @param idParam
     * @return
     */
    @PostMapping("/queryForDetail")
    public IdmResDTO<ParkInfoEntity> queryForDetail(@RequestBody @Valid IdParam idParam) {
     return IdmResDTO.success(parkInfoService.getById(idParam.getId()));
    }

    /**
     * 删除
     * @param deleteParam
     * @return
     */
    @PostMapping("/delete")
    public IdmResDTO delete(@RequestBody @Valid DeleteParam deleteParam) {
        parkInfoService.removeById(deleteParam.getId());
        return IdmResDTO.success();
    }
}
