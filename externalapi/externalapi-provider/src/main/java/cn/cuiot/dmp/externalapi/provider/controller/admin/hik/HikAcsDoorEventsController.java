package cn.cuiot.dmp.externalapi.provider.controller.admin.hik;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.dto.ExcelDownloadCallable;
import cn.cuiot.dmp.base.application.dto.ExcelDownloadDto;
import cn.cuiot.dmp.base.application.service.ExcelExportService;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.externalapi.service.query.hik.HikAcsDoorEventsPageQuery;
import cn.cuiot.dmp.externalapi.service.query.hik.HikIrdsResourcesByParamsQuery;
import cn.cuiot.dmp.externalapi.service.query.hik.HikOrgListQuery;
import cn.cuiot.dmp.externalapi.service.service.hik.HikAcsDoorEventsService;
import cn.cuiot.dmp.externalapi.service.service.hik.HikPersonService;
import cn.cuiot.dmp.externalapi.service.vo.hik.HikAcsDoorEventsPageVO;
import cn.cuiot.dmp.externalapi.service.vo.hik.HikCommonResourcesVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 后台-海康事件
 *
 * @Author: zc
 * @Date: 2024-10-11
 */
@RestController
@RequestMapping("/hik/acsDoorEvent")
public class HikAcsDoorEventsController {

    @Autowired
    private HikAcsDoorEventsService hikAcsDoorEventsService;
    @Autowired
    private HikPersonService hikPersonService;
    @Autowired
    private ExcelExportService excelExportService;

    /**
     * 分页
     *
     * @return IPage
     * @Param query 参数
     */
    @RequiresPermissions
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<HikAcsDoorEventsPageVO>> queryForPage(@RequestBody HikAcsDoorEventsPageQuery query) {
        return IdmResDTO.success(hikAcsDoorEventsService.queryForPage(query));
    }

    /**
     * 查询组织列表
     *
     * @return List
     * @Param query 参数
     */
    @RequiresPermissions
    @PostMapping("/queryOrgList")
    public IdmResDTO<List<HikCommonResourcesVO>> queryOrgList(@RequestBody HikOrgListQuery query) {
        return IdmResDTO.success(hikPersonService.queryOrgList(query));
    }

    /**
     * 查询控制器列表
     *
     * @return List<HikIrdsResourcesByParamsVO>
     * @Param query 参数
     */
    @RequiresPermissions
    @PostMapping("/queryAcsDeviceList")
    public IdmResDTO<List<HikCommonResourcesVO>> queryAcsDeviceList(HikIrdsResourcesByParamsQuery query) {
        return IdmResDTO.success(hikAcsDoorEventsService.queryAcsDeviceList(query));
    }

    /**
     * 导出
     *
     * @Param query 参数
     */
    @RequiresPermissions
    @PostMapping("/export")
    public IdmResDTO<?> export(@RequestBody HikAcsDoorEventsPageQuery query) {

        ExcelDownloadDto<HikAcsDoorEventsPageQuery> dto = ExcelDownloadDto.<HikAcsDoorEventsPageQuery>builder()
                .loginInfo(LoginInfoHolder.getCurrentLoginInfo())
                .query(query)
                .fileName("人员导出（" + DateTimeUtil.dateToString(new Date(), "yyyyMMdd") + "）")
                .build();

        excelExportService.excelExport(dto, HikAcsDoorEventsPageVO.class,
                new ExcelDownloadCallable<HikAcsDoorEventsPageQuery, HikAcsDoorEventsPageVO>() {
                    @Override
                    public IPage<HikAcsDoorEventsPageVO> excute(
                            ExcelDownloadDto<HikAcsDoorEventsPageQuery> dto) {

                        return hikAcsDoorEventsService.queryForPage(dto.getQuery());
                    }
                });
        return IdmResDTO.success();
    }
}
