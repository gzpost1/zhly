package cn.cuiot.dmp.content.service.impl;

import cn.cuiot.dmp.base.application.dto.ExcelDownloadDto;
import cn.cuiot.dmp.base.application.service.ExcelExportService;
import cn.cuiot.dmp.base.infrastructure.dto.BaseUserDto;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.*;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.AuditConfigRspDTO;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;
import cn.cuiot.dmp.base.infrastructure.syslog.LogContextHolder;
import cn.cuiot.dmp.base.infrastructure.syslog.OptTargetData;
import cn.cuiot.dmp.base.infrastructure.syslog.OptTargetInfo;
import cn.cuiot.dmp.common.bean.dto.SmsMsgDto;
import cn.cuiot.dmp.common.bean.dto.SysMsgDto;
import cn.cuiot.dmp.common.bean.dto.UserMessageAcceptDto;
import cn.cuiot.dmp.common.constant.*;
import cn.cuiot.dmp.common.enums.AuditConfigTypeEnum;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.content.config.MsgChannel;
import cn.cuiot.dmp.content.constant.ContentConstants;
import cn.cuiot.dmp.content.conver.NoticeConvert;
import cn.cuiot.dmp.content.dal.entity.ContentNoticeEntity;
import cn.cuiot.dmp.content.dal.mapper.ContentNoticeMapper;
import cn.cuiot.dmp.content.feign.ArchiveConverService;
import cn.cuiot.dmp.content.feign.MessageFeignService;
import cn.cuiot.dmp.content.feign.SystemConverService;
import cn.cuiot.dmp.content.param.dto.AuditResultDto;
import cn.cuiot.dmp.content.param.dto.NoticeCreateDto;
import cn.cuiot.dmp.content.param.dto.NoticeUpdateDto;
import cn.cuiot.dmp.content.param.query.NoticPageQuery;
import cn.cuiot.dmp.content.param.req.NoticeStatisInfoReqVo;
import cn.cuiot.dmp.content.param.req.PublishReqVo;
import cn.cuiot.dmp.content.param.vo.ContentNoticeVo;
import cn.cuiot.dmp.content.param.vo.NoticeVo;
import cn.cuiot.dmp.content.param.vo.export.NoticeExportVo;
import cn.cuiot.dmp.content.service.ContentDataRelevanceService;
import cn.cuiot.dmp.content.service.NoticeService;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MimeTypeUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/29 14:35
 */
@Service("noticeService")
@Slf4j
public class NoticeServiceImpl extends ServiceImpl<ContentNoticeMapper, ContentNoticeEntity> implements NoticeService {

    @Autowired
    private ContentDataRelevanceService contentDataRelevanceService;
    @Autowired
    private SystemConverService systemConverService;
    @Autowired
    private ArchiveConverService archiveConverService;
    @Autowired
    private MsgChannel msgChannel;
    @Autowired
    private MessageFeignService messageFeignService;
    @Autowired
    private ExcelExportService excelExportService;

    @Override
    public NoticeVo queryForDetail(Long id) {
        ContentNoticeEntity contentNoticeEntity = this.baseMapper.selectById(id);
        if (contentNoticeEntity == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXIST);
        }
        Byte effectiveStatus = NoticeConvert.INSTANCE.checkEffectiveStatus(contentNoticeEntity);
        NoticeVo noticeVo = NoticeConvert.INSTANCE.convert(contentNoticeEntity);
        noticeVo.setEffectiveStatus(effectiveStatus);
        return noticeVo;
    }

    @Override
    @Transactional
    public int saveNotice(NoticeCreateDto createDTO) {
        ContentNoticeEntity contentNoticeEntity = NoticeConvert.INSTANCE.convert(createDTO);
        contentNoticeEntity.setStatus(EntityConstants.DISABLED);
        contentNoticeEntity.setPublishStatus(ContentConstants.PublishStatus.UNPUBLISHED);
        contentNoticeEntity.setNoticed(ContentConstants.Noticed.UN_NOTICE);
        AuditConfigTypeReqDTO reqDTO = new AuditConfigTypeReqDTO().setCompanyId(LoginInfoHolder.getCurrentOrgId())
                .setAuditConfigType(AuditConfigTypeEnum.NOTICE_MANAGE.getCode()).setName("新增公告");
        AuditConfigRspDTO auditConfigRspDTO = systemConverService.lookUpAuditConfig(reqDTO);
        if (auditConfigRspDTO != null && EntityConstants.ENABLED.equals(auditConfigRspDTO.getStatus())) {
            contentNoticeEntity.setAuditStatus(ContentConstants.AuditStatus.AUDIT_ING);
        } else {
            contentNoticeEntity.setAuditStatus(ContentConstants.AuditStatus.AUDIT_PASSED);
        }
        int insert = this.baseMapper.insert(contentNoticeEntity);
        contentDataRelevanceService.batchSaveContentDataRelevance(ContentConstants.DataType.NOTICE, createDTO.getDepartBuilds(), contentNoticeEntity.getId());
        //设置日志操作对象内容
        LogContextHolder.setOptTargetInfo(OptTargetInfo.builder()
                .name("公告")
                .targetDatas(Lists.newArrayList(new OptTargetData(contentNoticeEntity.getTitle(), contentNoticeEntity.getId().toString())))
                .build());

        return insert;
    }

    @Override
    @Transactional
    public int updateNotice(NoticeUpdateDto updateDtO) {
        ContentNoticeEntity contentNoticeEntity = NoticeConvert.INSTANCE.convert(updateDtO);
        contentNoticeEntity.setId(updateDtO.getId());
        AuditConfigTypeReqDTO reqDTO = new AuditConfigTypeReqDTO().setCompanyId(LoginInfoHolder.getCurrentOrgId())
                .setAuditConfigType(AuditConfigTypeEnum.NOTICE_MANAGE.getCode()).setName("编辑公告");
        AuditConfigRspDTO auditConfigRspDTO = systemConverService.lookUpAuditConfig(reqDTO);
        if (auditConfigRspDTO != null && EntityConstants.ENABLED.equals(auditConfigRspDTO.getStatus())) {
            contentNoticeEntity.setAuditStatus(ContentConstants.AuditStatus.AUDIT_ING);
        } else {
            contentNoticeEntity.setAuditStatus(ContentConstants.AuditStatus.AUDIT_PASSED);
        }
        int update = this.baseMapper.updateById(contentNoticeEntity);
        contentDataRelevanceService.batchSaveContentDataRelevance(ContentConstants.DataType.NOTICE, updateDtO.getDepartBuilds(), contentNoticeEntity.getId());
        //设置日志操作对象内容
        LogContextHolder.setOptTargetInfo(OptTargetInfo.builder()
                .name("公告")
                .targetDatas(Lists.newArrayList(new OptTargetData(contentNoticeEntity.getTitle(), contentNoticeEntity.getId().toString())))
                .build());
        return update;
    }

    @Override
    public List<NoticeVo> queryForList(NoticPageQuery pageQuery) {
        initQuery(pageQuery);
        List<ContentNoticeEntity> noticeEntityList = this.baseMapper.queryForList(pageQuery, ContentConstants.DataType.NOTICE);
        return NoticeConvert.INSTANCE.convert(noticeEntityList);
    }

    private void initQuery(NoticPageQuery pageQuery) {
        if (CollUtil.isEmpty(pageQuery.getBuildings())) {
            if (CollUtil.isEmpty(pageQuery.getDepartments())) {
                List<Long> departments = Collections.singletonList(LoginInfoHolder.getCurrentDeptId());
                pageQuery.setDepartments(departments);
                DepartmentReqDto query = new DepartmentReqDto();
                query.setDeptIdList(pageQuery.getDepartments());
                pageQuery.setDepartments(systemConverService.getDeptIds(query));
            }
        }
    }

    @Override
    public IPage<NoticeVo> queryForPage(NoticPageQuery pageQuery) {
        initQuery(pageQuery);
        PageHelper.orderBy("create_time desc");
        IPage<ContentNoticeEntity> noticeEntityIPage = this.baseMapper.queryForPage(new Page<>(pageQuery.getPageNo(), pageQuery.getPageSize()), pageQuery, ContentConstants.DataType.NOTICE);
        IPage<NoticeVo> pageResult = new Page<>();
        if (CollUtil.isNotEmpty(noticeEntityIPage.getRecords())) {
            List<Long> departmentIds = new ArrayList<>();
            List<Long> buildingIds = new ArrayList<>();
            List<Long> creatUserIds = new ArrayList<>();
            noticeEntityIPage.getRecords().forEach(contentNoticeEntity -> {
                creatUserIds.add(contentNoticeEntity.getCreateUser());
                departmentIds.addAll(Optional.ofNullable(contentNoticeEntity.getDepartments()).orElse(new ArrayList<>()).stream().map(Long::parseLong).collect(Collectors.toList()));
                buildingIds.addAll(Optional.ofNullable(contentNoticeEntity.getBuildings()).orElse(new ArrayList<>()).stream().map(Long::parseLong).collect(Collectors.toList()));
            });
            HashMap<Long, DepartmentDto> departmentMapByIds = systemConverService.getDepartmentMapByIds(departmentIds);
            HashMap<Long, BuildingArchive> buildingMapByIds = archiveConverService.getBuildingArchiveMapByIds(buildingIds);
            HashMap<Long, BaseUserDto> userMapByIds = systemConverService.getUserMapByIds(creatUserIds);
            pageResult = NoticeConvert.INSTANCE.convert(noticeEntityIPage, departmentMapByIds, buildingMapByIds, userMapByIds);
        }
        return pageResult;
    }

    @Override
    public Boolean publish(PublishReqVo publishReqVo) {
        ContentNoticeEntity contentNoticeEntity = this.baseMapper.selectById(publishReqVo.getId());
        if (contentNoticeEntity != null) {
            if (ContentConstants.PublishStatus.PUBLISHED.equals(publishReqVo.getPublishStatus())) {
                if (DateUtil.compare(contentNoticeEntity.getEffectiveEndTime(), new Date()) < 0) {
                    throw new BusinessException(ResultCode.EFFECTIVE_TIME_EXPIRED);
                }
            }
            contentNoticeEntity.setPublishStatus(publishReqVo.getPublishStatus());
            contentNoticeEntity.setStatus(EntityConstants.ENABLED);
            this.baseMapper.updateById(contentNoticeEntity);
            //设置日志操作对象内容
            LogContextHolder.setOptTargetInfo(OptTargetInfo.builder()
                    .name("公告")
                    .targetDatas(Lists.newArrayList(new OptTargetData(contentNoticeEntity.getTitle(), contentNoticeEntity.getId().toString())))
                    .build());
            return true;
        }
        return false;
    }

    @Override
    public IPage<NoticeVo> getAppNoticePage(NoticPageQuery pageQuery) {
        initQuery(pageQuery);
        pageQuery.setPublishStatus(ContentConstants.PublishStatus.PUBLISHED);
        pageQuery.setStatus(EntityConstants.ENABLED);
        PageHelper.orderBy("effective_start_time desc");
        IPage<ContentNoticeEntity> noticeEntityIPage = this.baseMapper.queryForPage(new Page<>(pageQuery.getPageNo(), pageQuery.getPageSize()), pageQuery, ContentConstants.DataType.NOTICE);
        return NoticeConvert.INSTANCE.convert(noticeEntityIPage);
    }

    @Override
    public List<ContentNoticeEntity> queryPublishNotice() {
        LambdaQueryWrapper<ContentNoticeEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.lt(ContentNoticeEntity::getEffectiveStartTime, new Date());
        queryWrapper.ge(ContentNoticeEntity::getEffectiveEndTime, new Date());
        queryWrapper.eq(ContentNoticeEntity::getAuditStatus, ContentConstants.AuditStatus.AUDIT_PASSED);
        queryWrapper.eq(ContentNoticeEntity::getStatus, EntityConstants.ENABLED);
        queryWrapper.eq(ContentNoticeEntity::getPublishStatus, ContentConstants.PublishStatus.PUBLISHED);
        queryWrapper.eq(ContentNoticeEntity::getNoticed, ContentConstants.Noticed.UN_NOTICE);
        return list(queryWrapper);
    }

    @Override
    public void sendNoticeMessage(ContentNoticeEntity noticeEntity) {
        log.info("sendNoticeMessage-params:{}", JsonUtil.writeValueAsString(noticeEntity));
        if (ContentConstants.PublishSource.MANAGE.equals(noticeEntity.getPublishSource())) {
            if (CollUtil.isNotEmpty(noticeEntity.getDepartments())) {
                BaseUserReqDto reqDto = new BaseUserReqDto();
                reqDto.setDeptIdList(noticeEntity.getDepartments().stream().map(Long::parseLong).collect(Collectors.toList()));
                List<Long> longs = systemConverService.lookUpUserIds(reqDto);
                sendMsg(noticeEntity, longs, null);
            }
        } else if (ContentConstants.PublishSource.APP.equals(noticeEntity.getPublishSource())) {
            if (CollUtil.isNotEmpty(noticeEntity.getBuildings())) {
                UserHouseAuditBuildingReqDTO dto = new UserHouseAuditBuildingReqDTO();
                dto.setBuildingIds(noticeEntity.getBuildings().stream().map(Long::parseLong).collect(Collectors.toList()));
                Map<Long, List<Long>> map = systemConverService.lookUpUserIdsByBuildingIds(dto);
                for (String building : noticeEntity.getBuildings()) {
                    if (map.containsKey(Long.parseLong(building))) {
                        List<Long> longs = map.get(Long.parseLong(building));
                        sendMsg(noticeEntity, longs, Long.parseLong(building));
                    }
                }
            }
        }
    }

    private void sendMsg(ContentNoticeEntity noticeEntity, List<Long> longs, Long building) {
        log.info("sendMsg-params:noticeEntity:{},userIds:{}", JsonUtil.writeValueAsString(noticeEntity), longs);
        if (CollUtil.isEmpty(longs) || CollUtil.isEmpty(noticeEntity.getInform())) {
            log.info("notice-sendMsg:userid is entity -> return");
            return;
        }
        if (noticeEntity.getInform().contains(ContentConstants.MsgInform.SYSTEM) && noticeEntity.getInform().contains(ContentConstants.MsgInform.SMS)) {
            UserMessageAcceptDto userMessageAcceptDto = new UserMessageAcceptDto().setMsgType((byte) (InformTypeConstant.SYS_MSG + InformTypeConstant.SMS));
            SysMsgDto sysMsgDto = new SysMsgDto().setAcceptors(longs).setDataId(noticeEntity.getId()).setDataType(MsgDataType.NOTICE).setMessage("你收到一条公告通知，公告名称:" + noticeEntity.getTitle() + ",点击查看详情")
                    .setDataJson(noticeEntity).setMessageTime(new Date()).setMsgType(MsgTypeConstant.NOTICE).setBuildingId(building);
            SmsMsgDto smsMsgDto = new SmsMsgDto();
            smsMsgDto.setUserIds(longs).setTemplateId(SmsStdTemplate.MANAGE_NOTICE).setParams(Collections.singletonList(noticeEntity.getTitle()));
            userMessageAcceptDto.setSysMsgDto(sysMsgDto).setSmsMsgDto(smsMsgDto);
            log.info("notice-sendMsg-userMessageAcceptDto:{}", JsonUtil.writeValueAsString(userMessageAcceptDto));
            msgChannel.userMessageOutput().send(MessageBuilder.withPayload(userMessageAcceptDto)
                    .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build());
        } else if (noticeEntity.getInform().contains(ContentConstants.MsgInform.SYSTEM)) {
            SysMsgDto sysMsgDto = new SysMsgDto().setAcceptors(longs).setDataId(noticeEntity.getId()).setDataType(MsgDataType.NOTICE).setMessage("你收到一条公告通知，公告名称:" + noticeEntity.getTitle() + ",点击查看详情")
                    .setDataJson(noticeEntity).setMessageTime(new Date()).setMsgType(MsgTypeConstant.NOTICE).setBuildingId(building);
            UserMessageAcceptDto userMessageAcceptDto = new UserMessageAcceptDto().setMsgType(InformTypeConstant.SYS_MSG).setSysMsgDto(sysMsgDto);
            log.info("notice-sendMsg-userMessageAcceptDto:{}", JsonUtil.writeValueAsString(userMessageAcceptDto));
            msgChannel.userMessageOutput().send(MessageBuilder.withPayload(userMessageAcceptDto)
                    .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                    .build());
        } else if (noticeEntity.getInform().contains(ContentConstants.MsgInform.SMS)) {
            SmsMsgDto smsMsgDto = new SmsMsgDto();
            smsMsgDto.setUserIds(longs).setTemplateId(SmsStdTemplate.MANAGE_NOTICE).setParams(Collections.singletonList(noticeEntity.getTitle()));
            UserMessageAcceptDto userMessageAcceptDto = new UserMessageAcceptDto().setMsgType(InformTypeConstant.SMS);
            log.info("notice-sendMsg-userMessageAcceptDto:{}", JsonUtil.writeValueAsString(smsMsgDto));
            msgChannel.userMessageOutput().send(MessageBuilder.withPayload(userMessageAcceptDto)
                    .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build());
            log.info("notice-sendMsg-userMessageAcceptDto:{}", "");
        }
    }

    @Override
    public NoticeVo queryForAppDetail(Long id) {
        ContentNoticeEntity contentNoticeEntity = this.baseMapper.selectById(id);
        if (contentNoticeEntity == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXIST);
        }
        NoticeVo noticeVo = NoticeConvert.INSTANCE.convert(contentNoticeEntity);
        Byte effectiveStatus = NoticeConvert.INSTANCE.checkEffectiveStatus(noticeVo);
        if (ContentConstants.PublishStatus.UNPUBLISHED.equals(noticeVo.getPublishStatus()) || !EntityConstants.NORMAL.equals(effectiveStatus)) {
            throw new BusinessException(ResultCode.EFFECTIVE_TIME_EXPIRED);
        }
        return noticeVo;
    }

    @Override
    public void getMyNotice(Long communityId) {
        MsgExistDataIdReqDto reqDto = new MsgExistDataIdReqDto().setAccepter(LoginInfoHolder.getCurrentUserId()).setDataType(MsgDataType.NOTICE);
        IdmResDTO<List<Long>> acceptDataIdList = messageFeignService.getAcceptDataIdList(reqDto);
        NoticPageQuery pageQuery = new NoticPageQuery();
        pageQuery.setIdNotIn(acceptDataIdList.getData());
        pageQuery.setBuildings(Collections.singletonList(communityId));
        pageQuery.setPublishStatus(ContentConstants.PublishStatus.PUBLISHED);
        pageQuery.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        pageQuery.setAuditStatus(ContentConstants.AuditStatus.AUDIT_PASSED);
        pageQuery.setStatus(EntityConstants.ENABLED);
        List<ContentNoticeEntity> noticeEntityList = this.baseMapper.queryForList(pageQuery, ContentConstants.DataType.NOTICE);
        if (CollUtil.isNotEmpty(noticeEntityList)) {
            noticeEntityList.forEach(noticeEntity -> {
                UserMessageAcceptDto userMessageAcceptDto = new UserMessageAcceptDto().setMsgType(InformTypeConstant.SYS_MSG).setSysMsgDto(
                        new SysMsgDto().setAcceptors(Collections.singletonList(LoginInfoHolder.getCurrentUserId())).setDataId(noticeEntity.getId()).setDataType(MsgDataType.NOTICE).setMessage(noticeEntity.getDetail())
                                .setDataJson(noticeEntity).setMessageTime(new Date()).setMsgType(MsgTypeConstant.NOTICE));
                msgChannel.userMessageOutput().send(MessageBuilder.withPayload(userMessageAcceptDto)
                        .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                        .build());
            });
        }
    }

    @Override
    public IPage<ContentNoticeVo> queryContentNoticeStatistic(NoticeStatisInfoReqVo dto) {
        if(dto.getType().equals(ContentConstants.PublishSource.MANAGE.intValue())){
            // 管理端不根据楼盘查询信息
            dto.setLoupanIds(null);
            return this.baseMapper.queryContentAdminNoticeStatistic(new Page<>(dto.getPageNo(), dto.getPageSize()),dto);
        }
        if(dto.getType().equals(ContentConstants.PublishSource.APP.intValue())){
            return this.baseMapper.queryContentAppNoticeStatistic(new Page<>(dto.getPageNo(), dto.getPageSize()),dto);
        }
        return new Page<>();
    }

    @Override
    public void export(NoticPageQuery pageQuery) {
        ExcelDownloadDto<NoticPageQuery> excelDownloadDto = null;
        if (ContentConstants.PublishStatus.UNPUBLISHED.equals(pageQuery.getPublishStatus())) {
            excelDownloadDto = ExcelDownloadDto.<NoticPageQuery>builder().loginInfo(LoginInfoHolder.getCurrentLoginInfo()).query(pageQuery)
                    .title("未发布公告列表").fileName("未发布公告导出"+ "("+ DateTimeUtil.dateToString(new Date(), "yyyyMMdd")+")").sheetName("未发布公告列表").build();
        } else if (ContentConstants.PublishStatus.PUBLISHED.equals(pageQuery.getPublishStatus())) {
            excelDownloadDto = ExcelDownloadDto.<NoticPageQuery>builder().loginInfo(LoginInfoHolder.getCurrentLoginInfo()).query(pageQuery)
                    .title("已发布公告列表").fileName("已发布公告导出"+ "("+ DateTimeUtil.dateToString(new Date(), "yyyyMMdd")+")").sheetName("已发布公告列表").build();
        } else {
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "传入的发布状态不对");
        }
        excelExportService.excelExport(excelDownloadDto, NoticeExportVo.class, this::executePageQuery);
    }

    private IPage<NoticeExportVo> executePageQuery(ExcelDownloadDto<NoticPageQuery> noticPageQueryExcelDownloadDto) {
        NoticPageQuery pageQuery = noticPageQueryExcelDownloadDto.getQuery();
        IPage<NoticeVo> pageResult = this.queryForPage(pageQuery);
        List<Long> typeIds = pageResult.getRecords().stream().map(o -> {
            return Long.parseLong(o.getType());
        }).distinct().collect(Collectors.toList());
        CustomConfigDetailReqDTO customConfigDetailReqDTO = new CustomConfigDetailReqDTO();
        customConfigDetailReqDTO.setCustomConfigDetailIdList(typeIds);
        Map<Long, String> typesMap = systemConverService.batchQueryCustomConfigDetailsForMap(customConfigDetailReqDTO);
        return pageResult.convert(o -> {
            NoticeExportVo exportVo = new NoticeExportVo();
            BeanUtil.copyProperties(o, exportVo);
            exportVo.setTypeName(typesMap.get(Long.parseLong(o.getType())));
            return exportVo;
        });
    }

    @Override
    public Boolean dealAuditResult(AuditResultDto auditResultDto) {
        ContentNoticeEntity contentNoticeEntity = this.baseMapper.selectById(auditResultDto.getId());
        if (contentNoticeEntity != null) {
            contentNoticeEntity.setAuditStatus(auditResultDto.getAuditStatus());
            this.baseMapper.updateById(contentNoticeEntity);
            return true;
        }
        return false;
    }

}
