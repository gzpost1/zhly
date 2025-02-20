package cn.cuiot.dmp.lease.service;

import cn.cuiot.dmp.base.application.dto.ExcelDownloadDto;
import cn.cuiot.dmp.base.application.service.ExcelExportService;
import cn.cuiot.dmp.base.infrastructure.domain.pojo.BuildingArchiveReq;
import cn.cuiot.dmp.base.infrastructure.dto.BaseUserDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.BaseUserReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.CustomConfigDetailReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.req.CustomerUseReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.FormConfigReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.CustomerUserRspDto;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.FormConfigRspDTO;
import cn.cuiot.dmp.base.infrastructure.feign.ArchiveFeignService;
import cn.cuiot.dmp.base.infrastructure.feign.SystemApiFeignService;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;
import cn.cuiot.dmp.common.bean.dto.SmsMsgDto;
import cn.cuiot.dmp.common.bean.dto.SysMsgDto;
import cn.cuiot.dmp.common.bean.dto.UserMessageAcceptDto;
import cn.cuiot.dmp.common.constant.*;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.domain.types.enums.UserTypeEnum;
import cn.cuiot.dmp.lease.config.MsgChannel;
import cn.cuiot.dmp.lease.constants.ClueConstant;
import cn.cuiot.dmp.lease.dto.clue.*;
import cn.cuiot.dmp.lease.entity.ClueEntity;
import cn.cuiot.dmp.lease.entity.ClueRecordEntity;
import cn.cuiot.dmp.lease.enums.ClueFollowDayEnum;
import cn.cuiot.dmp.lease.enums.ClueStatusEnum;
import cn.cuiot.dmp.lease.mapper.ClueMapper;
import cn.cuiot.dmp.lease.vo.export.ClueExportVo;
import cn.cuiot.dmp.lease.vo.export.ClueFinishExportVo;
import cn.cuiot.dmp.lease.vo.export.ClueFollowUpExportVo;
import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MimeTypeUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author caorui
 * @date 2024/6/1
 */
@Slf4j
@Service
public class ClueService extends ServiceImpl<ClueMapper, ClueEntity> {

    @Autowired
    private ClueRecordService clueRecordService;

    @Autowired
    private SystemApiFeignService systemApiFeignService;

    @Autowired
    private ArchiveFeignService archiveFeignService;

    @Autowired
    private MsgChannel msgChannel;
    @Autowired
    private ExcelExportService excelExportService;

    /**
     * 查询详情
     */
    public ClueDTO queryForDetail(Long id) {
        ClueEntity clueEntity = Optional.ofNullable(getById(id))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        ClueDTO clueDTO = new ClueDTO();
        BeanUtils.copyProperties(clueEntity, clueDTO);
        clueDTO.setFormData(JSON.parseObject(clueEntity.getFormData()));
        fillBuildingName(Lists.newArrayList(clueDTO));
        fillUserName(Lists.newArrayList(clueDTO));
        fillSystemOptionName(Lists.newArrayList(clueDTO));
        fillCustomerName(Lists.newArrayList(clueDTO));
        return clueDTO;
    }

    /**
     * 查询列表
     */
    public List<ClueDTO> queryForList(CluePageQueryDTO queryDTO) {
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        LambdaQueryWrapper<ClueEntity> queryWrapper = new LambdaQueryWrapper<ClueEntity>()
                .like(StringUtils.isNotBlank(queryDTO.getName()), ClueEntity::getName, queryDTO.getName())
                .eq(Objects.nonNull(companyId), ClueEntity::getCompanyId, companyId)
                .eq(Objects.nonNull(queryDTO.getDepartmentId()), ClueEntity::getDepartmentId, queryDTO.getDepartmentId())
                .eq(Objects.nonNull(queryDTO.getBuildingId()), ClueEntity::getBuildingId, queryDTO.getBuildingId())
                .eq(Objects.nonNull(queryDTO.getSourceId()), ClueEntity::getSourceId, queryDTO.getSourceId())
                .eq(Objects.nonNull(queryDTO.getStatus()), ClueEntity::getStatus, queryDTO.getStatus())
                .eq(Objects.nonNull(queryDTO.getCurrentUserId()), ClueEntity::getCreatedBy, queryDTO.getCurrentUserId())
                .ge(Objects.nonNull(queryDTO.getBeginTime()), ClueEntity::getCreatedOn, queryDTO.getBeginTime())
                .le(Objects.nonNull(queryDTO.getEndTime()), ClueEntity::getCreatedOn, queryDTO.getEndTime())
                .eq(Objects.nonNull(queryDTO.getResultId()), ClueEntity::getResultId, queryDTO.getResultId())
                .eq(Objects.nonNull(queryDTO.getFinishUserId()), ClueEntity::getFinishUserId, queryDTO.getFinishUserId())
                .ge(Objects.nonNull(queryDTO.getFinishBeginTime()), ClueEntity::getFinishTime, queryDTO.getFinishBeginTime())
                .le(Objects.nonNull(queryDTO.getFinishEndTime()), ClueEntity::getFinishTime, queryDTO.getFinishEndTime())
                .eq(Objects.nonNull(queryDTO.getCurrentFollowerId()), ClueEntity::getCurrentFollowerId, queryDTO.getCurrentFollowerId())
                .eq(Objects.nonNull(queryDTO.getCurrentFollowStatusId()), ClueEntity::getCurrentFollowStatusId, queryDTO.getCurrentFollowStatusId())
                .ge(Objects.nonNull(queryDTO.getFollowBeginTime()), ClueEntity::getCurrentFollowTime, queryDTO.getFollowBeginTime())
                .le(Objects.nonNull(queryDTO.getFollowEndTime()), ClueEntity::getCurrentFollowTime, queryDTO.getFollowEndTime());
        // 如果未跟进天数类型不为空
        if (Objects.nonNull(queryDTO.getClueFollowDay())) {
            initQueryWrapperByFollowDay(queryWrapper, queryDTO.getClueFollowDay());
        }
        queryWrapper.orderByDesc(ClueEntity::getCreatedOn);
        List<ClueEntity> clueEntityList = list(queryWrapper);
        if (CollectionUtils.isEmpty(clueEntityList)) {
            return new ArrayList<>();
        }
        return clueEntityList.stream()
                .map(o -> {
                    ClueDTO clueDTO = new ClueDTO();
                    BeanUtils.copyProperties(o, clueDTO);
                    clueDTO.setFormData(JSON.parseObject(o.getFormData()));
                    return clueDTO;
                })
                .collect(Collectors.toList());
    }

    /**
     * 查询分页列表
     */
    public PageResult<ClueDTO> queryForPage(CluePageQueryDTO queryDTO) {
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        LambdaQueryWrapper<ClueEntity> queryWrapper = new LambdaQueryWrapper<ClueEntity>()
                .like(StringUtils.isNotBlank(queryDTO.getName()), ClueEntity::getName, queryDTO.getName())
                .eq(Objects.nonNull(companyId), ClueEntity::getCompanyId, companyId)
                .eq(Objects.nonNull(queryDTO.getDepartmentId()), ClueEntity::getDepartmentId, queryDTO.getDepartmentId())
                .eq(Objects.nonNull(queryDTO.getBuildingId()), ClueEntity::getBuildingId, queryDTO.getBuildingId())
                .eq(Objects.nonNull(queryDTO.getSourceId()), ClueEntity::getSourceId, queryDTO.getSourceId())
                .eq(Objects.nonNull(queryDTO.getStatus()), ClueEntity::getStatus, queryDTO.getStatus())
                .eq(Objects.nonNull(queryDTO.getCurrentUserId()), ClueEntity::getCreatedBy, queryDTO.getCurrentUserId())
                .ge(Objects.nonNull(queryDTO.getBeginTime()), ClueEntity::getCreatedOn, queryDTO.getBeginTime())
                .le(Objects.nonNull(queryDTO.getEndTime()), ClueEntity::getCreatedOn, queryDTO.getEndTime())
                .eq(Objects.nonNull(queryDTO.getResultId()), ClueEntity::getResultId, queryDTO.getResultId())
                .eq(Objects.nonNull(queryDTO.getFinishUserId()), ClueEntity::getFinishUserId, queryDTO.getFinishUserId())
                .ge(Objects.nonNull(queryDTO.getFinishBeginTime()), ClueEntity::getFinishTime, queryDTO.getFinishBeginTime())
                .le(Objects.nonNull(queryDTO.getFinishEndTime()), ClueEntity::getFinishTime, queryDTO.getFinishEndTime())
                .eq(Objects.nonNull(queryDTO.getCurrentFollowerId()), ClueEntity::getCurrentFollowerId, queryDTO.getCurrentFollowerId())
                .eq(Objects.nonNull(queryDTO.getCurrentFollowStatusId()), ClueEntity::getCurrentFollowStatusId, queryDTO.getCurrentFollowStatusId())
                .ge(Objects.nonNull(queryDTO.getFollowBeginTime()), ClueEntity::getCurrentFollowTime, queryDTO.getFollowBeginTime())
                .le(Objects.nonNull(queryDTO.getFollowEndTime()), ClueEntity::getCurrentFollowTime, queryDTO.getFollowEndTime());
        // 如果未跟进天数类型不为空
        if (Objects.nonNull(queryDTO.getClueFollowDay())) {
            initQueryWrapperByFollowDay(queryWrapper, queryDTO.getClueFollowDay());
        }
        queryWrapper.orderByDesc(ClueEntity::getCreatedOn);
        IPage<ClueEntity> clueEntityIPage = page(new Page<>(queryDTO.getPageNo(), queryDTO.getPageSize()), queryWrapper);
        if (CollectionUtils.isEmpty(clueEntityIPage.getRecords())) {
            return new PageResult<>();
        }
        return clueEntity2ClueDTOs(clueEntityIPage);
    }

    /**
     * 保存
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean saveClue(ClueCreateDTO createDTO) {
        ClueEntity clueEntity = new ClueEntity();
        BeanUtils.copyProperties(createDTO, clueEntity);
        clueEntity.setFormData(String.valueOf(createDTO.getFormData()));
        // 保存线索表单快照
        FormConfigReqDTO formConfigReqDTO = new FormConfigReqDTO();
        formConfigReqDTO.setCompanyId(createDTO.getCompanyId());
        formConfigReqDTO.setName(SystemFormConfigConstant.CLUE_FORM_CONFIG.get(0));
        FormConfigRspDTO formConfigRspDTO = systemApiFeignService.lookUpFormConfigByName(formConfigReqDTO).getData();
        clueEntity.setFormConfigDetail(formConfigRspDTO.getFormConfigDetail());
        return save(clueEntity);
    }

    /**
     * 更新
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateClue(ClueUpdateDTO updateDTO) {
        ClueEntity clueEntity = Optional.ofNullable(getById(updateDTO.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        BeanUtils.copyProperties(updateDTO, clueEntity);
        clueEntity.setFormData(String.valueOf(updateDTO.getFormData()));
        return updateById(clueEntity);
    }

    /**
     * 分配
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean distributeClue(ClueDistributeDTO distributeDTO) {
        String userName = LoginInfoHolder.getCurrentUsername();
        ClueEntity clueEntity = Optional.ofNullable(getById(distributeDTO.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        clueEntity.setCurrentFollowerId(distributeDTO.getCurrentFollowerId());
        clueEntity.setStatus(ClueStatusEnum.FOLLOW_STATUS.getCode());
        clueEntity.setDistributeTime(new Date());
        // 发送分配线索通知
        UserMessageAcceptDto userMessageAcceptDto = new UserMessageAcceptDto()
                .setMsgType((byte) (InformTypeConstant.SYS_MSG + InformTypeConstant.SMS));
        SysMsgDto sysMsgDto = new SysMsgDto().setAcceptors(Lists.newArrayList(distributeDTO.getCurrentFollowerId()))
                .setDataId(clueEntity.getId()).setDataType(MsgDataType.CLUE_DISTRIBUTE)
                .setMessage(String.format(ClueConstant.CLUE_DISTRIBUTE_MSG, userName, clueEntity.getName()))
                .setMessageTime(new Date()).setMsgType(MsgTypeConstant.CLUE)
                .setUserType(UserTypeEnum.USER.getValue());
        SmsMsgDto smsMsgDto = new SmsMsgDto();
        smsMsgDto.setUserIds(Lists.newArrayList(distributeDTO.getCurrentFollowerId())).setParams(Arrays.asList(LoginInfoHolder.getCurrentName(), clueEntity.getName()))
                .setTemplateId(SmsStdTemplate.MANAGE_CLUE);
        userMessageAcceptDto.setSysMsgDto(sysMsgDto).setSmsMsgDto(smsMsgDto);
        msgChannel.userMessageOutput().send(MessageBuilder.withPayload(userMessageAcceptDto)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build());
        return updateById(clueEntity);
    }

    /**
     * 跟进
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean followClue(ClueFollowDTO followDTO) {
        ClueRecordEntity clueRecord = new ClueRecordEntity();
        BeanUtils.copyProperties(followDTO, clueRecord);
        clueRecord.setId(IdWorker.getId());
        clueRecord.setFormData(String.valueOf(followDTO.getFormData()));
        clueRecord.setFollowTime(new Date());
        // 保存跟进表单快照
        FormConfigReqDTO formConfigReqDTO = new FormConfigReqDTO();
        formConfigReqDTO.setCompanyId(followDTO.getCompanyId());
        formConfigReqDTO.setName(SystemFormConfigConstant.CLUE_FORM_CONFIG.get(1));
        FormConfigRspDTO formConfigRspDTO = systemApiFeignService.lookUpFormConfigByName(formConfigReqDTO).getData();
        clueRecord.setFormConfigDetail(formConfigRspDTO.getFormConfigDetail());
        // 更新线索相关字段
        ClueEntity clueEntity = Optional.ofNullable(getById(followDTO.getClueId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        clueEntity.setCurrentFollowTime(clueRecord.getFollowTime());
        clueEntity.setCurrentFollowStatusId(followDTO.getFollowStatusId());
        clueEntity.setCurrentFollowRecordId(clueRecord.getId());
        updateById(clueEntity);
        return clueRecordService.save(clueRecord);
    }

    /**
     * 完成
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean finishClue(ClueFinishDTO finishDTO) {
        ClueEntity clueEntity = Optional.ofNullable(getById(finishDTO.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        BeanUtils.copyProperties(finishDTO, clueEntity);
        clueEntity.setFinishTime(new Date());
        clueEntity.setStatus(ClueStatusEnum.FINISH_STATUS.getCode());
        return updateById(clueEntity);
    }

    /**
     * 删除
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteClue(Long id) {
        // 删除关联的线索记录
        clueRecordService.deleteClueRecordByClueId(id);
        return removeById(id);
    }

    /**
     * 批量分配
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDistributeClue(ClueBatchUpdateDTO batchUpdateDTO) {
        AssertUtil.notNull(batchUpdateDTO.getCurrentFollowerId(), "当前跟进人ID不能为空");
        LambdaQueryWrapper<ClueEntity> queryWrapper = new LambdaQueryWrapper<ClueEntity>()
                .in(CollectionUtils.isNotEmpty(batchUpdateDTO.getIdList()), ClueEntity::getId, batchUpdateDTO.getIdList());
        List<ClueEntity> clueEntityList = list(queryWrapper);
        if (CollectionUtils.isEmpty(clueEntityList)) {
            return true;
        }
        clueEntityList.forEach(o -> {
            o.setStatus(ClueStatusEnum.FOLLOW_STATUS.getCode());
            o.setCurrentFollowerId(batchUpdateDTO.getCurrentFollowerId());
            o.setDistributeTime(new Date());
        });
        return updateBatchById(clueEntityList);
    }

    /**
     * 批量完成
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean batchFinishClue(ClueBatchUpdateDTO batchUpdateDTO) {
        AssertUtil.notNull(batchUpdateDTO.getResultId(), "线索结果不能为空");
        LambdaQueryWrapper<ClueEntity> queryWrapper = new LambdaQueryWrapper<ClueEntity>()
                .in(CollectionUtils.isNotEmpty(batchUpdateDTO.getIdList()), ClueEntity::getId, batchUpdateDTO.getIdList());
        List<ClueEntity> clueEntityList = list(queryWrapper);
        if (CollectionUtils.isEmpty(clueEntityList)) {
            return true;
        }
        clueEntityList.forEach(o -> {
            o.setResultId(batchUpdateDTO.getResultId());
            o.setFinishTime(new Date());
            o.setFinishUserId(batchUpdateDTO.getFinishUserId());
            o.setStatus(ClueStatusEnum.FINISH_STATUS.getCode());
            if (StringUtils.isNotBlank(batchUpdateDTO.getRemark())) {
                o.setRemark(batchUpdateDTO.getRemark());
            }
        });
        return updateBatchById(clueEntityList);
    }

    /**
     * 批量删除
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteClue(List<Long> idList) {
        return removeByIds(idList);
    }

    private PageResult<ClueDTO> clueEntity2ClueDTOs(IPage<ClueEntity> clueEntityIPage) {
        PageResult<ClueDTO> clueDTOPageResult = new PageResult<>();
        List<ClueDTO> clueDTOList = clueEntityIPage.getRecords().stream()
                .map(o -> {
                    ClueDTO clueDTO = new ClueDTO();
                    BeanUtils.copyProperties(o, clueDTO);
                    clueDTO.setFormData(JSON.parseObject(o.getFormData()));
                    return clueDTO;
                })
                .collect(Collectors.toList());
        fillBuildingName(clueDTOList);
        fillUserName(clueDTOList);
        fillSystemOptionName(clueDTOList);
        fillCustomerName(clueDTOList);
        clueDTOPageResult.setList(clueDTOList);
        clueDTOPageResult.setCurrentPage((int) clueEntityIPage.getCurrent());
        clueDTOPageResult.setPageSize((int) clueEntityIPage.getSize());
        clueDTOPageResult.setTotal(clueEntityIPage.getTotal());
        return clueDTOPageResult;
    }

    /**
     * 使用楼盘id列表查询出，对应的楼盘名称
     *
     * @param clueDTOList 线索列表
     */
    private void fillBuildingName(List<ClueDTO> clueDTOList) {
        if (CollectionUtils.isEmpty(clueDTOList)) {
            return;
        }
        Set<Long> buildingIdList = new HashSet<>();
        clueDTOList.forEach(o -> {
            if (Objects.nonNull(o.getBuildingId())) {
                buildingIdList.add(o.getBuildingId());
            }
        });
        BuildingArchiveReq buildingArchiveReq = new BuildingArchiveReq();
        buildingArchiveReq.setIdList(new ArrayList<>(buildingIdList));
        List<BuildingArchive> buildingArchiveList = archiveFeignService.buildingArchiveQueryForList(buildingArchiveReq).getData();
        if (CollectionUtils.isEmpty(buildingArchiveList)) {
            return;
        }
        Map<Long, List<BuildingArchive>> buildingMap = buildingArchiveList.stream()
                .collect(Collectors.groupingBy(BuildingArchive::getId));
        clueDTOList.forEach(o -> {
            if (Objects.nonNull(o.getBuildingId()) && buildingMap.containsKey(o.getBuildingId())) {
                o.setBuildingName(buildingMap.get(o.getBuildingId()).get(0).getName());
                o.setBuildingDepartmentId(buildingMap.get(o.getBuildingId()).get(0).getDepartmentId());
            }
        });
    }

    /**
     * 使用用户id列表查询出，对应的用户名称
     *
     * @param clueDTOList 线索列表
     */
    private void fillUserName(List<ClueDTO> clueDTOList) {
        if (CollectionUtils.isEmpty(clueDTOList)) {
            return;
        }
        Set<Long> userIdList = new HashSet<>();
        clueDTOList.forEach(o -> {
            if (StringUtils.isNotBlank(o.getCreatedBy())) {
                userIdList.add(Long.valueOf(o.getCreatedBy()));
            }
            if (Objects.nonNull(o.getCurrentFollowerId())) {
                userIdList.add(o.getCurrentFollowerId());
            }
            if (Objects.nonNull(o.getFinishUserId())) {
                userIdList.add(o.getFinishUserId());
            }
        });
        BaseUserReqDto reqDto = new BaseUserReqDto();
        reqDto.setUserIdList(new ArrayList<>(userIdList));
        List<BaseUserDto> baseUserDtoList = systemApiFeignService.lookUpUserList(reqDto).getData();
        Map<Long, String> userMap = baseUserDtoList.stream().collect(Collectors.toMap(BaseUserDto::getId, BaseUserDto::getName));
        if (userMap.isEmpty()) {
            return;
        }
        clueDTOList.forEach(o -> {
            if (StringUtils.isNotBlank(o.getCreatedBy()) && userMap.containsKey(Long.valueOf(o.getCreatedBy()))) {
                o.setCreatedName(userMap.get(Long.valueOf(o.getCreatedBy())));
            }
            if (Objects.nonNull(o.getCurrentFollowerId()) && userMap.containsKey(o.getCurrentFollowerId())) {
                o.setCurrentFollowerName(userMap.get(o.getCurrentFollowerId()));
            }
            if (Objects.nonNull(o.getFinishUserId()) && userMap.containsKey(o.getFinishUserId())) {
                o.setFinishUserName(userMap.get(o.getFinishUserId()));
            }
        });
    }

    /**
     * 使用配置id列表查询出，对应的名称关系
     *
     * @param clueDTOList 线索列表
     */
    private void fillSystemOptionName(List<ClueDTO> clueDTOList) {
        if (CollectionUtils.isEmpty(clueDTOList)) {
            return;
        }
        Set<Long> configIdList = new HashSet<>();
        clueDTOList.forEach(o -> {
            if (Objects.nonNull(o.getSourceId())) {
                configIdList.add(o.getSourceId());
            }
            if (Objects.nonNull(o.getResultId())) {
                configIdList.add(o.getResultId());
            }
            if (Objects.nonNull(o.getCurrentFollowStatusId())) {
                configIdList.add(o.getCurrentFollowStatusId());
            }
        });
        CustomConfigDetailReqDTO customConfigDetailReqDTO = new CustomConfigDetailReqDTO();
        customConfigDetailReqDTO.setCustomConfigDetailIdList(new ArrayList<>(configIdList));
        Map<Long, String> systemOptionMap = systemApiFeignService.batchQueryCustomConfigDetailsForMap(customConfigDetailReqDTO)
                .getData();
        if (systemOptionMap.isEmpty()) {
            return;
        }
        clueDTOList.forEach(o -> {
            if (Objects.nonNull(o.getSourceId()) && systemOptionMap.containsKey(o.getSourceId())) {
                o.setSourceIdName(systemOptionMap.get(o.getSourceId()));
            }
            if (Objects.nonNull(o.getResultId()) && systemOptionMap.containsKey(o.getResultId())) {
                o.setResultIdName(systemOptionMap.get(o.getResultId()));
            }
            if (Objects.nonNull(o.getCurrentFollowStatusId()) && systemOptionMap.containsKey(o.getCurrentFollowStatusId())) {
                o.setCurrentFollowStatusIdName(systemOptionMap.get(o.getCurrentFollowStatusId()));
            }
        });
    }

    /**
     * 使用客户id列表查询出，对应的客户名称
     *
     * @param clueDTOList 线索列表
     */
    private void fillCustomerName(List<ClueDTO> clueDTOList) {
        if (CollectionUtils.isEmpty(clueDTOList)) {
            return;
        }
        Set<Long> customerIdList = new HashSet<>();
        clueDTOList.forEach(o -> {
            if (Objects.nonNull(o.getCustomerUserId())) {
                customerIdList.add(o.getCustomerUserId());
            }
        });
        CustomerUseReqDto reqDto = new CustomerUseReqDto();
        reqDto.setCustomerIdList(new ArrayList<>(customerIdList));
        List<CustomerUserRspDto> customerUserRspDtoList = archiveFeignService.lookupCustomerUsers(reqDto).getData();
        if (CollectionUtils.isEmpty(customerUserRspDtoList)) {
            return;
        }
        // 分组取第一条数据
        Map<Long, String> customerMap = customerUserRspDtoList.stream()
                .collect(Collectors.groupingBy(
                        CustomerUserRspDto::getCustomerId,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.get(0).getCustomerName()
                        )
                ));
        clueDTOList.forEach(o -> {
            if (Objects.nonNull(o.getCustomerUserId()) && customerMap.containsKey(o.getCustomerUserId())) {
                o.setCustomerUserName(customerMap.get(o.getCustomerUserId()));
            }
        });
    }

    private void initQueryWrapperByFollowDay(LambdaQueryWrapper<ClueEntity> queryWrapper, Byte clueFollowDay) {
        LocalDate now = LocalDate.now();
        if (ClueFollowDayEnum.ZERO_THREE_DAY.getCode().equals(clueFollowDay)) {
            queryWrapper.and(qwp -> {
                        qwp.ge(ClueEntity::getCurrentFollowTime, now.minusDays(3));
                        qwp.or(wp -> wp.isNull(ClueEntity::getCurrentFollowTime)
                                .ge(ClueEntity::getDistributeTime, now.minusDays(3)));
                    }
            );
        } else if (ClueFollowDayEnum.THREE_SEVEN_DAY.getCode().equals(clueFollowDay)) {
            queryWrapper.and(qwp -> {
                        qwp.and(wp -> wp.ge(ClueEntity::getCurrentFollowTime, now.minusDays(7))
                                .le(ClueEntity::getCurrentFollowTime, now.minusDays(3)));
                        qwp.or(wp -> wp.isNull(ClueEntity::getCurrentFollowTime)
                                .ge(ClueEntity::getDistributeTime, now.minusDays(7))
                                .le(ClueEntity::getDistributeTime, now.minusDays(3)));
                    }
            );
        } else if (ClueFollowDayEnum.SEVEN_FIFTEEN_DAY.getCode().equals(clueFollowDay)) {
            queryWrapper.and(qwp -> {
                        qwp.and(wp -> wp.ge(ClueEntity::getCurrentFollowTime, now.minusDays(15))
                                .le(ClueEntity::getCurrentFollowTime, now.minusDays(7)));
                        qwp.or(wp -> wp.isNull(ClueEntity::getCurrentFollowTime)
                                .ge(ClueEntity::getDistributeTime, now.minusDays(15))
                                .le(ClueEntity::getDistributeTime, now.minusDays(7)));
                    }
            );
        } else if (ClueFollowDayEnum.FIFTEEN_THIRTY_DAY.getCode().equals(clueFollowDay)) {
            queryWrapper.and(qwp -> {
                        qwp.and(wp -> wp.ge(ClueEntity::getCurrentFollowTime, now.minusDays(30))
                                .le(ClueEntity::getCurrentFollowTime, now.minusDays(15)));
                        qwp.or(wp -> wp.isNull(ClueEntity::getCurrentFollowTime)
                                .ge(ClueEntity::getDistributeTime, now.minusDays(30))
                                .le(ClueEntity::getDistributeTime, now.minusDays(15)));
                    }
            );
        } else if (ClueFollowDayEnum.THIRTY_MORE_DAY.getCode().equals(clueFollowDay)) {
            queryWrapper.and(qwp -> {
                        qwp.le(ClueEntity::getCurrentFollowTime, now.minusDays(30));
                        qwp.or(wp -> wp.isNull(ClueEntity::getCurrentFollowTime)
                                .le(ClueEntity::getDistributeTime, now.minusDays(30)));
                    }
            );
        }
    }

    public void export(CluePageQueryDTO pageQuery) throws Exception {
        PageResult<ClueDTO> pageResult = this.queryForPage(pageQuery);
        if (pageResult.getTotal() > ExcelExportService.MAX_EXPORT_DATA) {
            throw new BusinessException(ResultCode.EXPORT_DATA_OVER_LIMIT);
        }
        if (ClueStatusEnum.DISTRIBUTE_STATUS.getCode().equals(pageQuery.getStatus())) {
            excelExportService.excelExport(ExcelDownloadDto.<CluePageQueryDTO>builder().loginInfo(LoginInfoHolder.getCurrentLoginInfo()).query(pageQuery)
                    .title("待分配列表").fileName("待分配线索导出" + "("+ DateTimeUtil.dateToString(new Date(), "yyyyMMdd")+")").sheetName("待分配列表").build(), ClueExportVo.class, this::executePageQuery);
        } else if (ClueStatusEnum.FOLLOW_STATUS.getCode().equals(pageQuery.getStatus())) {
            excelExportService.excelExport(ExcelDownloadDto.<CluePageQueryDTO>builder().loginInfo(LoginInfoHolder.getCurrentLoginInfo()).query(pageQuery)
                    .title("跟进中列表").fileName("跟进中线索导出" + "("+ DateTimeUtil.dateToString(new Date(), "yyyyMMdd")+")").sheetName("跟进中列表").build(), ClueFollowUpExportVo.class, this::executeFollowUpPageQuery);
        } else if (ClueStatusEnum.FINISH_STATUS.getCode().equals(pageQuery.getStatus())) {
            excelExportService.excelExport(ExcelDownloadDto.<CluePageQueryDTO>builder().loginInfo(LoginInfoHolder.getCurrentLoginInfo()).query(pageQuery)
                    .title("已完成列表").fileName("已完成线索导出" + "("+ DateTimeUtil.dateToString(new Date(), "yyyyMMdd")+")").sheetName("已完成列表").build(), ClueFinishExportVo.class, this::executeFinishPageQuery);
        } else {
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "传入的状态不对");
        }
    }

    private IPage<ClueFinishExportVo> executeFinishPageQuery(ExcelDownloadDto<CluePageQueryDTO> cluePageQueryDTOExcelDownloadDto) {
        PageResult<ClueDTO> pageResult = this.queryForPage(cluePageQueryDTOExcelDownloadDto.getQuery());
        List<ClueFinishExportVo> exportDataList = new ArrayList<>();
        pageResult.getList().forEach(o -> {
            ClueFinishExportVo exportVo = new ClueFinishExportVo();
            BeanUtil.copyProperties(o, exportVo);
            exportDataList.add(exportVo);
        });
        Page<ClueFinishExportVo> page = new Page<>(pageResult.getCurrentPage(), pageResult.getPageSize(), pageResult.getTotal());
        return page.setRecords(exportDataList);
    }

    private IPage<ClueFollowUpExportVo> executeFollowUpPageQuery(ExcelDownloadDto<CluePageQueryDTO> cluePageQueryDTOExcelDownloadDto) {
        PageResult<ClueDTO> pageResult = this.queryForPage(cluePageQueryDTOExcelDownloadDto.getQuery());
        List<ClueFollowUpExportVo> exportDataList = new ArrayList<>();
        pageResult.getList().forEach(o -> {
            ClueFollowUpExportVo exportVo = new ClueFollowUpExportVo();
            BeanUtil.copyProperties(o, exportVo);
            exportDataList.add(exportVo);
        });
        Page<ClueFollowUpExportVo> page = new Page<>(pageResult.getCurrentPage(), pageResult.getPageSize(), pageResult.getTotal());
        return page.setRecords(exportDataList);
    }

    private IPage<ClueExportVo> executePageQuery(ExcelDownloadDto<CluePageQueryDTO> cluePageQueryDTOExcelDownloadDto) {
        PageResult<ClueDTO> pageResult = this.queryForPage(cluePageQueryDTOExcelDownloadDto.getQuery());
        List<ClueExportVo> exportDataList = new ArrayList<>();
        pageResult.getList().forEach(o -> {
            ClueExportVo exportVo = new ClueExportVo();
            BeanUtil.copyProperties(o, exportVo);
            exportDataList.add(exportVo);
        });
        Page<ClueExportVo> page = new Page<>(pageResult.getCurrentPage(), pageResult.getPageSize(), pageResult.getTotal());
        return page.setRecords(exportDataList);
    }

}
