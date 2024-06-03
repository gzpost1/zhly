package cn.cuiot.dmp.content.service.impl;//	模板

import cn.cuiot.dmp.base.infrastructure.dto.BaseUserDto;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.DepartmentReqDto;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.content.constant.ContentConstants;
import cn.cuiot.dmp.content.conver.NoticeConvert;
import cn.cuiot.dmp.content.dal.entity.ContentAudit;
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
import cn.cuiot.dmp.content.service.ContentAuditService;
import cn.cuiot.dmp.content.service.ContentDataRelevanceService;
import cn.cuiot.dmp.content.service.NoticeService;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private ContentAuditService contentAuditService;

    @Override
    public NoticeVo queryForDetail(Long id) {
        ContentNoticeEntity contentNoticeEntity = this.baseMapper.selectById(id);
        NoticeVo noticeVo = NoticeConvert.INSTANCE.convert(contentNoticeEntity);
        if (!ContentConstants.AuditStatus.AUDIT_ING.equals(contentNoticeEntity.getAuditStatus())) {
            ContentAudit lastAuditResult = contentAuditService.getLastAuditResult(contentNoticeEntity.getId());
            noticeVo.setContentAudit(lastAuditResult);
        }
        return noticeVo;
    }

    @Override
    @Transactional
    public int saveNotice(NoticeCreateDto createDTO) {
        ContentNoticeEntity contentNoticeEntity = NoticeConvert.INSTANCE.convert(createDTO);
        contentNoticeEntity.setStatus(EntityConstants.ENABLED);
        int insert = this.baseMapper.insert(contentNoticeEntity);
        contentDataRelevanceService.batchSaveContentDataRelevance(ContentConstants.DataType.NOTICE, createDTO.getDepartments(), createDTO.getBuildings(), contentNoticeEntity.getId());
        return insert;
    }

    @Override
    @Transactional
    public int updateNotice(NoticeUpdateDto updateDtO) {
        ContentNoticeEntity contentNoticeEntity = NoticeConvert.INSTANCE.convert(updateDtO);
        contentNoticeEntity.setId(updateDtO.getId());
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
