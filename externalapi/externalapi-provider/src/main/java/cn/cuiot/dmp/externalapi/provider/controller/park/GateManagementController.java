package cn.cuiot.dmp.externalapi.provider.controller.park;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.DeleteParam;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.utils.BeanMapper;
import cn.cuiot.dmp.externalapi.service.entity.park.GateManagementEntity;
import cn.cuiot.dmp.externalapi.service.service.park.GateManagementService;
import cn.cuiot.dmp.externalapi.service.vendor.park.query.GateManagementQuery;
import cn.cuiot.dmp.externalapi.service.vendor.park.vo.GageManagePageVO;
import cn.cuiot.dmp.externalapi.service.vendor.park.vo.GateControlVO;
import cn.cuiot.dmp.externalapi.service.vendor.park.vo.GateManagementDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;
import javax.validation.Valid;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


import org.springframework.web.bind.annotation.RestController;
/**
 * 管理后台-道闸管理
 * @author pengjian
 * @since 2024-09-09
 */
@RestController
@RequestMapping("/gate-management")
public class GateManagementController {

    @Autowired
    private GateManagementService gateManagementService;


    /**
     * 分页获取数据信息
     * @param query
     * @return
     */

    @PostMapping("/queryForPage")
    @RequiresPermissions
    public IdmResDTO<IPage<GageManagePageVO>> queryForPage(@RequestBody GateManagementQuery query) {

     IPage<GageManagePageVO> pageResult = gateManagementService.queryForPage(new Page<>(query.getPageNo(), query.getPageSize()),query);
     return IdmResDTO.success(pageResult);
    }


    /**
     * 道闸控制
     * @return
     */
    @PostMapping("/gateControl")
    @RequiresPermissions
    public IdmResDTO gateControl(@RequestBody @Valid GateControlVO vo){
        return gateManagementService.gateControl(vo);
    }





}
