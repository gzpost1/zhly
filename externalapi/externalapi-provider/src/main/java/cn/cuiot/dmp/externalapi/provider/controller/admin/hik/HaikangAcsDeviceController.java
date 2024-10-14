package cn.cuiot.dmp.externalapi.provider.controller.admin.hik;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.dto.ExcelDownloadCallable;
import cn.cuiot.dmp.base.application.dto.ExcelDownloadDto;
import cn.cuiot.dmp.base.application.service.ExcelExportService;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.externalapi.service.converter.hik.HaikangAcsDeviceConverter;
import cn.cuiot.dmp.externalapi.service.query.hik.HaikangAcsDeviceQuery;
import cn.cuiot.dmp.externalapi.service.query.hik.HaikangRegionQuery;
import cn.cuiot.dmp.externalapi.service.service.hik.HaikangAcsDeviceService;
import cn.cuiot.dmp.externalapi.service.sync.hik.HaikangAcsDataManualSyncService;
import cn.cuiot.dmp.externalapi.service.vendor.hik.bean.resp.HikRegionResp;
import cn.cuiot.dmp.externalapi.service.vo.hik.HaikangAcsDeviceExportVo;
import cn.cuiot.dmp.externalapi.service.vo.hik.HaikangAcsDeviceVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端-海康门禁设备
 *
 * @author: wuyongchong
 * @date: 2024/10/10 14:13
 */
@Slf4j
@RestController
@RequestMapping("/hik/acs-device")
public class HaikangAcsDeviceController {


    @Autowired
    private HaikangAcsDeviceService haikangAcsDeviceService;

    @Autowired
    private HaikangAcsDataManualSyncService haikangAcsDataManualSyncService;


    @Autowired
    private ExcelExportService excelExportService;

    @Autowired
    private HaikangAcsDeviceConverter haikangAcsDeviceConverter;

    /**
     * 分页查询
     */
    @RequiresPermissions
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<HaikangAcsDeviceVo>> queryForPage(
            @RequestBody HaikangAcsDeviceQuery query) {
        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        query.setCompanyId(currentOrgId);
        IPage<HaikangAcsDeviceVo> pageData = haikangAcsDeviceService.queryForPage(query);
        return IdmResDTO.success(pageData);
    }

    /**
     * 下拉列表查询
     */
    @RequiresPermissions
    @PostMapping("/listForSelect")
    public IdmResDTO<List<HaikangAcsDeviceVo>> listForSelect(
            @RequestBody HaikangAcsDeviceQuery query) {
        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        query.setCompanyId(currentOrgId);
        List<HaikangAcsDeviceVo> list = haikangAcsDeviceService.listForSelect(query);
        return IdmResDTO.success(list);
    }

    /**
     * 查询区域
     */
    @RequiresPermissions
    @PostMapping("/queryRegions")
    public IdmResDTO<List<HikRegionResp.DataItem>> queryRegions(
            @RequestBody HaikangRegionQuery query) {
        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        query.setCompanyId(currentOrgId);
        List<HikRegionResp.DataItem> list = haikangAcsDeviceService.queryRegions(query);
        return IdmResDTO.success(list);
    }

    /**
     * 手动同步数据
     */
    @RequiresPermissions
    @PostMapping("/syncData")
    public IdmResDTO syncData() {
        haikangAcsDataManualSyncService.haikangAcsDeviceDataManualSync();
        return IdmResDTO.success(null);
    }

    /**
     * 导出
     */
    @RequiresPermissions
    @PostMapping("export")
    public IdmResDTO export(@RequestBody HaikangAcsDeviceQuery query) {

        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        query.setCompanyId(currentOrgId);

        ExcelDownloadDto<HaikangAcsDeviceQuery> dto = ExcelDownloadDto.<HaikangAcsDeviceQuery>builder()
                .loginInfo(LoginInfoHolder.getCurrentLoginInfo())
                .query(query)
                .fileName("门禁设备导出（" + DateTimeUtil.dateToString(new Date(), "yyyyMMdd") + "）")
                .build();

        excelExportService.excelExport(dto, HaikangAcsDeviceExportVo.class,
                new ExcelDownloadCallable<HaikangAcsDeviceQuery, HaikangAcsDeviceExportVo>() {
                    @Override
                    public IPage<HaikangAcsDeviceExportVo> excute(
                            ExcelDownloadDto<HaikangAcsDeviceQuery> dto) {

                        IPage<HaikangAcsDeviceVo> page = haikangAcsDeviceService.queryForPage(
                                dto.getQuery());

                        return page.convert(item -> {
                            HaikangAcsDeviceExportVo exportVo = haikangAcsDeviceConverter.voToExportVo(
                                    item);
                            return exportVo;
                        });
                    }
                });

        return IdmResDTO.success();
    }

}
