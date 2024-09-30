package cn.cuiot.dmp.externalapi.provider.controller.app.park;

import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.externalapi.service.query.UniUbiDeviceQueryReq;
import cn.cuiot.dmp.externalapi.service.query.UniUbiDeviceRespInfo;
import cn.cuiot.dmp.externalapi.service.query.UniUbiEntranceGuardQueryVO;
import cn.cuiot.dmp.externalapi.service.query.UniUbiPage;
import cn.cuiot.dmp.externalapi.service.service.park.UniUbiService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 小程序-门禁（宇泛）
 *
 * @date 2024/8/21 16:01
 * @author gxp
 */
@RestController
@RequestMapping("/app/entranceGuard")
public class UniUbiController {

    @Autowired
    private UniUbiService uniUbiService;


    /**
     * 查询分页
     *
     * @param vo
     * @return
     */
    @PostMapping(value = "/queryForPage")
//    @RequiresPermissions
    public IdmResDTO<IPage<UniUbiDeviceRespInfo>> queryForPage(@RequestBody UniUbiEntranceGuardQueryVO vo) {
        //参数转换
        UniUbiPage<UniUbiDeviceRespInfo> uniUbiPage = uniUbiService.queryDevicePageV2(new UniUbiDeviceQueryReq(vo));
        //分页数据转换
        Page<UniUbiDeviceRespInfo> page = new Page<>(uniUbiPage.getIndex(), uniUbiPage.getLength(), uniUbiPage.getTotal());
        page.setRecords(uniUbiPage.getContent());
        return IdmResDTO.success(page);
    }

//    /**
//     * 远程开门
//     *
//     * @param vo
//     * @return
//     */
//    @PostMapping(value = "/open")
//    @RequiresPermissions
//    public IdmResDTO open(@RequestBody @Valid UniUbiEntranceGuardOperateVO vo) {
//        return IdmResDTO.success(uniUbiService.deviceCommandV2(BeanMapper.copyBean(vo, UniUbiCommandReq.class)));
//    }

}
