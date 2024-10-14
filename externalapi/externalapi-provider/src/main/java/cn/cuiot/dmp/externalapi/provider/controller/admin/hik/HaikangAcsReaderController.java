package cn.cuiot.dmp.externalapi.provider.controller.admin.hik;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.dto.ExcelDownloadCallable;
import cn.cuiot.dmp.base.application.dto.ExcelDownloadDto;
import cn.cuiot.dmp.base.application.service.ExcelExportService;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.externalapi.service.converter.hik.HaikangAcsReaderConverter;
import cn.cuiot.dmp.externalapi.service.query.hik.HaikangAcsReaderQuery;
import cn.cuiot.dmp.externalapi.service.service.hik.HaikangAcsReaderService;
import cn.cuiot.dmp.externalapi.service.sync.hik.HaikangAcsDataManualSyncService;
import cn.cuiot.dmp.externalapi.service.vo.hik.HaikangAcsReaderExportVo;
import cn.cuiot.dmp.externalapi.service.vo.hik.HaikangAcsReaderVo;
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
 * 管理端-海康门禁读卡器
 *
 * @author: wuyongchong
 * @date: 2024/10/10 15:56
 */
@Slf4j
@RestController
@RequestMapping("/hik/acs-reader")
public class HaikangAcsReaderController {


    @Autowired
    private HaikangAcsReaderService haikangAcsReaderService;

    @Autowired
    private HaikangAcsDataManualSyncService haikangAcsDataManualSyncService;

    @Autowired
    private ExcelExportService excelExportService;

    @Autowired
    private HaikangAcsReaderConverter haikangAcsReaderConverter;


    /**
     * 分页查询
     */
    @RequiresPermissions
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<HaikangAcsReaderVo>> queryForPage(
            @RequestBody HaikangAcsReaderQuery query) {
        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        query.setCompanyId(currentOrgId);
        IPage<HaikangAcsReaderVo> pageData = haikangAcsReaderService.queryForPage(query);
        return IdmResDTO.success(pageData);
    }

    /**
     * 下拉列表查询
     */
    @RequiresPermissions
    @PostMapping("/listForSelect")
    public IdmResDTO<List<HaikangAcsReaderVo>> listForSelect(
            @RequestBody HaikangAcsReaderQuery query) {
        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        query.setCompanyId(currentOrgId);
        List<HaikangAcsReaderVo> list = haikangAcsReaderService.listForSelect(query);
        return IdmResDTO.success(list);
    }

    /**
     * 手动同步数据
     */
    @RequiresPermissions
    @PostMapping("/syncData")
    public IdmResDTO syncData() {
        haikangAcsDataManualSyncService.haikangAcsReaderDataManualSync();
        return IdmResDTO.success(null);
    }

    /**
     * 导出
     */
    @PostMapping("export")
    public IdmResDTO export(@RequestBody HaikangAcsReaderQuery query) {

        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        query.setCompanyId(currentOrgId);

        ExcelDownloadDto<HaikangAcsReaderQuery> dto = ExcelDownloadDto.<HaikangAcsReaderQuery>builder()
                .loginInfo(LoginInfoHolder.getCurrentLoginInfo())
                .query(query)
                .fileName("读卡器导出（" + DateTimeUtil.dateToString(new Date(), "yyyyMMdd") + "）")
                .build();

        excelExportService.excelExport(dto, HaikangAcsReaderExportVo.class,
                new ExcelDownloadCallable<HaikangAcsReaderQuery, HaikangAcsReaderExportVo>() {
                    @Override
                    public IPage<HaikangAcsReaderExportVo> excute(
                            ExcelDownloadDto<HaikangAcsReaderQuery> dto) {
                        IPage<HaikangAcsReaderVo> page = haikangAcsReaderService.queryForPage(
                                query);
                        return page.convert(item -> {
                            HaikangAcsReaderExportVo exportVo = haikangAcsReaderConverter.entityToExportVo(
                                    item);
                            return exportVo;
                        });
                    }
                });

        return IdmResDTO.success();
    }
}
