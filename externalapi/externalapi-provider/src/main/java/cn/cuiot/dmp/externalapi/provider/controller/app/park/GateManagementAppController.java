package cn.cuiot.dmp.externalapi.provider.controller.app.park;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.externalapi.service.service.park.GateManagementService;
import cn.cuiot.dmp.externalapi.service.vendor.park.query.GateManagementQuery;
import cn.cuiot.dmp.externalapi.service.vendor.park.vo.GageManagePageVO;
import cn.cuiot.dmp.externalapi.service.vendor.park.vo.GateControlVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * app-道闸管理
 * @author pengjian
 * @since 2024-09-09
 */
@RestController
@RequestMapping("/app/gate-management")
public class GateManagementAppController {

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
