package cn.cuiot.dmp.externalapi.provider.controller.admin.hik;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.dto.ExcelDownloadCallable;
import cn.cuiot.dmp.base.application.dto.ExcelDownloadDto;
import cn.cuiot.dmp.base.application.service.ExcelExportService;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.common.constant.ErrorCode;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.externalapi.service.converter.hik.HaikangAcsDoorConverter;
import cn.cuiot.dmp.externalapi.service.entity.hik.HaikangAcsDoorEntity;
import cn.cuiot.dmp.externalapi.service.query.hik.HaikangAcsDoorControlDto;
import cn.cuiot.dmp.externalapi.service.query.hik.HaikangAcsDoorQuery;
import cn.cuiot.dmp.externalapi.service.query.hik.HaikangAcsDoorStateQuery;
import cn.cuiot.dmp.externalapi.service.service.hik.HaikangAcsDoorService;
import cn.cuiot.dmp.externalapi.service.sync.hik.HaikangAcsDataManualSyncService;
import cn.cuiot.dmp.externalapi.service.vo.hik.HaikangAcsDoorExportVo;
import cn.cuiot.dmp.externalapi.service.vo.hik.HaikangAcsDoorVo;
import cn.cuiot.dmp.externalapi.service.vo.hik.HikDoorControlVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.Date;
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
 * 管理端-海康门禁点
 *
 * @author: wuyongchong
 * @date: 2024/10/10 15:56
 */
@Slf4j
@RestController
@RequestMapping("/hik/acs-door")
public class HaikangAcsDoorController {

    @Autowired
    private HaikangAcsDoorService haikangAcsDoorService;

    @Autowired
    private HaikangAcsDataManualSyncService haikangAcsDataManualSyncService;

    @Autowired
    private HaikangAcsDoorConverter haikangAcsDoorConverter;

    @Autowired
    private ExcelExportService excelExportService;

    /**
     * 分页查询
     */
    @RequiresPermissions
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
    @RequiresPermissions
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
    public IdmResDTO<List<HikDoorControlVo>> doControlDoor(@RequestBody @Valid HaikangAcsDoorControlDto dto) {
        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        dto.setCompanyId(currentOrgId);
        List<HikDoorControlVo> resultList = haikangAcsDoorService.doControlDoor(dto);
        return IdmResDTO.success(resultList);
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

    /**
     * 手动同步数据
     */
    @RequiresPermissions
    @PostMapping("/syncData")
    public IdmResDTO syncData() {
        haikangAcsDataManualSyncService.haikangAcsDoorDataManualSync();
        return IdmResDTO.success(null);
    }

    /**
     * 导出
     */
    @RequiresPermissions
    @PostMapping("export")
    public IdmResDTO export(@RequestBody HaikangAcsDoorQuery query) {

        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        query.setCompanyId(currentOrgId);

        ExcelDownloadDto<HaikangAcsDoorQuery> dto = ExcelDownloadDto.<HaikangAcsDoorQuery>builder()
                .loginInfo(LoginInfoHolder.getCurrentLoginInfo())
                .query(query)
                .fileName("门禁点导出（" + DateTimeUtil.dateToString(new Date(), "yyyyMMdd") + "）")
                .build();

        excelExportService.excelExport(dto, HaikangAcsDoorExportVo.class,
                new ExcelDownloadCallable<HaikangAcsDoorQuery, HaikangAcsDoorExportVo>() {
                    @Override
                    public IPage<HaikangAcsDoorExportVo> excute(
                            ExcelDownloadDto<HaikangAcsDoorQuery> dto) {

                        IPage<HaikangAcsDoorVo> page = haikangAcsDoorService.queryForPage(query);

                        return page.convert(item->{
                            HaikangAcsDoorExportVo exportVo = haikangAcsDoorConverter.voToExportVo(
                                    item);
                            return exportVo;
                        });
                    }
                });

        return IdmResDTO.success();
    }

}
