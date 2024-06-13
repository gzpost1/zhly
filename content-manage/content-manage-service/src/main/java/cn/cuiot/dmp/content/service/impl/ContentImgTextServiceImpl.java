package cn.cuiot.dmp.content.service.impl;//	模板


import cn.cuiot.dmp.base.infrastructure.dto.BaseUserDto;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.base.infrastructure.dto.req.DepartmentReqDto;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;
import cn.cuiot.dmp.common.constant.EntityConstants;
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
import cn.cuiot.dmp.content.param.vo.ImgTextVo;
import cn.cuiot.dmp.content.service.ContentDataRelevanceService;
import cn.cuiot.dmp.content.service.ContentImgTextService;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.hutool.core.collection.CollUtil;
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
        imgTextEntity.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        int insert = this.baseMapper.insert(imgTextEntity);
        contentDataRelevanceService.batchSaveContentDataRelevance(ContentConstants.DataType.IMG_TEXT, createDTO.getDepartments(), createDTO.getBuildings(), imgTextEntity.getId());
        return insert;
    }

    @Override
    @Transactional
    public int updateContentImgText(ContentImgTextUpdateDto updateDtO) {
        ContentImgTextEntity imgTextEntity = ImgTextConvert.INSTANCE.convert(updateDtO);
        imgTextEntity.setId(updateDtO.getId());
        int update = this.baseMapper.updateById(imgTextEntity);
        contentDataRelevanceService.batchSaveContentDataRelevance(ContentConstants.DataType.IMG_TEXT, updateDtO.getDepartments(), updateDtO.getBuildings(), imgTextEntity.getId());
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
