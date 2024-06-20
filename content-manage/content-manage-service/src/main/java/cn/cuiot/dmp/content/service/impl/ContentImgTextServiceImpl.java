package cn.cuiot.dmp.content.service.impl;//	模板


import cn.cuiot.dmp.base.infrastructure.dto.BaseUserDto;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.base.infrastructure.dto.req.AuditConfigTypeReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.req.DepartmentReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.AuditConfigRspDTO;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.enums.AuditConfigTypeEnum;
import cn.cuiot.dmp.content.constant.ContentConstants;
import cn.cuiot.dmp.content.conver.ImgTextConvert;
import cn.cuiot.dmp.content.dal.entity.ContentImgTextEntity;
import cn.cuiot.dmp.content.dal.mapper.ContentImgTextMapper;
import cn.cuiot.dmp.content.feign.ArchiveConverService;
import cn.cuiot.dmp.content.feign.SystemConverService;
import cn.cuiot.dmp.content.param.dto.AuditResultDto;
import cn.cuiot.dmp.content.param.dto.ContentImgTextCreateDto;
import cn.cuiot.dmp.content.param.dto.ContentImgTextUpdateDto;
import cn.cuiot.dmp.content.param.query.ContentImgTextPageQuery;
import cn.cuiot.dmp.content.param.vo.AuditStatusNumVo;
import cn.cuiot.dmp.content.param.vo.ImgTextVo;
import cn.cuiot.dmp.content.service.ContentDataRelevanceService;
import cn.cuiot.dmp.content.service.ContentImgTextService;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
 * @data 2024/5/28 14:42
 */
@Service("imgTextService")
public class ContentImgTextServiceImpl extends ServiceImpl<ContentImgTextMapper, ContentImgTextEntity> implements ContentImgTextService {

    @Autowired
    private ContentDataRelevanceService contentDataRelevanceService;
    @Autowired
    private SystemConverService systemConverService;
    @Autowired
    private ArchiveConverService archiveConverService;


    @Override
    public ImgTextVo queryForDetail(Long id) {
        ContentImgTextEntity contentImgTextEntity = this.baseMapper.selectById(id);
        ImgTextVo imgTextVo = ImgTextConvert.INSTANCE.convert(contentImgTextEntity);
        return imgTextVo;
    }

    @Override
    public List<ImgTextVo> queryForList(ContentImgTextPageQuery pageQuery) {
        initQuery(pageQuery);
        List<ContentImgTextEntity> imgTextEntityList = this.baseMapper.queryForList(pageQuery, ContentConstants.DataType.IMG_TEXT);
        return ImgTextConvert.INSTANCE.convert(imgTextEntityList);
    }

    @Override
    public IPage<ImgTextVo> queryForPage(ContentImgTextPageQuery pageQuery) {
        initQuery(pageQuery);
        IPage<ContentImgTextEntity> imgTextEntityIPage = this.baseMapper.queryForPage(new Page<>(pageQuery.getPageNo(), pageQuery.getPageSize()), pageQuery, ContentConstants.DataType.IMG_TEXT);
        IPage<ImgTextVo> pageResult = new Page<>();
        if (CollUtil.isNotEmpty(imgTextEntityIPage.getRecords())) {
            List<Long> departmentIds = new ArrayList<>();
            List<Long> buildingIds = new ArrayList<>();
            List<Long> creatUserIds = new ArrayList<>();
            imgTextEntityIPage.getRecords().forEach(imgTextEntity -> {
                creatUserIds.add(imgTextEntity.getCreateUser());
                departmentIds.addAll(Optional.ofNullable(imgTextEntity.getDepartments()).orElse(new ArrayList<>()).stream().map(Long::parseLong).collect(Collectors.toList()));
                buildingIds.addAll(Optional.ofNullable(imgTextEntity.getBuildings()).orElse(new ArrayList<>()).stream().map(Long::parseLong).collect(Collectors.toList()));
            });
            HashMap<Long, DepartmentDto> departmentMapByIds = systemConverService.getDepartmentMapByIds(departmentIds);
            HashMap<Long, BuildingArchive> buildingMapByIds = archiveConverService.getBuildingArchiveMapByIds(buildingIds);
            HashMap<Long, BaseUserDto> userMapByIds = systemConverService.getUserMapByIds(creatUserIds);
            pageResult = ImgTextConvert.INSTANCE.convert(imgTextEntityIPage, departmentMapByIds, buildingMapByIds, userMapByIds);
        }
        return pageResult;
    }

    @Override
    @Transactional
    public int saveContentImgText(ContentImgTextCreateDto createDTO) {
        ContentImgTextEntity imgTextEntity = ImgTextConvert.INSTANCE.convert(createDTO);
        imgTextEntity.setStatus(EntityConstants.ENABLED);
        AuditConfigTypeReqDTO reqDTO = new AuditConfigTypeReqDTO().setCompanyId(LoginInfoHolder.getCurrentOrgId())
                .setAuditConfigType(AuditConfigTypeEnum.CONTENT_MANAGE.getCode()).setName("新增图文");
        AuditConfigRspDTO auditConfigRspDTO = systemConverService.lookUpAuditConfig(reqDTO);
        if (auditConfigRspDTO != null && EntityConstants.ENABLED.equals(auditConfigRspDTO.getStatus())) {
            imgTextEntity.setAuditStatus(ContentConstants.AuditStatus.AUDIT_ING);
        } else {
            imgTextEntity.setAuditStatus(ContentConstants.AuditStatus.AUDIT_PASSED);
        }
        imgTextEntity.setAuditStatus(ContentConstants.AuditStatus.AUDIT_ING);
        imgTextEntity.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        int insert = this.baseMapper.insert(imgTextEntity);
        contentDataRelevanceService.batchSaveContentDataRelevance(ContentConstants.DataType.IMG_TEXT, LoginInfoHolder.getCurrentDeptId(), createDTO.getBuildings(), imgTextEntity.getId());
        return insert;
    }

    @Override
    @Transactional
    public int updateContentImgText(ContentImgTextUpdateDto updateDtO) {
        ContentImgTextEntity imgTextEntity = ImgTextConvert.INSTANCE.convert(updateDtO);
        imgTextEntity.setId(updateDtO.getId());
        AuditConfigTypeReqDTO reqDTO = new AuditConfigTypeReqDTO().setCompanyId(LoginInfoHolder.getCurrentOrgId())
                .setAuditConfigType(AuditConfigTypeEnum.CONTENT_MANAGE.getCode()).setName("编辑图文");
        AuditConfigRspDTO auditConfigRspDTO = systemConverService.lookUpAuditConfig(reqDTO);
        if (auditConfigRspDTO != null && EntityConstants.ENABLED.equals(auditConfigRspDTO.getStatus())) {
            imgTextEntity.setAuditStatus(ContentConstants.AuditStatus.AUDIT_ING);
        } else {
            imgTextEntity.setAuditStatus(ContentConstants.AuditStatus.AUDIT_PASSED);
        }
        int update = this.baseMapper.updateById(imgTextEntity);
        contentDataRelevanceService.batchSaveContentDataRelevance(ContentConstants.DataType.IMG_TEXT, LoginInfoHolder.getCurrentDeptId(), updateDtO.getBuildings(), imgTextEntity.getId());
        return update;
    }

    @Override
    public Boolean updateStatus(UpdateStatusParam updateStatusParam) {
        ContentImgTextEntity imgTextEntity = this.getById(updateStatusParam.getId());
        if (imgTextEntity != null) {
            imgTextEntity.setStatus(updateStatusParam.getStatus());
            return this.updateById(imgTextEntity);
        }
        return false;
    }

    @Override
    public List<ContentImgTextEntity> getByTypeId(Long typeId) {
        LambdaQueryWrapper<ContentImgTextEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContentImgTextEntity::getTypeId, typeId);
        return list(queryWrapper);
    }

    @Override
    public List<AuditStatusNumVo> getAuditStatusNum(Long typeId) {
        ContentImgTextPageQuery pageQuery = new ContentImgTextPageQuery();
        initQuery(pageQuery);
        pageQuery.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        pageQuery.setTypeId(typeId);
        return this.baseMapper.getAuditStatusNum(pageQuery, ContentConstants.DataType.IMG_TEXT);
    }

    @Override
    public Boolean dealAuditResult(AuditResultDto auditResultDto) {
        ContentImgTextEntity imgTextEntity = this.baseMapper.selectById(auditResultDto.getId());
        if (imgTextEntity != null) {
            imgTextEntity.setAuditStatus(auditResultDto.getAuditStatus());
            this.baseMapper.updateById(imgTextEntity);
            return true;
        }
        return false;
    }

    private void initQuery(ContentImgTextPageQuery pageQuery) {
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
}