package cn.cuiot.dmp.externalapi.provider.controller.unicom;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.externalapi.service.entity.unicom.UnicomEntranceGuardPassRecordEntity;
import cn.cuiot.dmp.externalapi.service.entity.unicom.UnicomEntranceGuardPersonManageEntity;
import cn.cuiot.dmp.externalapi.service.vo.unicom.UnicomEntranceGuardPassRecordQueryVO;
import cn.cuiot.dmp.externalapi.service.vo.unicom.UnicomEntranceGuardPersonManageQueryVO;
import cn.cuiot.dmp.externalapi.service.service.unicom.UnicomEntranceGuardPassRecordService;
import cn.cuiot.dmp.externalapi.service.service.unicom.UnicomEntranceGuardPersonManageService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 联通格物门禁
 *
 * @date 2024/8/21 16:01
 * @author gxp
 */
@RestController
@RequestMapping("/unicomEntranceGuard")
public class UnicomEntranceGuardController {

    @Autowired
    private UnicomEntranceGuardPersonManageService unicomEntranceGuardPersonManageService;
    @Autowired
    private UnicomEntranceGuardPassRecordService unicomEntranceGuardPassRecordService;


    /**
     * 查询人员管理分页
     *
     * @param vo
     * @return
     */
    @PostMapping(value = "/queryPersonManageForPage")
    @RequiresPermissions
    public IdmResDTO<IPage<UnicomEntranceGuardPersonManageEntity>> queryPersonManageForPage(@RequestBody UnicomEntranceGuardPersonManageQueryVO vo) {
        return IdmResDTO.success(unicomEntranceGuardPersonManageService.queryForPage(vo));
    }

    /**
     * 查询通行记录分页
     *
     * @param vo
     * @return
     */
    @PostMapping(value = "/queryPassRecordForPage")
    @RequiresPermissions
    public IdmResDTO<IPage<UnicomEntranceGuardPassRecordEntity>> queryPassRecordForPage(@RequestBody UnicomEntranceGuardPassRecordQueryVO vo) {
        return IdmResDTO.success(unicomEntranceGuardPassRecordService.queryForPage(vo));
    }

}
