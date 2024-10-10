package cn.cuiot.dmp.externalapi.provider.controller.app.hik;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.annotation.ResolveExtData;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.common.constant.ErrorCode;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.externalapi.service.converter.hik.HaikangAcsDoorConverter;
import cn.cuiot.dmp.externalapi.service.entity.hik.HaikangAcsDoorEntity;
import cn.cuiot.dmp.externalapi.service.query.hik.HaikangAcsDoorControlDto;
import cn.cuiot.dmp.externalapi.service.query.hik.HaikangAcsDoorQuery;
import cn.cuiot.dmp.externalapi.service.query.hik.HaikangAcsDoorStateQuery;
import cn.cuiot.dmp.externalapi.service.service.hik.HaikangAcsDoorService;
import cn.cuiot.dmp.externalapi.service.vo.hik.HaikangAcsDoorVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * APP端-海康门禁点
 *
 * @author: wuyongchong
 * @date: 2024/10/10 15:56
 */
@Slf4j
@RestController
@RequestMapping("/app/hik/acs-door")
public class HaikangAcsDoorController {

    @Autowired
    private HaikangAcsDoorService haikangAcsDoorService;

    @Autowired
    private HaikangAcsDoorConverter haikangAcsDoorConverter;

    /**
     * 分页查询
     */
    @ResolveExtData
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<HaikangAcsDoorVo>> queryForPage(
            @RequestBody HaikangAcsDoorQuery query) {
        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        query.setCompanyId(currentOrgId);
        IPage<HaikangAcsDoorVo> pageData = haikangAcsDoorService.queryForPage(query);
        return IdmResDTO.success(pageData);
    }

    /**
     * 下拉列表查询
     */
    @ResolveExtData
    @PostMapping("/listForSelect")
    public IdmResDTO<List<HaikangAcsDoorVo>> listForSelect(
            @RequestBody HaikangAcsDoorQuery query) {
        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        query.setCompanyId(currentOrgId);
        List<HaikangAcsDoorVo> list = haikangAcsDoorService.listForSelect(query);
        return IdmResDTO.success(list);
    }

    /**
     * 门禁点反控
     */
    @RequiresPermissions
    @PostMapping("/doControlDoor")
    public IdmResDTO doControlDoor(@RequestBody @Valid HaikangAcsDoorControlDto dto) {
        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        dto.setCompanyId(currentOrgId);
        haikangAcsDoorService.doControlDoor(dto);
        return IdmResDTO.success();
    }

    /**
     * 查询门禁点状态
     */
    @RequiresPermissions
    @PostMapping("/queryDoorState")
    public IdmResDTO<HaikangAcsDoorVo> queryDoorState(@RequestBody @Valid IdParam param) {
        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        HaikangAcsDoorEntity acsDoorEntity = Optional.ofNullable(
                haikangAcsDoorService.getById(param.getId())).orElseThrow(
                () -> new BusinessException(ErrorCode.NOT_FOUND.getCode(),
                        ErrorCode.NOT_FOUND.getMessage()));
        AssertUtil.isTrue(acsDoorEntity.getOrgId().equals(currentOrgId), "操作失败，越权操作数据");
        HaikangAcsDoorStateQuery query = new HaikangAcsDoorStateQuery();
        query.setIndexCode(acsDoorEntity.getIndexCode());
        query.setCompanyId(currentOrgId);
        Byte state = haikangAcsDoorService.queryDoorState(query);
        HaikangAcsDoorVo vo = haikangAcsDoorConverter.entityToVo(acsDoorEntity);
        vo.setDoorState(state);
        return IdmResDTO.success(vo);
    }

}
