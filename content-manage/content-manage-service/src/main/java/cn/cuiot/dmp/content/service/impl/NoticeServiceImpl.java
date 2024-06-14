package cn.cuiot.dmp.content.service.impl;//	模板

import cn.cuiot.dmp.base.infrastructure.dto.BaseUserDto;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.AuditConfigTypeReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.req.BaseUserReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.DepartmentReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.AuditConfigRspDTO;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;
import cn.cuiot.dmp.common.bean.dto.SysMsgDto;
import cn.cuiot.dmp.common.bean.dto.UserMessageAcceptDto;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.MsgDataType;
import cn.cuiot.dmp.common.constant.MsgTypeConstant;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.enums.AuditConfigTypeEnum;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.content.config.MsgChannel;
import cn.cuiot.dmp.content.constant.ContentConstants;
import cn.cuiot.dmp.content.conver.NoticeConvert;
import cn.cuiot.dmp.content.dal.entity.ContentNoticeEntity;
import cn.cuiot.dmp.content.dal.mapper.ContentNoticeMapper;
import cn.cuiot.dmp.content.feign.ArchiveConverService;
import cn.cuiot.dmp.content.feign.SystemConverService;
import cn.cuiot.dmp.content.param.dto.AuditResultDto;
import cn.cuiot.dmp.content.param.dto.NoticeCreateDto;
import cn.cuiot.dmp.content.param.dto.NoticeUpdateDto;
import cn.cuiot.dmp.content.param.query.NoticPageQuery;
import cn.cuiot.dmp.content.param.req.PublishReqVo;
import cn.cuiot.dmp.content.param.vo.NoticeVo;
import cn.cuiot.dmp.content.service.ContentDataRelevanceService;
import cn.cuiot.dmp.content.service.NoticeService;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Async;
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
public class NoticeServiceImpl extends ServiceImpl<ContentNoticeMapper, ContentNoticeEntity> implements NoticeService {

    @Autowired
    private ContentDataRelevanceService contentDataRelevanceService;
    @Autowired
    private SystemConverService systemConverService;
    @Autowired
    private ArchiveConverService archiveConverService;
    @Autowired
    private MsgChannel msgChannel;

    @Override
    public NoticeVo queryForDetail(Long id) {
        ContentNoticeEntity contentNoticeEntity = this.baseMapper.selectById(id);
        NoticeVo noticeVo = NoticeConvert.INSTANCE.convert(contentNoticeEntity);
        return noticeVo;
    }

    @Override
    @Transactional
    public int saveNotice(NoticeCreateDto createDTO) {
        ContentNoticeEntity contentNoticeEntity = NoticeConvert.INSTANCE.convert(createDTO);
        contentNoticeEntity.setStatus(EntityConstants.ENABLED);
        AuditConfigTypeReqDTO reqDTO = new AuditConfigTypeReqDTO().setCompanyId(LoginInfoHolder.getCurrentOrgId())
                .setAuditConfigType(AuditConfigTypeEnum.NOTICE_MANAGE.getCode()).setName("新增公告");
        AuditConfigRspDTO auditConfigRspDTO = systemConverService.lookUpAuditConfig(reqDTO);
        if (auditConfigRspDTO != null && EntityConstants.ENABLED.equals(auditConfigRspDTO.getStatus())) {
            contentNoticeEntity.setAuditStatus(ContentConstants.AuditStatus.AUDIT_ING);
        } else {
            contentNoticeEntity.setAuditStatus(ContentConstants.AuditStatus.AUDIT_PASSED);
        }
        int insert = this.baseMapper.insert(contentNoticeEntity);
        contentDataRelevanceService.batchSaveContentDataRelevance(ContentConstants.DataType.NOTICE, createDTO.getDepartments(), createDTO.getBuildings(), contentNoticeEntity.getId());
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
        contentDataRelevanceService.batchSaveContentDataRelevance(ContentConstants.DataType.NOTICE, updateDtO.getDepartments(), updateDtO.getBuildings(), contentNoticeEntity.getId());
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
            }
            DepartmentReqDto query = new DepartmentReqDto();
            query.setDeptIdList(pageQuery.getDepartments());
            pageQuery.setDepartments(systemConverService.getDeptIds(query));
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
            this.baseMapper.updateById(contentNoticeEntity);
            return true;
        }
        return false;
    }

    @Override
    public IPage<NoticeVo> getAppNoticePage(NoticPageQuery pageQuery) {
        pageQuery.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        initQuery(pageQuery);
        pageQuery.setPublishStatus(ContentConstants.PublishStatus.PUBLISHED);
        PageHelper.orderBy("effective_start_time asc");
        IPage<ContentNoticeEntity> noticeEntityIPage = this.baseMapper.queryForPage(new Page<>(pageQuery.getPageNo(), pageQuery.getPageSize()), pageQuery, ContentConstants.DataType.NOTICE);
        return NoticeConvert.INSTANCE.convert(noticeEntityIPage);
    }

    @Override
    public List<ContentNoticeEntity> queryPublishNotice() {
        LambdaQueryWrapper<ContentNoticeEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ge(ContentNoticeEntity::getEffectiveStartTime, new Date());
        queryWrapper.lt(ContentNoticeEntity::getEffectiveEndTime, new Date());
        queryWrapper.eq(ContentNoticeEntity::getAuditStatus, ContentConstants.AuditStatus.AUDIT_PASSED);
        queryWrapper.eq(ContentNoticeEntity::getStatus, EntityConstants.ENABLED);
        queryWrapper.eq(ContentNoticeEntity::getPublishStatus, ContentConstants.PublishStatus.UNPUBLISHED);
        return list(queryWrapper);
    }

    @Override
    @Async
    //TODO 缺少短信通知
    public void sendNoticeMessage(ContentNoticeEntity noticeEntity) {
        if (ContentConstants.PublishSource.MANAGE.equals(noticeEntity.getPublishSource())) {
            if (CollUtil.isNotEmpty(noticeEntity.getDepartments())) {
                BaseUserReqDto reqDto = new BaseUserReqDto();
                reqDto.setDeptIdList(noticeEntity.getDepartments().stream().map(Long::parseLong).collect(Collectors.toList()));
                List<Long> longs = systemConverService.lookUpUserIds(reqDto);
                if (CollUtil.isNotEmpty(longs) && ContentConstants.MsgInform.SYSTEM.equals(noticeEntity.getInform())) {
                    UserMessageAcceptDto userMessageAcceptDto = new UserMessageAcceptDto().setMsgType(MsgTypeConstant.SYS_MSG).setSysMsgDto(
                            new SysMsgDto().setAcceptors(longs).setDataId(noticeEntity.getId()).setDataType(MsgDataType.NOTICE).setMessage(noticeEntity.getDetail())
                                    .setDataJson(noticeEntity).setMessageTime(new Date()));
                    msgChannel.userMessageOutput().send(MessageBuilder.withPayload(userMessageAcceptDto)
                            .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                            .build());
                }
            }
        } else if (ContentConstants.PublishSource.APP.equals(noticeEntity.getPublishSource())) {
            if (CollUtil.isNotEmpty(noticeEntity.getBuildings())) {
                //TODO 小程序通知
            }
        }
    }

    @Override
    public NoticeVo queryForAppDetail(Long id) {
        ContentNoticeEntity contentNoticeEntity = this.baseMapper.selectById(id);
        NoticeVo noticeVo = NoticeConvert.INSTANCE.convert(contentNoticeEntity);
        Byte publishStatus = NoticeConvert.INSTANCE.checkEffectiveStatus(noticeVo);
        if (ContentConstants.PublishStatus.STOP_PUBLISH.equals(publishStatus) || ContentConstants.PublishStatus.EXPIRED.equals(publishStatus)) {
            throw new BusinessException(ResultCode.EFFECTIVE_TIME_EXPIRED);
        }
        return noticeVo;
    }

    @Override
    public void getMyNotice(Long communityId) {
        NoticPageQuery pageQuery = new NoticPageQuery();
        pageQuery.setBuildings(Collections.singletonList(communityId));
        pageQuery.setPublishStatus(ContentConstants.PublishStatus.PUBLISHED);
        pageQuery.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        List<ContentNoticeEntity> noticeEntityList = this.baseMapper.queryForList(pageQuery, ContentConstants.DataType.NOTICE);
        if (CollUtil.isNotEmpty(noticeEntityList)) {
            noticeEntityList.forEach(this::sendNoticeMessage);
        }
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
