package cn.cuiot.dmp.externalapi.provider.controller.app.park;

import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.externalapi.service.entity.park.ParkInfoEntity;
import cn.cuiot.dmp.externalapi.service.service.park.ParkInfoService;
import cn.cuiot.dmp.externalapi.service.vendor.park.query.ParkInfoQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * app-车辆管理
 * @author pengjian
 * @since 2024-09-03
 */
@RestController
@RequestMapping("/app/park-info")
public class ParkAppInfoController {

    @Autowired
    private ParkInfoService parkInfoService;


    /**
     * 分页查询
     * @param query
     * @return
     */
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<ParkInfoEntity>> queryForPage(@RequestBody ParkInfoQuery query) {

     IPage<ParkInfoEntity> pageResult = parkInfoService.queryForPage(new Page<>(query.getPageNo(), query.getPageSize()),query);
     return IdmResDTO.success(pageResult);
    }

}
