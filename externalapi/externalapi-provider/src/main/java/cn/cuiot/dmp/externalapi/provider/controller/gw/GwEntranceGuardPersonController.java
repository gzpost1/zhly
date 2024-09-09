package cn.cuiot.dmp.externalapi.provider.controller.gw;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.IdsParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.externalapi.service.entity.gw.GwEntranceGuardPersonEntity;
import cn.cuiot.dmp.externalapi.service.entity.unicom.UnicomEntranceGuardPassRecordEntity;
import cn.cuiot.dmp.externalapi.service.query.gw.GwEntranceGuardPersonCreateDto;
import cn.cuiot.dmp.externalapi.service.query.gw.GwEntranceGuardPersonUpdateDto;
import cn.cuiot.dmp.externalapi.service.vo.unicom.UnicomEntranceGuardPassRecordQueryVO;
import cn.cuiot.dmp.externalapi.service.vo.unicom.UnicomEntranceGuardPersonManageQueryVO;
import cn.cuiot.dmp.externalapi.service.service.gw.UnicomEntranceGuardPassRecordService;
import cn.cuiot.dmp.externalapi.service.service.gw.GwEntranceGuardPersonService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 联通格物门禁 人员管理
 *
 * @Author: zc
 * @Date: 2024-09-07
 */
@RestController
@RequestMapping("/gw/entranceGuard/person")
public class GwEntranceGuardPersonController {

    @Autowired
    private GwEntranceGuardPersonService gwEntranceGuardPersonService;
    @Autowired
    private UnicomEntranceGuardPassRecordService unicomEntranceGuardPassRecordService;

    /**
     * 创建
     */
    @PostMapping("/create")
    public IdmResDTO<?> create(@RequestBody @Valid GwEntranceGuardPersonCreateDto dto) {
        gwEntranceGuardPersonService.create(dto);
        return IdmResDTO.success();
    }

    /**
     * 更新
     */
    @PostMapping("/update")
    public IdmResDTO<?> update(@RequestBody @Valid GwEntranceGuardPersonUpdateDto dto) {
        gwEntranceGuardPersonService.update(dto);
        return IdmResDTO.success();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public IdmResDTO<?> delete(@RequestBody @Valid IdsParam param) {
        gwEntranceGuardPersonService.delete(param.getIds());
        return IdmResDTO.success();
    }

    /**
     * 查询人员管理分页
     *
     * @param vo 参数
     * @return page
     */
    @PostMapping(value = "/queryForPage")
    @RequiresPermissions
    public IdmResDTO<IPage<GwEntranceGuardPersonEntity>> queryPersonManageForPage(@RequestBody UnicomEntranceGuardPersonManageQueryVO vo) {
        return IdmResDTO.success(gwEntranceGuardPersonService.queryForPage(vo));
    }

    /**
     * 查询通行记录分页
     *
     * @param vo 参数
     * @return page
     */
    @PostMapping(value = "/queryPassRecordForPage")
    @RequiresPermissions
    public IdmResDTO<IPage<UnicomEntranceGuardPassRecordEntity>> queryPassRecordForPage(@RequestBody UnicomEntranceGuardPassRecordQueryVO vo) {
        return IdmResDTO.success(unicomEntranceGuardPassRecordService.queryForPage(vo));
    }

}
