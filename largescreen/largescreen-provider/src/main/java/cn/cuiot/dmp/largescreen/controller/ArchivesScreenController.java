package cn.cuiot.dmp.largescreen.controller;


import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.service.ApiSystemService;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.req.DepartmentReqDto;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.utils.BeanMapper;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.largescreen.service.dto.StatisInfoReqDTO;
import cn.cuiot.dmp.largescreen.service.dto.archive.BuildingArchivesPageQuery;
import cn.cuiot.dmp.largescreen.service.dto.content.NoticeStatisInfoReqDTO;
import cn.cuiot.dmp.largescreen.service.dto.external.EntranceGuardRecordReqDTO;
import cn.cuiot.dmp.largescreen.service.dto.external.VideoPageQuery;
import cn.cuiot.dmp.largescreen.service.feign.*;
import cn.cuiot.dmp.largescreen.service.vo.*;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 管理端-楼盘可视化大屏
 *
 * @author zc
 */
@RestController
@RequestMapping("/archive/screen")
public class ArchivesScreenController {


    @Autowired
    private ApiSystemService apiSystemService;

    @Autowired
    private ArchiveApiFeignService archiveApiFeignService;

    @Autowired
    private LeaseFeignService leaseFeignService;

    @Autowired
    private ConfigFeignService configFeignService;

    @Autowired
    private ContentFeignService contentFeignService;

    @Autowired
    private ExternalApiFeignService externalApiFeignService;

    /**
     * 查询企业楼盘信息
     *
     * @return BuildingArchive
     */
    @RequiresPermissions
    @PostMapping("/archive/query")
    public IdmResDTO<List<BuildingArchivesVO>> queryArchive(@RequestBody StatisInfoReqDTO statisInfoReqDTO) {
        BuildingArchivesPageQuery buildingArchivesPageQuery = buildStatisticPageReq(statisInfoReqDTO);
        return archiveApiFeignService.queryArchiveInfoList(buildingArchivesPageQuery);
    }


    /**
     * 查询基础档案信息
     *
     * @return BuildingArchive
     */
    @RequiresPermissions
    @PostMapping("/archive/statistic")
    public IdmResDTO<ArchivesStatisticVO> queryArchiveStatistic(@RequestBody StatisInfoReqDTO statisInfoReqDTO) {
        BuildingArchivesPageQuery buildingArchivesPageQuery = buildStatisticPageReq(statisInfoReqDTO);
        IdmResDTO<ArchivesStatisticVO> archivesStatisticVOIdmResDTO = archiveApiFeignService.queryArchiveBaseStatisticInfo(buildingArchivesPageQuery);
        ArchivesStatisticVO res = new ArchivesStatisticVO();
        if (archivesStatisticVOIdmResDTO.getCode().equals(ResultCode.SUCCESS.getCode())) {
            ArchivesStatisticVO data = archivesStatisticVOIdmResDTO.getData();
            res = BeanMapper.copyBean(data, ArchivesStatisticVO.class);
        }
        IdmResDTO<ContractLeaseStatisticVO> leaseStatisticVOIdmResDTO = leaseFeignService.contractLeaseArchiveStatistic(buildingArchivesPageQuery);
        if (leaseStatisticVOIdmResDTO.getCode().equals(ResultCode.SUCCESS.getCode())) {
            ContractLeaseStatisticVO data = leaseStatisticVOIdmResDTO.getData();
            res.setLeased(data.getLeased());
            res.setLeasing(data.getLeasing());
        }
        return IdmResDTO.success(res);
    }


    /**
     * 查询工单统计
     *
     * @return BuildingArchive
     */
    @RequiresPermissions
    @PostMapping("/work/statistic")
    public IdmResDTO<WorkInfoStatisticVO> queryWorkStatistic(@RequestBody StatisInfoReqDTO statisInfoReqDTO) {
        buildStatisticReq(statisInfoReqDTO);
        return configFeignService.queryWorkOrderStatistic(statisInfoReqDTO);
    }


    /**
     * 查看公告分页统计
     * @return
     */
    @RequiresPermissions
    @PostMapping("/notice/queryForPage")
    public IdmResDTO<Page<ContentNoticeVO>> noticeQueryForPage(@RequestBody @Valid NoticeStatisInfoReqDTO noticeReq){
        BuildingArchivesPageQuery pageQuery = buildStatisticPageReq(noticeReq);
        NoticeStatisInfoReqDTO map = BeanMapper.map(pageQuery, NoticeStatisInfoReqDTO.class);
        map.setType(noticeReq.getType());
        return contentFeignService.queryContentNotice(map);
    }


    /**
     * 根据id获取公告详情
     */
    @RequiresPermissions
    @PostMapping("/notice/queryForDetail")
    public IdmResDTO<NoticeVo> noticeQueryForDetail(@RequestBody @Valid IdParam idParam) {
        return contentFeignService.queryForDetail(idParam);
    }


    /**
     * 查询视频信息列表
     * @param query StatisInfoReqDTO
     * @return VideoPageVo
     */
    @RequiresPermissions
    @RequestMapping("/video/queryForPage")
    public IdmResDTO<Page<VideoPageVo>> videoQueryForPage(@RequestBody StatisInfoReqDTO query){
        BuildingArchivesPageQuery pageQuery = buildStatisticPageReq(query);
        VideoPageQuery videoPageQuery = BeanMapper.map(pageQuery, VideoPageQuery.class);
        // 查询在线的设备  1: 未注册    2: 在线    3: 离线
        videoPageQuery.setState(2);
        return externalApiFeignService.queryForPage(videoPageQuery);
    }

    /**
     * 查看物联网设备统计数据
     * @param reqDTO StatisInfoReqDTO
     * @return IOTStatisticVo
     */
    @RequiresPermissions
    @RequestMapping("/iot/queryIotStatistic")
    public IdmResDTO<IOTStatisticVo> queryIotStatistic(@RequestBody StatisInfoReqDTO reqDTO) {
        buildStatisticReq(reqDTO);
        return externalApiFeignService.queryIotStatistic(reqDTO);
    }

    /**
     * 查询通行记录
     * @param query
     * @return
     */
    @RequiresPermissions
    @PostMapping("/entranceGuard/queryForPage")
    public IdmResDTO<Page<EntranceGuardRecordVo>> entranceGuardQueryForPage(@RequestBody EntranceGuardRecordReqDTO query) {
        buildStatisticPageReq(query);
        return externalApiFeignService.entranceGuardQueryForPage(query);
    }


    private BuildingArchivesPageQuery buildStatisticPageReq(StatisInfoReqDTO statisInfoReqDTO) {
        BuildingArchivesPageQuery buildingArchivesReqDTO = new BuildingArchivesPageQuery();
        if (statisInfoReqDTO.getCompanyId() == null) {
            // 设置企业
            buildingArchivesReqDTO.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        }

        if (CollectionUtils.isEmpty(statisInfoReqDTO.getDepartmentIdList())) {
            // 设置部门编码
            Long currentDeptId = LoginInfoHolder.getCurrentDeptId();
            DepartmentReqDto paraDto = new DepartmentReqDto();
            paraDto.setDeptId(currentDeptId);
            paraDto.setSelfReturn(true);
            List<DepartmentDto> departmentDtoList = apiSystemService.lookUpDepartmentChildList(paraDto);
            List<Long> departmentIdList = departmentDtoList.stream()
                    .map(DepartmentDto::getId)
                    .collect(Collectors.toList());
            buildingArchivesReqDTO.setDepartmentIdList(departmentIdList);
        }

        buildingArchivesReqDTO.setIdList(statisInfoReqDTO.getLoupanIds());
        return buildingArchivesReqDTO;
    }

    private void buildStatisticReq(StatisInfoReqDTO statisInfoReqDTO) {

        if (statisInfoReqDTO.getCompanyId() == null) {
            // 设置企业
            statisInfoReqDTO.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        }

        if (CollectionUtils.isEmpty(statisInfoReqDTO.getDepartmentIdList())) {
            // 设置部门编码
            Long currentDeptId = LoginInfoHolder.getCurrentDeptId();
            DepartmentReqDto paraDto = new DepartmentReqDto();
            paraDto.setDeptId(currentDeptId);
            paraDto.setSelfReturn(true);
            List<DepartmentDto> departmentDtoList = apiSystemService.lookUpDepartmentChildList(paraDto);
            List<Long> departmentIdList = departmentDtoList.stream()
                    .map(DepartmentDto::getId)
                    .collect(Collectors.toList());
            statisInfoReqDTO.setDepartmentIdList(departmentIdList);
        }

    }

}
