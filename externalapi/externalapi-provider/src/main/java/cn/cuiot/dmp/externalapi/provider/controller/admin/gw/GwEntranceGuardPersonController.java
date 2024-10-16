package cn.cuiot.dmp.externalapi.provider.controller.admin.gw;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.IdsParam;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.externalapi.service.entity.gw.GwEntranceGuardAuthorizeEntity;
import cn.cuiot.dmp.externalapi.service.entity.gw.GwEntranceGuardEntity;
import cn.cuiot.dmp.externalapi.service.entity.gw.GwEntranceGuardPersonEntity;
import cn.cuiot.dmp.externalapi.service.query.gw.GwEntranceGuardPersonAuthorizeQuery;
import cn.cuiot.dmp.externalapi.service.query.gw.GwEntranceGuardPersonCreateDto;
import cn.cuiot.dmp.externalapi.service.query.gw.GwEntranceGuardPersonUpdateDto;
import cn.cuiot.dmp.externalapi.service.query.gw.GwEntranceGuardPersonPageQuery;
import cn.cuiot.dmp.externalapi.service.service.gw.GwEntranceGuardAuthorizeService;
import cn.cuiot.dmp.externalapi.service.service.gw.GwEntranceGuardPersonService;
import cn.cuiot.dmp.externalapi.service.service.gw.GwEntranceGuardService;
import cn.cuiot.dmp.externalapi.service.vo.gw.GwEntranceGuardAuthorizeVO;
import cn.cuiot.dmp.externalapi.service.vo.gw.GwEntranceGuardPersonPageVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 后台-联通格物门禁-人员管理
 *
 * @Author: zc
 * @Date: 2024-09-07
 */
@RestController
@RequestMapping("/gw/entranceGuard/person")
public class GwEntranceGuardPersonController {

    @Autowired
    private GwEntranceGuardService gwEntranceGuardService;
    @Autowired
    private GwEntranceGuardPersonService gwEntranceGuardPersonService;
    @Autowired
    private GwEntranceGuardAuthorizeService gwEntranceGuardAuthorizeService;

    /**
     * 查询人员管理分页
     *
     * @param query 参数
     * @return page
     */
    @RequiresPermissions
    @PostMapping(value = "/queryForPage")
    public IdmResDTO<IPage<GwEntranceGuardPersonPageVO>> queryForPage(@RequestBody GwEntranceGuardPersonPageQuery query) {
        return IdmResDTO.success(gwEntranceGuardPersonService.queryForPage(query));
    }

    /**
     *格物门禁-门禁人员导出
     * @param query
     * @return
     */
    @RequiresPermissions
    @PostMapping(value = "/export")
    public IdmResDTO export(@RequestBody GwEntranceGuardPersonPageQuery query){
        gwEntranceGuardPersonService.export(query);
        return IdmResDTO.success();
    }

    /**
     * 详情
     *
     * @param param 参数
     * @return page
     */
    @RequiresPermissions
    @PostMapping(value = "/queryForDetail")
    public IdmResDTO<GwEntranceGuardPersonEntity> queryForDetail(@RequestBody IdParam param) {
        return IdmResDTO.success(gwEntranceGuardPersonService.queryForDetail(param.getId()));
    }

    /**
     * 创建
     */
    @RequiresPermissions
    @LogRecord(operationCode = "createGwEntranceGuardPersonGuard", operationName = "创建格物门禁人员", serviceType = "gwEntranceGuardPerson", serviceTypeName = "格物门禁人员管理")
    @PostMapping("/create")
    public IdmResDTO<?> create(@RequestBody @Valid GwEntranceGuardPersonCreateDto dto) {
        gwEntranceGuardPersonService.create(dto);
        return IdmResDTO.success();
    }

    /**
     * 更新
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateGwEntranceGuardPersonGuard", operationName = "更新格物门禁人员", serviceType = "gwEntranceGuardPerson", serviceTypeName = "格物门禁人员管理")
    @PostMapping("/update")
    public IdmResDTO<?> update(@RequestBody @Valid GwEntranceGuardPersonUpdateDto dto) {
        gwEntranceGuardPersonService.update(dto);
        return IdmResDTO.success();
    }

    /**
     * 删除
     */
    @RequiresPermissions
    @LogRecord(operationCode = "deleteGwEntranceGuardPersonGuard", operationName = "删除格物门禁人员", serviceType = "gwEntranceGuardPerson", serviceTypeName = "格物门禁人员管理")
    @PostMapping("/delete")
    public IdmResDTO<?> delete(@RequestBody @Valid IdsParam param) {
        gwEntranceGuardPersonService.delete(param.getIds());
        return IdmResDTO.success();
    }

    /**
     * 查询授权信息
     */
    @RequiresPermissions
    @PostMapping("/queryAuthorize")
    public IdmResDTO<List<GwEntranceGuardAuthorizeVO>> queryAuthorize(@RequestBody @Valid IdParam param) {
        List<GwEntranceGuardAuthorizeVO> collect = Lists.newArrayList();

        //企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();

        //获取门禁列表
        List<GwEntranceGuardEntity> guardEntities = gwEntranceGuardService.queryListByCompanyId(companyId);
        if (CollectionUtils.isNotEmpty(guardEntities)) {
            //获取人员授权门禁列表
            List<GwEntranceGuardAuthorizeEntity> authorizeList =
                    gwEntranceGuardAuthorizeService.queryAuthorize(param.getId(), companyId);
            Map<Long, GwEntranceGuardAuthorizeEntity> map = authorizeList.stream()
                    .collect(Collectors.toMap(GwEntranceGuardAuthorizeEntity::getEntranceGuardId, e -> e));

            collect = guardEntities.stream().map(item -> {
                GwEntranceGuardAuthorizeVO vo = new GwEntranceGuardAuthorizeVO();
                vo.setEntranceGuardId(item.getId());
                vo.setEntranceGuardName(item.getName());
                vo.setAuthorize(map.containsKey(item.getId()) ? EntityConstants.YES : EntityConstants.NO);
                return vo;
            }).collect(Collectors.toList());
        }
        return IdmResDTO.success(collect);
    }

    /**
     * 批量授权
     */
    @RequiresPermissions
    @LogRecord(operationCode = "authorizeGwEntranceGuardPersonGuard", operationName = "授权格物门禁人员", serviceType = "gwEntranceGuardPerson", serviceTypeName = "格物门禁人员管理")
    @PostMapping("/batchAuthorize")
    public IdmResDTO<?> batchAuthorize(@RequestBody @Valid GwEntranceGuardPersonAuthorizeQuery query) {
        gwEntranceGuardAuthorizeService.batchAuthorize(query);
        return IdmResDTO.success();
    }
}
