package cn.cuiot.dmp.externalapi.service.service.hik;

import cn.cuiot.dmp.common.bean.external.HIKEntranceGuardBO;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.externalapi.service.entity.hik.HikPersonEntity;
import cn.cuiot.dmp.externalapi.service.mapper.hik.HikPersonMapper;
import cn.cuiot.dmp.externalapi.service.query.hik.*;
import cn.cuiot.dmp.externalapi.service.vendor.hik.HikApiFeignService;
import cn.cuiot.dmp.externalapi.service.vendor.hik.bean.req.*;
import cn.cuiot.dmp.externalapi.service.vendor.hik.bean.resp.HikAcpsAuthConfigSearchResp;
import cn.cuiot.dmp.externalapi.service.vendor.hik.bean.resp.HikDoorResp;
import cn.cuiot.dmp.externalapi.service.vendor.hik.bean.resp.HikOrgListResp;
import cn.cuiot.dmp.externalapi.service.vo.hik.*;
import cn.cuiot.dmp.util.Sm4;
import cn.hutool.core.codec.Base64;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 海康-人员信息 业务层
 *
 * @Author: zc
 * @Date: 2024-10-09
 */
@Service
public class HikPersonService extends ServiceImpl<HikPersonMapper, HikPersonEntity> {

    @Autowired
    private HikApiFeignService hikApiFeignService;
    @Autowired
    private HikCommonHandle hikCommonHandle;

    /**
     * 区域查询默认分页大小
     */
    private final static Long DEFAULT_PAGE_SIZE = 1000L;

    /**
     * 创建人员基础信息 Step 1
     *
     * @return Long 主键id
     * @Param dto 参数
     */
    public Long createPersonInfo(HikPersonInfoCreateDto dto) {
        //企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        // id
        long id = IdWorker.getId();

        // 获取当前企业的对接配置
        HIKEntranceGuardBO bo = hikCommonHandle.queryHikConfigByPlatfromInfo(companyId);

        HikPersonAddReq req = new HikPersonAddReq();
        BeanUtils.copyProperties(dto, req);
        req.setPersonId(id + "");
        // 调用外部接口添加人员信息
        hikApiFeignService.personAdd(req, bo);

        HikPersonEntity entity = new HikPersonEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(id);
        entity.setCompanyId(companyId);
        entity.setFaceDataStatus(EntityConstants.NO);
        if (StringUtils.isNotBlank(dto.getPhoneNo())) {
            entity.setPhoneNo(Sm4.encryption(dto.getPhoneNo()));
        }
        if (StringUtils.isNotBlank(dto.getCertificateNo())) {
            entity.setCertificateNo(Sm4.encryption(dto.getCertificateNo()));
        }
        save(entity);

        return id;
    }

    /**
     * 更新用户信息
     *
     * @return Long 主键id
     * @Param dto 参数
     */
    public Long updatePersonInfo(HikPersonInfoUpdateDto dto) {
        // 企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        // 获取当前企业的对接配置
        HIKEntranceGuardBO bo = hikCommonHandle.queryHikConfigByPlatfromInfo(companyId);

        // 远程调用修改用户信息
        HikPersonUpdateReq req = new HikPersonUpdateReq();
        BeanUtils.copyProperties(dto, req);
        req.setPersonId(dto.getId() + "");
        hikApiFeignService.personUpdate(req, bo);

        HikPersonEntity entity = getHikPersonEntity(dto.getId(), companyId);
        BeanUtils.copyProperties(dto, entity);
        if (StringUtils.isNotBlank(dto.getPhoneNo())) {
            entity.setPhoneNo(Sm4.encryption(dto.getPhoneNo()));
        }
        if (StringUtils.isNotBlank(dto.getCertificateNo())) {
            entity.setCertificateNo(Sm4.encryption(dto.getCertificateNo()));
        }
        updateById(entity);

        return entity.getId();
    }

    /**
     * 编辑照片信息 Step 2
     *
     * @return 人员信息id
     * @Param dto 参数
     */
    public Long editFaceData(HikPersonFaceDataDto dto) {
        // 企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        // 获取人员信息
        HikPersonEntity entity = getHikPersonEntity(dto.getId(), companyId);
        // 获取当前企业的对接配置
        HIKEntranceGuardBO bo = hikCommonHandle.queryHikConfigByPlatfromInfo(companyId);

        // 调用外部接口编辑照片
        HikFaceSingleUpdateReq req = new HikFaceSingleUpdateReq();
        req.setFaceId(entity.getId() + "");
        req.setFaceData(Base64.encode(dto.getFaceData().getBytes(StandardCharsets.UTF_8)));
        hikApiFeignService.faceSingleUpdate(req, bo);

        entity.setFaceData(dto.getFaceData());
        entity.setFaceDataStatus(EntityConstants.YES);
        updateById(entity);

        return entity.getId();
    }

    /**
     * 编辑授权配置 Step 3
     *
     * @Param dto 参数
     */
    public void editAuthorize(HikPersonAuthorizeDto dto) {
        // 人员id列表
        List<Long> personIds = dto.getIds();

        // 检查有效期
        personIds.forEach(item -> {
            if (Objects.equals(dto.getValidityType(), EntityConstants.ENABLED)) {
                if (Objects.isNull(dto.getBeginTime()) || Objects.isNull(dto.getEndTime())) {
                    throw new BusinessException(ResultCode.ERROR, "权限有效期为自定义有效期时，开始结束时间不能为空");
                }
                if (dto.getBeginTime().isAfter(dto.getEndTime())) {
                    throw new BusinessException(ResultCode.ERROR, "权限有效期结束日期必须大于开始日期");
                }
            }
        });

        // 企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        // 获取当前企业对接配置
        HIKEntranceGuardBO bo = hikCommonHandle.queryHikConfigByPlatfromInfo(companyId);

        // 批量删除用户授权
        batchDeleteAuthorize(personIds, bo);

        // 批量设置授权信息
        batchAddAuthorize(dto, bo);

        List<HikPersonEntity> collect = dto.getIds().stream().map(personId -> {
            // 获取人员信息
            HikPersonEntity entity = getHikPersonEntity(personId, companyId);
            entity.setValidityType(dto.getValidityType());
            entity.setBeginTime(dto.getBeginTime());
            entity.setEndTime(dto.getEndTime());
            return entity;
        }).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(collect)) {
            updateBatchById(collect);
        }
    }

    /**
     * 分页查询
     *
     * @return IPage<HikPersonEntity>
     * @Param query 参数
     */
    public IPage<HikPersonPageVO> queryForPage(HikPersonPageQuery query) {
        // 企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        // 获取当前企业对接配置
        HIKEntranceGuardBO bo = hikCommonHandle.queryHikConfigByPlatfromInfo(companyId);

        LambdaQueryWrapper<HikPersonEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HikPersonEntity::getCompanyId, companyId);
        wrapper.eq(StringUtils.isNotBlank(query.getPersonName()), HikPersonEntity::getPersonName, query.getPersonName());
        wrapper.eq(Objects.nonNull(query.getId()), HikPersonEntity::getId, query.getId());
        wrapper.eq(StringUtils.isNotBlank(query.getJobNo()), HikPersonEntity::getJobNo, query.getJobNo());
        wrapper.eq(StringUtils.isNotBlank(query.getOrgIndexCode()), HikPersonEntity::getOrgIndexCode, query.getOrgIndexCode());
        wrapper.eq(Objects.nonNull(query.getFaceDataStatus()), HikPersonEntity::getFaceDataStatus, query.getFaceDataStatus());
        wrapper.orderByDesc(HikPersonEntity::getCreateTime);

        IPage<HikPersonPageVO> iPage = page(new Page<>(query.getPageNo(), query.getPageSize()), wrapper).convert(item -> {
            HikPersonPageVO vo = new HikPersonPageVO();
            BeanUtils.copyProperties(item, vo);
            return vo;
        });

        // 设置组织信息
        if (Objects.nonNull(iPage) && CollectionUtils.isNotEmpty(iPage.getRecords())) {
            // 远程请求组织信息
            List<String> orgIndexCodes = iPage.getRecords().stream()
                    .map(HikPersonPageVO::getOrgIndexCode)
                    .distinct().collect(Collectors.toList());

            HikOrgListReq req = new HikOrgListReq();
            req.setOrgIndexCodes(String.join(",", orgIndexCodes));
            List<HikOrgListResp.DataItem> orgList = queryOrgList(req, bo);
            Map<String, HikOrgListResp.DataItem> orgMap = orgList.stream()
                    .collect(Collectors.toMap(HikOrgListResp.DataItem::getOrgIndexCode, e -> e));

            // 远程请求人员授权信息
            List<Long> personIds = iPage.getRecords().stream().map(HikPersonPageVO::getId).collect(Collectors.toList());
            List<HikAcpsAuthConfigSearchResp.PermissionConfig> authorizeByPersonIds = queryAuthorizeByPersonIds(personIds, bo);
            Map<String, Long> authorizeMap = authorizeByPersonIds.stream()
                    .collect(Collectors.groupingBy(HikAcpsAuthConfigSearchResp.PermissionConfig::getPersonDataId, Collectors.counting()));

            iPage.getRecords().forEach(item -> {
                item.setOrgName(orgMap.containsKey(item.getOrgIndexCode()) ? orgMap.get(item.getOrgIndexCode()).getOrgName() : null);
                item.setAuthorizeStatus(authorizeMap.containsKey(item.getId() + "") ? EntityConstants.YES : EntityConstants.NO);
                if (StringUtils.isNotBlank(item.getPhoneNo())) {
                    item.setPhoneNo(Sm4.decrypt(item.getPhoneNo()));
                }
            });
        }
        return iPage;
    }

    /**
     * app-分页
     *
     * @return IPage
     * @Param query 参数
     */
    public IPage<HikPersonAppPageVO> queryAppForPage(HikPersonAppPageQuery query) {
        // 企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        // 获取当前企业对接配置
        HIKEntranceGuardBO bo = hikCommonHandle.queryHikConfigByPlatfromInfo(companyId);

        LambdaQueryWrapper<HikPersonEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HikPersonEntity::getCompanyId, companyId);
        wrapper.eq(StringUtils.isNotBlank(query.getPersonName()), HikPersonEntity::getPersonName, query.getPersonName());
        wrapper.eq(StringUtils.isNotBlank(query.getOrgIndexCode()), HikPersonEntity::getOrgIndexCode, query.getOrgIndexCode());
        wrapper.orderByDesc(HikPersonEntity::getCreateTime);

        IPage<HikPersonAppPageVO> iPage = page(new Page<>(query.getPageNo(), query.getPageSize()), wrapper).convert(item -> {
            HikPersonAppPageVO vo = new HikPersonAppPageVO();
            BeanUtils.copyProperties(item, vo);
            return vo;
        });

        // 设置组织信息
        if (Objects.nonNull(iPage) && CollectionUtils.isNotEmpty(iPage.getRecords())) {
            // 远程请求组织信息
            List<String> orgIndexCodes = iPage.getRecords().stream()
                    .map(HikPersonAppPageVO::getOrgIndexCode)
                    .distinct().collect(Collectors.toList());

            HikOrgListReq req = new HikOrgListReq();
            req.setOrgIndexCodes(String.join(",", orgIndexCodes));
            List<HikOrgListResp.DataItem> orgList = queryOrgList(req, bo);
            Map<String, HikOrgListResp.DataItem> orgMap = orgList.stream()
                    .collect(Collectors.toMap(HikOrgListResp.DataItem::getOrgIndexCode, e -> e));

            iPage.getRecords().forEach(item ->
                    item.setOrgName(orgMap.containsKey(item.getOrgIndexCode()) ? orgMap.get(item.getOrgIndexCode()).getOrgName() : null));
        }
        return iPage;
    }

    /**
     * 查询详情
     *
     * @return HikPersonEntity
     * @Param id 人员id
     */
    public HikPersonEntity queryForDetail(Long id) {
        // 企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        // 获取人员信息
        return getHikPersonEntity(id, companyId);
    }

    /**
     * 根据人员id查询照片信息
     *
     * @return 照片
     * @Param id 人员id
     */
    public String queryFaceData(Long id) {
        // 企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        // 获取人员信息
        HikPersonEntity entity = getHikPersonEntity(id, companyId);
        return Objects.nonNull(entity) ? entity.getFaceData() : null;
    }

    /**
     * 根据人员id查询授权时效
     *
     * @return PersonAuthorizeValidityVO
     * @Param id 人员id
     */
    public HikPersonAuthorizeValidityVO queryAuthorizeValidity(Long id) {
        // 企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        // 获取人员信息
        HikPersonEntity entity = getHikPersonEntity(id, companyId);

        HikPersonAuthorizeValidityVO vo = new HikPersonAuthorizeValidityVO();
        if (Objects.nonNull(entity)) {
            vo.setValidityType(entity.getValidityType());
            vo.setBeginTime(entity.getBeginTime());
            vo.setEndTime(entity.getEndTime());
        }
        return vo;
    }

    /**
     * 查询授权分页
     *
     * @return IPage<HikPersonAuthorizeVO>
     * @Param query 参数
     */
    public IPage<HikPersonAuthorizeVO> queryAuthorizeForPage(HikPersonAuthorizePageQuery query) {
        // 企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        // 获取当前企业对接配置
        HIKEntranceGuardBO bo = hikCommonHandle.queryHikConfigByPlatfromInfo(companyId);
        // 查询门禁点分页数据
        HikDoorReq req = new HikDoorReq();
        req.setPageNo(query.getPageNo());
        req.setPageSize(query.getPageSize());
        HikDoorResp search = hikApiFeignService.queryDoorSearch(req, bo);

        List<HikPersonAuthorizeVO> collect = Lists.newArrayList();
        // 判断返回的list是否为空
        if (Objects.nonNull(search) && CollectionUtils.isNotEmpty(search.getList())) {
            // 调用外部接口查询当前用户的授权信息
            List<HikAcpsAuthConfigSearchResp.PermissionConfig> authorizeEntityList =
                    queryAuthorizeByPersonIds(Collections.singletonList(query.getId()), bo);
            Map<String, HikAcpsAuthConfigSearchResp.PermissionConfig> authorizeEntityMap = authorizeEntityList.stream()
                    .collect(Collectors.toMap(HikAcpsAuthConfigSearchResp.PermissionConfig::getChannelIndexCode, e -> e));

            collect = search.getList().stream().filter(Objects::nonNull).map(item -> {
                HikPersonAuthorizeVO vo = new HikPersonAuthorizeVO();
                vo.setName(item.getName());
                // 数据库存在则设置为已选择
                vo.setIsSelect(authorizeEntityMap.containsKey(item.getIndexCode()) ? EntityConstants.YES : EntityConstants.NO);
                vo.setRegionPathName(item.getRegionPathName());
                return vo;
            }).collect(Collectors.toList());
        }

        // 创建并返回 IPage 对象
        IPage<HikPersonAuthorizeVO> iPage = new Page<>(query.getPageNo(), query.getPageSize());
        iPage.setRecords(collect);
        iPage.setTotal(search.getTotal());
        return iPage;
    }

    /**
     * 批量删除
     *
     * @Param ids 列表
     */
    public void delete(List<Long> ids) {
        // 企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        // 获取当前企业对接配置
        HIKEntranceGuardBO bo = hikCommonHandle.queryHikConfigByPlatfromInfo(companyId);

        long count = count(new LambdaQueryWrapper<HikPersonEntity>()
                .ne(HikPersonEntity::getCompanyId, companyId)
                .in(HikPersonEntity::getId, ids));
        if (count > 0) {
            throw new BusinessException(ResultCode.ERROR, "存在非当前企业的数据，无法删除");
        }

        List<String> idstrList = ids.stream().map(e -> e + "").collect(Collectors.toList());

        // 批量删除授权信息
        batchDeleteAuthorize(ids, bo);

        // 删除人脸数据
        batchDeleteFaceData(ids, bo);

        // 批量删除第三方人员信息
        HikPersonBatchDeleteReq req = new HikPersonBatchDeleteReq();
        req.setPersonIds(idstrList);
        hikApiFeignService.personBatchDelete(req, bo);

        removeByIds(ids);
    }

    private List<HikOrgListResp.DataItem> queryOrgList(HikOrgListReq req, HIKEntranceGuardBO bo) {
        if (Objects.isNull(req.getPageNo())) {
            req.setPageNo(1L);
        }
        if (Objects.isNull(req.getPageSize())) {
            req.setPageSize(DEFAULT_PAGE_SIZE);
        }
        req.setIsSubOrg(true);
        HikOrgListResp resp = hikApiFeignService.queryOrgList(req, bo);
        if (Objects.nonNull(resp) && CollectionUtils.isNotEmpty(resp.getList())) {
            return resp.getList();
        }
        return Lists.newArrayList();
    }

    /**
     * 根据用户id列表获取第三方授权信息
     *
     * @return List<HikAcpsAuthConfigSearchResp.PermissionConfig>
     * @Param personIds 用户id列表
     * @Param bo 当前企业对接配置
     */
    private List<HikAcpsAuthConfigSearchResp.PermissionConfig> queryAuthorizeByPersonIds(List<Long> personIds, HIKEntranceGuardBO bo) {
        List<String> personIdStr = personIds.stream().map(e -> e + "").collect(Collectors.toList());

        List<HikAcpsAuthConfigSearchResp.PermissionConfig> list = Lists.newArrayList();

        AtomicLong pageNo = new AtomicLong(1);
        long pageSize = 200;
        long totalSize = 0;
        long currentSize = 0;
        do {
            HikAcpsAuthConfigSearchReq searchReq = new HikAcpsAuthConfigSearchReq();
            searchReq.setPersonDataIds(personIdStr);
            searchReq.setResourceDataType("person");
            searchReq.setPageNo(pageNo.getAndAdd(1));
            searchReq.setPageSize(pageSize);
            // 查询当前用户授权
            HikAcpsAuthConfigSearchResp search = hikApiFeignService.acpsAuthConfigSearch(searchReq, bo);

            if (Objects.nonNull(search)) {

                totalSize = search.getTotal();
                currentSize += pageSize;

                if (CollectionUtils.isNotEmpty(search.getList())) {
                    list.addAll(search.getList());
                }
            }
        } while (currentSize < totalSize);

        return list;
    }

    /**
     * 根据用户id批量删除第三方授权
     *
     * @Param personIds 用户id列表
     * @Param bo 当前企业对接配置
     */
    private void batchDeleteAuthorize(List<Long> personIds, HIKEntranceGuardBO bo) {
        // 远程调用删除授权
        HikAcpsAuthConfigDeleteReq deleteReq = new HikAcpsAuthConfigDeleteReq();
        List<HikAcpsAuthConfigDeleteReq.PersonDataDTO> person = personIds.stream().map(item -> {
            HikAcpsAuthConfigDeleteReq.PersonDataDTO personDataDTO = new HikAcpsAuthConfigDeleteReq.PersonDataDTO();
            personDataDTO.setPersonDataType("person");
            personDataDTO.setIndexCodes(Collections.singletonList(item + ""));
            return personDataDTO;
        }).collect(Collectors.toList());
        deleteReq.setPersonDatas(person);

        hikApiFeignService.acpsAuthConfigDelete(deleteReq, bo);
    }

    /**
     * 批量删除第三方人脸数据
     *
     * @Param personIds 用户id列表
     * @Param bo 当前企业对接配置
     */
    private void batchDeleteFaceData(List<Long> personIds, HIKEntranceGuardBO bo) {
        personIds.forEach(personId -> {
            HikFaceSingleDeleteReq req = new HikFaceSingleDeleteReq();
            req.setFaceId(personId + "");
            hikApiFeignService.faceSingleDelete(req, bo);
        });
    }

    /**
     * 根据用户id批量添加第三方授权
     *
     * @Param personIds 用户id列表
     * @Param bo 当前企业对接配置
     */
    private void batchAddAuthorize(HikPersonAuthorizeDto dto, HIKEntranceGuardBO bo) {
        HikAcpsAuthConfigAddReq req = new HikAcpsAuthConfigAddReq();

        // 设置用户列表信息
        List<HikAcpsAuthConfigAddReq.PersonDataDTO> dtoList = dto.getIds().stream().map(personId -> {
            HikAcpsAuthConfigAddReq.PersonDataDTO personDataDTO = new HikAcpsAuthConfigAddReq.PersonDataDTO();
            personDataDTO.setIndexCodes(Collections.singletonList(personId + ""));
            personDataDTO.setPersonDataType("person");
            return personDataDTO;
        }).collect(Collectors.toList());
        req.setPersonDatas(dtoList);

        // 设置授权信息
        List<HikAcpsAuthConfigAddReq.ResourceInfoDTO> resourceInfoDtoList = dto.getThirdDoorIdList().stream().map(item -> {
            HikAcpsAuthConfigAddReq.ResourceInfoDTO resourceInfoDTO = new HikAcpsAuthConfigAddReq.ResourceInfoDTO();
            resourceInfoDTO.setResourceIndexCode(item);
            resourceInfoDTO.setResourceType("door");
            resourceInfoDTO.setChannelNos(Collections.singletonList(1));
            return resourceInfoDTO;
        }).collect(Collectors.toList());
        req.setResourceInfos(resourceInfoDtoList);

        hikApiFeignService.acpsAuthConfigAdd(req, bo);
    }

    /**
     * 根据人员id和企业id查询第三方数据
     *
     * @return HikPersonEntity 人员信息
     * @Param personId 人员id
     * @Param companyId 企业id
     */
    private HikPersonEntity getHikPersonEntity(Long personId, Long companyId) {
        HikPersonEntity entity = getOne(new LambdaQueryWrapper<HikPersonEntity>()
                .eq(HikPersonEntity::getId, personId)
                .eq(HikPersonEntity::getCompanyId, companyId)
                .last(" LIMIT 1 "));
        if (Objects.isNull(entity)) {
            throw new BusinessException(ResultCode.ERROR, "数据不存在");
        }
        return entity;
    }

    /**
     * 第三方组织列表
     *
     * @return List<HikCommonResourcesVO>
     * @Param query 参数
     */
    public List<HikCommonResourcesVO> queryOrgList(HikOrgListQuery query) {
        // 企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        // 获取当前企业对接配置
        HIKEntranceGuardBO bo = hikCommonHandle.queryHikConfigByPlatfromInfo(companyId);

        HikOrgListReq req = new HikOrgListReq();
        req.setPageNo(query.getPageNo());
        req.setPageSize(query.getPageSize());
        req.setOrgName(query.getName());
        List<HikOrgListResp.DataItem> dataItems = queryOrgList(req, bo);

        List<HikCommonResourcesVO> list = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(dataItems)) {
            list = dataItems.stream().map(item -> {
                HikCommonResourcesVO vo = new HikCommonResourcesVO();
                vo.setId(item.getOrgIndexCode());
                vo.setName(item.getOrgName());
                return vo;
            }).collect(Collectors.toList());
        }
        return list;
    }
}