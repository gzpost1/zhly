package cn.cuiot.dmp.externalapi.service.service.gw.entranceguard;

import cn.cuiot.dmp.base.application.dto.ExcelDownloadDto;
import cn.cuiot.dmp.base.application.service.ApiSystemService;
import cn.cuiot.dmp.base.application.service.ExcelExportService;
import cn.cuiot.dmp.base.infrastructure.domain.pojo.BuildingArchiveReq;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.DepartmentReqDto;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.domain.types.Aes;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.externalapi.service.constant.PersonGroupRelationConstant;
import cn.cuiot.dmp.externalapi.service.entity.PersonGroupEntity;
import cn.cuiot.dmp.externalapi.service.entity.PersonGroupRelationEntity;
import cn.cuiot.dmp.externalapi.service.entity.gw.entranceguard.GwEntranceGuardAuthorizeEntity;
import cn.cuiot.dmp.externalapi.service.entity.gw.entranceguard.GwEntranceGuardPersonEntity;
import cn.cuiot.dmp.externalapi.service.enums.AuthorizeEnums;
import cn.cuiot.dmp.externalapi.service.enums.PersonSexEnums;
import cn.cuiot.dmp.externalapi.service.feign.SystemApiService;
import cn.cuiot.dmp.externalapi.service.mapper.PersonGroupMapper;
import cn.cuiot.dmp.externalapi.service.mapper.gw.entranceguard.GwEntranceGuardAuthorizeMapper;
import cn.cuiot.dmp.externalapi.service.query.gw.entranceguard.GwEntranceGuardPersonCreateDto;
import cn.cuiot.dmp.externalapi.service.query.gw.entranceguard.GwEntranceGuardPersonUpdateDto;
import cn.cuiot.dmp.externalapi.service.query.gw.entranceguard.GwEntranceGuardPersonPageQuery;
import cn.cuiot.dmp.externalapi.service.service.TbPersonGroupRelationService;
import cn.cuiot.dmp.externalapi.service.mapper.gw.entranceguard.GwEntranceGuardPersonMapper;
import cn.cuiot.dmp.externalapi.service.vo.gw.entranceguard.GwEntranceGuardPersonPageVO;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.cuiot.dmp.common.constant.CacheConst.SECRET_INFO_KEY;

/**
 * 联通格物门禁-人员管理 业务层
 *
 * @Author: zc
 * @Date: 2024-09-07
 */
@Service
public class GwEntranceGuardPersonService extends ServiceImpl<GwEntranceGuardPersonMapper, GwEntranceGuardPersonEntity> {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private TbPersonGroupRelationService tbPersonGroupRelationService;
    @Autowired
    private ApiSystemService apiSystemService;
    @Autowired
    private SystemApiService systemApiService;
    @Autowired
    private PersonGroupMapper personGroupMapper;
    @Autowired
    private GwEntranceGuardAuthorizeMapper authorizeMapper;

    @Autowired
    private ExcelExportService excelExportService;

    /**
     * 查询分页
     */
    public IPage<GwEntranceGuardPersonPageVO> queryForPage(GwEntranceGuardPersonPageQuery query) {
        //企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();

        LambdaQueryWrapper<GwEntranceGuardPersonEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GwEntranceGuardPersonEntity::getCompanyId, companyId);
        wrapper.like(StringUtils.isNotBlank(query.getName()), GwEntranceGuardPersonEntity::getName, query.getName());
        wrapper.eq(Objects.nonNull(query.getPersonGroupId()), GwEntranceGuardPersonEntity::getPersonGroupId, query.getPersonGroupId());
        wrapper.eq(Objects.nonNull(query.getAuthorize()), GwEntranceGuardPersonEntity::getAuthorize, query.getAuthorize());
        wrapper.orderByDesc(GwEntranceGuardPersonEntity::getCreateTime);

        IPage<GwEntranceGuardPersonPageVO> iPage = page(new Page<>(query.getPageNo(), query.getPageSize()), wrapper).convert(item -> {
            GwEntranceGuardPersonPageVO vo = new GwEntranceGuardPersonPageVO();
            BeanUtils.copyProperties(item, vo);
            return vo;
        });

        List<GwEntranceGuardPersonPageVO> records;
        if (Objects.nonNull(iPage) && CollectionUtils.isNotEmpty(records = iPage.getRecords())) {
            //构建名称信息
            buildPageVo(records);
        }

        return iPage;
    }

    /**
     * 门禁人员导出
     * @param query
     */
    public void export(GwEntranceGuardPersonPageQuery query){
        excelExportService.excelExport(ExcelDownloadDto.<GwEntranceGuardPersonPageQuery>builder().
                loginInfo(LoginInfoHolder.getCurrentLoginInfo()).query(query)
                .title("门禁人员导出").fileName("门禁人员导出(" + DateTimeUtil.dateToString(new Date(), "yyyyMMdd")+")").sheetName("门禁人员导出")
                .build(), GwEntranceGuardPersonPageVO.class, this::queryExport);
    }

    /**
     * 获取导出列表数据
     * @param downloadDto
     * @return
     */
    public IPage<GwEntranceGuardPersonPageVO> queryExport(ExcelDownloadDto<GwEntranceGuardPersonPageQuery> downloadDto){
        GwEntranceGuardPersonPageQuery pageQuery = downloadDto.getQuery();
        IPage<GwEntranceGuardPersonPageVO> data = this.queryForPage(pageQuery);
        List<GwEntranceGuardPersonPageVO> records = Optional.ofNullable(data.getRecords()).orElse(new ArrayList<>());
        records.stream().forEach(item->{
            item.setAuthorizeName(AuthorizeEnums.getNameByCode(item.getAuthorize()));
            item.setSexName(PersonSexEnums.getNameByCode(item.getSex()));
            item.setCreateExcelTime(item.getCreateTime());
        });
        return data;
    }

    /**
     * 详情
     */
    public GwEntranceGuardPersonEntity queryForDetail(Long id) {
        //企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        return baseMapper.queryForDetail(companyId, id);
    }

    /**
     * 创建和更新数据解密
     *
     * @Param dto 参数
     */
    public void dtoDecodeValue(GwEntranceGuardPersonCreateDto dto) {
        String jsonObject = stringRedisTemplate.opsForValue().get(SECRET_INFO_KEY + dto.getKid());
        stringRedisTemplate.delete(SECRET_INFO_KEY + dto.getKid());

        if (StringUtils.isEmpty(jsonObject)) {
            throw new BusinessException(ResultCode.KID_EXPIRED_ERROR, "密钥ID已过期，请重新获取");
        }
        Aes aes = JSONObject.parseObject(jsonObject, Aes.class);
        // 密码解密
        if (StringUtils.isNotBlank(dto.getPassword())) {
            dto.setPassword(aes.getDecodeValue(dto.getPassword()));
        }
        if (StringUtils.isNotBlank(dto.getIcCardNo())) {
            dto.setIcCardNo(aes.getDecodeValue(dto.getIcCardNo()));
        }
        if (StringUtils.isNotBlank(dto.getPhone())) {
            dto.setPhone(aes.getDecodeValue(dto.getPhone()));
        }
        if (StringUtils.isNotBlank(dto.getIdCardNo())) {
            dto.setIdCardNo(aes.getDecodeValue(dto.getIdCardNo()));
        }
    }

    /**
     * 创建
     */
    @Transactional(rollbackFor = Exception.class)
    public void create(GwEntranceGuardPersonCreateDto dto) {
        //数据解密
        dtoDecodeValue(dto);

        //参数校验
        checkData(dto, null);
        //企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        //部门id
        Long deptId = LoginInfoHolder.getCurrentDeptId();

        long id = IdWorker.getId();
        GwEntranceGuardPersonEntity personEntity = new GwEntranceGuardPersonEntity();
        BeanUtils.copyProperties(dto, personEntity);
        personEntity.setId(id);
        personEntity.setCompanyId(companyId);
        personEntity.setDeptId(deptId);
        personEntity.setAuthorize(EntityConstants.NO);
        //保存数据
        save(personEntity);

        //设置分组关联信息
        setPersonGroupRelation(personEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(GwEntranceGuardPersonUpdateDto dto) {
        //数据解密
        dtoDecodeValue(dto);
        //参数校验
        checkData(dto, dto.getId());

        //企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();

        GwEntranceGuardPersonEntity entranceGuardPerson = Optional.ofNullable(baseMapper.queryForDetail(companyId, dto.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.ERROR, "数据不存在"));
        BeanUtils.copyProperties(dto, entranceGuardPerson);
        updateById(entranceGuardPerson);

        //设置分组信息
        if (!Objects.equals(dto.getPersonGroupId(), entranceGuardPerson.getPersonGroupId())) {
            setPersonGroupRelation(entranceGuardPerson);
        }
    }

    /**
     * 删除
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> ids) {
        //企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        List<GwEntranceGuardPersonEntity> gwEntranceGuardPersonList = checkPerson(ids, companyId);

        removeByIds(ids);

        //删除关联信息
        gwEntranceGuardPersonList.forEach(this::deletePersonGroupRelation);
    }

    /**
     * 人员校验
     */
    public List<GwEntranceGuardPersonEntity> checkPerson(List<Long> ids, Long companyId) {
        List<GwEntranceGuardPersonEntity> entranceGuardPersonList = baseMapper.queryForListByIds(companyId, ids);
        if (CollectionUtils.isEmpty(entranceGuardPersonList)) {
            throw new BusinessException(ResultCode.ERROR, "数据不存在");
        }

        //判断人员id列表是否都属于该企业的设备
        if (ids.size() != entranceGuardPersonList.size()) {

            List<GwEntranceGuardPersonEntity> collect = entranceGuardPersonList.stream()
                    .filter(e -> !ids.contains(e.getId())).collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(collect)) {
                List<String> deviceNames = collect.stream().map(GwEntranceGuardPersonEntity::getName).collect(Collectors.toList());
                throw new BusinessException(ResultCode.ERROR, "人员【" + String.join(",", deviceNames) + "】不属于该企业");
            }

        }
        return entranceGuardPersonList;
    }

    /**
     * 设置人员分组关联信息
     */
    private void setPersonGroupRelation(GwEntranceGuardPersonEntity personEntity) {
        PersonGroupRelationEntity relation = buildEntity(personEntity);
        tbPersonGroupRelationService.createOrUpdate(relation);
    }

    /**
     * 删除人员分组关联信息
     */
    private void deletePersonGroupRelation(GwEntranceGuardPersonEntity personEntity) {
        PersonGroupRelationEntity relation = buildEntity(personEntity);
        tbPersonGroupRelationService.delete(relation);
    }

    /**
     * 构建关联数据
     */
    private PersonGroupRelationEntity buildEntity(GwEntranceGuardPersonEntity personEntity) {
        PersonGroupRelationEntity relation = new PersonGroupRelationEntity();
        relation.setBusinessType(PersonGroupRelationConstant.GW_ENTRANCE_GUARD);
        relation.setDataId(personEntity.getId());
        relation.setPersonGroupId(personEntity.getPersonGroupId());
        return relation;
    }

    /**
     * 保存参数校验
     */
    private void checkData(GwEntranceGuardPersonCreateDto dto, Long id) {
        //时效校验
        if (Objects.equals(dto.getPrescriptionType(), EntityConstants.YES)) {
            if (Objects.isNull(dto.getPrescriptionBeginDate()) || Objects.isNull(dto.getPrescriptionEndDate())) {
                throw new BusinessException(ResultCode.ERROR, "请选择有效期起止时间");
            }
            if (dto.getPrescriptionEndDate().before(dto.getPrescriptionBeginDate())) {
                throw new BusinessException(ResultCode.ERROR, "时效结束日期必须大于开始日期");
            }
        }
        //身份证号校验
        if (StringUtils.isNotBlank(dto.getIdCardNo())) {
            boolean validCard = IdcardUtil.isValidCard(dto.getIdCardNo());
            if (!validCard) {
                throw new BusinessException(ResultCode.ERROR, "请输入正确的身份证号");
            }
        }
        //ic、密码、人员照片校验
        if (StringUtils.isBlank(dto.getIdCardNo()) && StringUtils.isBlank(dto.getIcCardNo()) && StringUtils.isBlank(dto.getImage())) {
            throw new BusinessException(ResultCode.ERROR, "IC卡号、密码、人员照片三项至少填写一项");
        }
        //ic卡号校验
        if (StringUtils.isNotBlank(dto.getIcCardNo())) {
            boolean numeric = StrUtil.isNumeric(dto.getIcCardNo());
            if (!numeric) {
                throw new BusinessException(ResultCode.ERROR, "密码必须是纯数字");
            }
            //校验数据库是否已存在
            Boolean existsIcCardNo = baseMapper.isExistsIcCardNo(dto.getIcCardNo(), id);
            if (existsIcCardNo) {
                throw new BusinessException(ResultCode.ERROR, "IC卡号已存在");
            }
        }
    }

    private void buildPageVo(List<GwEntranceGuardPersonPageVO> list) {
        // 获取各类信息的 Map
        Map<Long, Object> buildingArchiveMap = getDataMap(list, GwEntranceGuardPersonPageVO::getBuildingId, this::queryBuildingInfo, BuildingArchive::getId, BuildingArchive::getName);
        Map<Long, Object> deptMap = getDataMap(list, GwEntranceGuardPersonPageVO::getDeptId, this::queryDeptList, DepartmentDto::getId, DepartmentDto::getPathName);
        Map<Long, Object> personGroupMap = getDataMap(list, GwEntranceGuardPersonPageVO::getDeptId, this::queryPersonGroupList, PersonGroupEntity::getId, PersonGroupEntity::getName);
        Map<Long, Object> authorizeInfoMap = getDataMap(list, GwEntranceGuardPersonPageVO::getId, this::queryAuthorizeInfo, GwEntranceGuardAuthorizeEntity::getEntranceGuardPersonId, GwEntranceGuardAuthorizeEntity::getEntranceGuardId);

        // 设置 Vo 对象的值
        for (GwEntranceGuardPersonPageVO vo : list) {
            vo.setBuildingName(buildingArchiveMap.getOrDefault(vo.getBuildingId(), null) + "");
            vo.setDeptPathName(deptMap.getOrDefault(vo.getDeptId(), null) + "");
            vo.setPersonGroupName(personGroupMap.getOrDefault(vo.getPersonGroupId(), null) + "");
            vo.setAuthorize(authorizeInfoMap.containsKey(vo.getId()) ? EntityConstants.YES : EntityConstants.NO);
        }
    }

    /**
     * 通用方法：根据 VO 属性获取数据 Map
     */
    private <T> Map<Long, Object> getDataMap(List<GwEntranceGuardPersonPageVO> list,
                                             Function<GwEntranceGuardPersonPageVO, Long> idGetter,
                                             Function<List<Long>, List<T>> queryFunction,
                                             Function<T, Long> keyMapper,
                                             Function<T, Object> valueMapper) {
        List<Long> ids = list.stream().map(idGetter).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(ids)) {
            return Maps.newHashMap();
        }
        return queryFunction.apply(ids).stream().collect(Collectors.toMap(keyMapper, valueMapper));
    }

    /**
     * 查询楼盘信息
     */
    private List<BuildingArchive> queryBuildingInfo(List<Long> buildingIds) {
        BuildingArchiveReq req = new BuildingArchiveReq();
        req.setIdList(buildingIds);
        return systemApiService.buildingArchiveQueryForList(req);
    }

    /**
     * 查询分组信息
     */
    public List<PersonGroupEntity> queryPersonGroupList(List<Long> personGroupIds) {
        return personGroupMapper.selectBatchIds(personGroupIds);
    }

    /**
     * 获取组织信息
     */
    private List<DepartmentDto> queryDeptList(List<Long> deptIds) {
        DepartmentReqDto dto = new DepartmentReqDto();
        dto.setDeptIdList(deptIds);
        return apiSystemService.lookUpDepartmentList(dto);
    }

    private List<GwEntranceGuardAuthorizeEntity> queryAuthorizeInfo(List<Long> personIds) {
        return authorizeMapper.selectList(new LambdaQueryWrapper<GwEntranceGuardAuthorizeEntity>()
                .in(GwEntranceGuardAuthorizeEntity::getId, personIds)
                .groupBy(GwEntranceGuardAuthorizeEntity::getEntranceGuardPersonId));
    }
}
