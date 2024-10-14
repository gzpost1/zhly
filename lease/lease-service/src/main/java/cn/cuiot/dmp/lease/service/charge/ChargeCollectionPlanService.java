package cn.cuiot.dmp.lease.service.charge;

import cn.cuiot.dmp.base.application.dto.ExcelReportDto;
import cn.cuiot.dmp.base.application.service.ExcelExportService;
import cn.cuiot.dmp.base.infrastructure.dto.BaseUserDto;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.base.infrastructure.dto.req.BaseUserReqDto;
import cn.cuiot.dmp.common.constant.InformTypeConstant;
import cn.cuiot.dmp.common.constant.NumberConst;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.constant.StatusConst;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.lease.dto.charge.*;
import cn.cuiot.dmp.lease.entity.charge.ChargeCollectionPlanBuildingEntity;
import cn.cuiot.dmp.lease.entity.charge.ChargeCollectionPlanEntity;
import cn.cuiot.dmp.lease.entity.charge.ChargeCollectionPlanItemEntity;
import cn.cuiot.dmp.lease.enums.ChannelEnum;
import cn.cuiot.dmp.lease.enums.ChargeCollectionPlainCronTypeEnum;
import cn.cuiot.dmp.lease.enums.ChargeCollectionTypeEnum;
import cn.cuiot.dmp.lease.feign.SystemToFlowService;
import cn.cuiot.dmp.lease.mapper.charge.ChargeCollectionPlanBuildingMapper;
import cn.cuiot.dmp.lease.mapper.charge.ChargeCollectionPlanItemMapper;
import cn.cuiot.dmp.lease.mapper.charge.ChargeCollectionPlanMapper;
import cn.cuiot.dmp.lease.vo.*;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.collect.Maps.newHashMap;

/**
 * 收费管理-催款计划 业务层
 *
 * @author zc
 */
@Service
public class ChargeCollectionPlanService extends ServiceImpl<ChargeCollectionPlanMapper, ChargeCollectionPlanEntity> {

    @Autowired
    private ChargeCollectionPlanBuildingMapper chargeCollectionPlanBuildingMapper;
    @Autowired
    private ChargeCollectionPlanItemMapper chargeCollectionPlanItemMapper;
    @Autowired
    private SystemToFlowService systemToFlowService;
    @Autowired
    private ChargeCollectionManageService chargeCollectionManageService;
    @Autowired
    private ExcelExportService excelExportService;


    /**
     * 分页
     */
    public IPage<ChargeCollectionPlanPageVo> queryForPage(ChargeCollectionPlanPageQuery query) {
        IPage<ChargeCollectionPlanPageVo> page = getBaseMapper().queryForPage(new Page<>(query.getPageNo(), query.getPageSize()), query);
        if (Objects.nonNull(page) && CollectionUtils.isNotEmpty(page.getRecords())) {
            //获取通知单当前分页ids列表
            List<Long> ids = page.getRecords().stream().map(ChargeCollectionPlanPageVo::getId).collect(Collectors.toList());
            //获取楼盘名称信息
            Map<Long, List<String>> buildingNameMap = getBuildingNameMap(ids);
            //获取收费项目名称信息
            Map<Long, List<String>> chargeItemNameMap = getChargeItemNameMap(ids);

            //获取系统用户信息
            List<Long> userIds = page.getRecords().stream().map(ChargeCollectionPlanPageVo::getCreateUser)
                    .filter(Objects::nonNull).distinct().collect(Collectors.toList());
            Map<Long, BaseUserDto> systemUserMap = getSystemUserMap(userIds);

            //设置楼盘名称、收费项目名称、操作人名称
            for (ChargeCollectionPlanPageVo vo : page.getRecords()) {
                if (buildingNameMap.containsKey(vo.getId())) {
                    vo.setBuildingsName(String.join(",", buildingNameMap.get(vo.getId())));
                }
                if (chargeItemNameMap.containsKey(vo.getId())) {
                    vo.setChargeItemsName(String.join(",", chargeItemNameMap.get(vo.getId())));
                }
                if (systemUserMap.containsKey(vo.getCreateUser())) {
                    vo.setOperatorName(systemUserMap.get(vo.getCreateUser()).getName());
                }
            }
        }
        return page;
    }

    /**
     * 导出
     * @param dto
     * @throws Exception
     */

    public void export(ChargeCollectionPlanPageQuery dto) throws Exception {
        excelExportService.excelExport(ExcelReportDto.<ChargeCollectionPlanPageQuery,ChargeCollectionPlanPageVo>builder().title("催款计划导出").fileName("催款计划导出")
                .dataList(queryChargeCollectionPlan(dto)).build(),ChargeCollectionPlanPageVo.class);
    }
    /**
     * 查询催缴计划列表
     * @param query
     * @return
     */
    public List<ChargeCollectionPlanPageVo> queryChargeCollectionPlan(ChargeCollectionPlanPageQuery query){
        query.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        Boolean flag =true;
        Long pageNo = 1L;
        query.setPageSize(NumberConst.PAGE_MAX_SIZE);
        List<ChargeCollectionPlanPageVo> resultList = new ArrayList<>();
        do {
            query.setPageNo(pageNo);
            IPage<ChargeCollectionPlanPageVo> page = getBaseMapper().queryForPage(new Page<>(query.getPageNo(), query.getPageSize()), query);
            if(page.getTotal()> NumberConst.QUERY_MAX_SIZE){
                throw new BusinessException(ResultCode.EXPORT_DATA_OVER_LIMIT);
            }
            if(CollectionUtils.isEmpty(page.getRecords())){
                flag=false;
            }

                if (Objects.nonNull(page) && CollectionUtils.isNotEmpty(page.getRecords())) {
                //获取通知单当前分页ids列表
                List<Long> ids = page.getRecords().stream().map(ChargeCollectionPlanPageVo::getId).collect(Collectors.toList());
                //获取楼盘名称信息
                Map<Long, List<String>> buildingNameMap = getBuildingNameMap(ids);
                //获取收费项目名称信息
                Map<Long, List<String>> chargeItemNameMap = getChargeItemNameMap(ids);

                //获取系统用户信息
                List<Long> userIds = page.getRecords().stream().map(ChargeCollectionPlanPageVo::getCreateUser)
                        .filter(Objects::nonNull).distinct().collect(Collectors.toList());
                Map<Long, BaseUserDto> systemUserMap = getSystemUserMap(userIds);

                //设置楼盘名称、收费项目名称、操作人名称
                for (ChargeCollectionPlanPageVo vo : page.getRecords()) {
                    if (buildingNameMap.containsKey(vo.getId())) {
                        vo.setBuildingsName(String.join(",", buildingNameMap.get(vo.getId())));
                    }
                    if (chargeItemNameMap.containsKey(vo.getId())) {
                        vo.setChargeItemsName(String.join(",", chargeItemNameMap.get(vo.getId())));
                    }
                    if (systemUserMap.containsKey(vo.getCreateUser())) {
                        vo.setOperatorName(systemUserMap.get(vo.getCreateUser()).getName());
                    }
                }
                    page.getRecords().stream().forEach(item->{
                        item.setChannelName(ChannelEnum.getDesc(item.getChannel()));
                        item.setCornTypeName(ChargeCollectionPlainCronTypeEnum.getByCode(item.getCronType()).getDesc());
                        item.setStatusName(Objects.equals(item.getStatus(), NumberConst.DATA_STATUS)? StatusConst.STOP: StatusConst.ENABLE);
                    });
                    pageNo++;
                    resultList.addAll(page.getRecords());
            }
        }while (flag);

        return resultList;
    }

    /**
     * 根据通知单id查询楼盘名称并返回map
     *
     * @Param chargeNoticeIds 通知单ids
     */
    private Map<Long, List<String>> getBuildingNameMap(List<Long> chargeNoticeIds) {
        List<ChargeBuildingDto> buildingNames = chargeCollectionPlanBuildingMapper.getBuildingNamesByPlanId(chargeNoticeIds);
        Map<Long, List<String>> buildMap = newHashMap();
        if (CollectionUtils.isNotEmpty(buildingNames)) {
            buildMap = buildingNames.stream()
                    .collect(Collectors.groupingBy(ChargeBuildingDto::getDataId,
                            Collectors.mapping(ChargeBuildingDto::getBuildingName, Collectors.toList())));
        }
        return buildMap;
    }

    /**
     * 根据通知单id查询收费项目名称并返回map
     *
     * @Param chargeNoticeIds 通知单ids
     */
    private Map<Long, List<String>> getChargeItemNameMap(List<Long> chargeNoticeIds) {
        List<ChargeChargeItemDto> chargeItemNames = chargeCollectionPlanItemMapper.getChargeItemNamesByPlanId(chargeNoticeIds);
        Map<Long, List<String>> chargeItemMap = newHashMap();
        if (CollectionUtils.isNotEmpty(chargeItemNames)) {
            chargeItemMap = chargeItemNames.stream()
                    .collect(Collectors.groupingBy(ChargeChargeItemDto::getDataId,
                            Collectors.mapping(ChargeChargeItemDto::getChargeItemName, Collectors.toList())));
        }
        return chargeItemMap;
    }

    /**
     * 根据通知单创建用户id查询系统用户名称并返回map
     *
     * @Param createUserIds 创建人ids
     */
    private Map<Long, BaseUserDto> getSystemUserMap(List<Long> createUserIds) {
        BaseUserReqDto baseUserReqDto = new BaseUserReqDto();
        baseUserReqDto.setUserIdList(createUserIds);
        List<BaseUserDto> baseUserDtoList = systemToFlowService.lookUpUserList(baseUserReqDto);
        return baseUserDtoList.stream().collect(Collectors.toMap(BaseUserDto::getId, e -> e));
    }

    /**
     * 详情
     */
    public ChargeCollectionPlanVo queryForDetail(Long planId) {
        ChargeCollectionPlanVo planVo = new ChargeCollectionPlanVo();

        ChargeCollectionPlanEntity planEntity = getById(planId);
        if (Objects.nonNull(planEntity)) {
            BeanUtils.copyProperties(planEntity, planVo);

            //设置楼盘信息
            List<ChargeCollectionPlanBuildingEntity> buildingList = getBuildingListByPlanId(planId);
            if (CollectionUtils.isNotEmpty(buildingList)) {
                List<Long> buildingIds = buildingList.stream().map(ChargeCollectionPlanBuildingEntity::getBuildingId)
                        .collect(Collectors.toList());
                planVo.setBuildings(buildingIds);
            }
            //设置收费项目信息
            List<ChargeCollectionPlanItemEntity> chargeItemList = getChargeItemListByPlanId(planId);
            if (CollectionUtils.isNotEmpty(chargeItemList)) {
                List<Long> chargeItemIds = chargeItemList.stream().map(ChargeCollectionPlanItemEntity::getChargeItemId)
                        .collect(Collectors.toList());
                planVo.setChargeItems(chargeItemIds);
            }
        }
        return planVo;
    }

    /**
     * 创建
     *
     * @Param dto 参数
     */
    @Transactional(rollbackFor = Exception.class)
    public void create(ChargeCollectionPlanCreateDto dto) {
        dataCheck(dto);

        ChargeCollectionPlanEntity planEntity = new ChargeCollectionPlanEntity();
        BeanUtils.copyProperties(dto, planEntity);
        planEntity.setId(IdWorker.getId());
        planEntity.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        save(planEntity);

        //保存楼盘信息
        saveBuilding(planEntity.getId(), dto.getBuildings());
        //保存收费项目信息
        saveChargeItem(planEntity.getId(), dto.getChargeItems());
    }

    /**
     * 修改
     *
     * @Param dto 参数
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(ChargeCollectionPlanUpdateDto dto) {
        dataCheck(dto);

        Long planId = dto.getId();
        ChargeCollectionPlanEntity planEntity = getOptById(planId)
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST, "数据不存在"));
        BeanUtils.copyProperties(dto, planEntity);
        updateById(planEntity);

        //删除历史楼盘信息
        deleteBuildingByPlanId(planId);
        //保存楼盘信息
        saveBuilding(planId, dto.getBuildings());

        //删除收费项目信息
        deleteChargeItemByPlanId(planId);
        //保存收费项目信息
        saveChargeItem(planId, dto.getChargeItems());
    }

    /**
     * 启停用
     */
    public void updateStatus(UpdateStatusParam param) {
        ChargeCollectionPlanEntity planEntity = getOptById(param.getId())
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST, "数据不存在"));
        planEntity.setStatus(param.getStatus());
        updateById(planEntity);
    }

    /**
     * 删除
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long planId) {
        removeById(planId);
//        deleteBuildingByPlanId(planId);
//        deleteChargeItemByPlanId(planId);
    }

    /**
     * 数据校验
     */
    private void dataCheck(ChargeCollectionPlanCreateDto dto) {
        List<Byte> msgTypes = Arrays.asList(InformTypeConstant.SMS, InformTypeConstant.SYS_MSG);
        if (!msgTypes.contains(dto.getChannel())) {
            throw new BusinessException(ResultCode.REQ_PARAM_ERROR, "通知渠道错误");
        }
        Byte cronType = dto.getCronType();
        ChargeCollectionPlainCronTypeEnum cronTypeEnum = ChargeCollectionPlainCronTypeEnum.getByCode(cronType);
        if (Objects.isNull(cronTypeEnum)) {
            throw new BusinessException(ResultCode.REQ_PARAM_ERROR, "发送日期类型值错误");
        }
        if (Objects.equals(cronType, ChargeCollectionPlainCronTypeEnum.WEEKLY.getCode()) && Objects.isNull(dto.getCronAppointWeek())) {
            throw new BusinessException(ResultCode.REQ_PARAM_ERROR, "每周发送时间不能为空");
        }
        if (Objects.equals(cronType, ChargeCollectionPlainCronTypeEnum.MONTHLY.getCode()) && Objects.isNull(dto.getCronAppointDay())) {
            throw new BusinessException(ResultCode.REQ_PARAM_ERROR, "每月发送时间不能为空");
        }
    }

    /**
     * 根据计划id删除楼盘信息
     */
    private void deleteBuildingByPlanId(Long planId) {
        chargeCollectionPlanBuildingMapper.delete(new LambdaQueryWrapper<ChargeCollectionPlanBuildingEntity>()
                .eq(ChargeCollectionPlanBuildingEntity::getChargeCollectionPlanId, planId));
    }

    /**
     * 根据计划id删除楼盘信息
     */
    private void deleteChargeItemByPlanId(Long planId) {
        chargeCollectionPlanItemMapper.delete(new LambdaQueryWrapper<ChargeCollectionPlanItemEntity>()
                .eq(ChargeCollectionPlanItemEntity::getChargeCollectionPlanId, planId));
    }

    /**
     * 保存楼盘信息
     */
    private void saveBuilding(Long planId, List<Long> buildings) {
        List<ChargeCollectionPlanBuildingEntity> buildingList = buildings.stream().map(item -> {
            ChargeCollectionPlanBuildingEntity building = new ChargeCollectionPlanBuildingEntity();
            building.setChargeCollectionPlanId(planId);
            building.setBuildingId(item);
            return building;
        }).collect(Collectors.toList());
        chargeCollectionPlanBuildingMapper.batchInsert(buildingList);
    }

    /**
     * 保存楼盘信息
     */
    private void saveChargeItem(Long planId, List<Long> chargeItems) {
        List<ChargeCollectionPlanItemEntity> chargeItemList = chargeItems.stream().map(item -> {
            ChargeCollectionPlanItemEntity itemEntity = new ChargeCollectionPlanItemEntity();
            itemEntity.setChargeCollectionPlanId(planId);
            itemEntity.setChargeItemId(item);
            return itemEntity;
        }).collect(Collectors.toList());
        chargeCollectionPlanItemMapper.batchInsert(chargeItemList);
    }

    /**
     * 查询楼盘信息
     */
    private List<ChargeCollectionPlanBuildingEntity> getBuildingListByPlanId(Long planId) {
        return chargeCollectionPlanBuildingMapper.selectList(
                new LambdaQueryWrapper<ChargeCollectionPlanBuildingEntity>()
                        .eq(ChargeCollectionPlanBuildingEntity::getChargeCollectionPlanId, planId));

    }

    /**
     * 查询收费项目信息
     */
    private List<ChargeCollectionPlanItemEntity> getChargeItemListByPlanId(Long planId) {
        return chargeCollectionPlanItemMapper.selectList(
                new LambdaQueryWrapper<ChargeCollectionPlanItemEntity>()
                        .eq(ChargeCollectionPlanItemEntity::getChargeCollectionPlanId, planId));

    }

    /**
     * 实体分页查询
     */
    public IPage<ChargeCollectionPlanEntity> queryEntityForPage(Page<ChargeCollectionPlanEntity> pageQuery, ChargeCollectionPlanEntity entity) {
        LambdaQueryWrapper<ChargeCollectionPlanEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(entity.getName()), ChargeCollectionPlanEntity::getName, entity.getName());
        wrapper.eq(Objects.nonNull(entity.getChannel()), ChargeCollectionPlanEntity::getChannel, entity.getChannel());
        wrapper.eq(Objects.nonNull(entity.getCronType()), ChargeCollectionPlanEntity::getCronType, entity.getCronType());
        wrapper.eq(Objects.nonNull(entity.getStatus()), ChargeCollectionPlanEntity::getStatus, entity.getStatus());
        wrapper.eq(Objects.nonNull(entity.getCompanyId()), ChargeCollectionPlanEntity::getCompanyId, entity.getCompanyId());
        return baseMapper.selectPage(pageQuery, wrapper);
    }

    /**
     * 自动计划-执行任务
     */
    public void createChargePlainDayTask(List<ChargeCollectionPlanEntity> list) {
        list = list.stream().filter(e -> {
            if (Objects.equals(e.getCronType(), ChargeCollectionPlainCronTypeEnum.DAILY.getCode())) {
                return true;
            } else if(Objects.equals(e.getCronType(), ChargeCollectionPlainCronTypeEnum.WEEKLY.getCode())){
                int dayOfWeek = LocalDate.now().getDayOfWeek().getValue();
                return Objects.equals(e.getCronAppointWeek(), dayOfWeek);
            }else {
                int dayOfMonth = LocalDate.now().getDayOfMonth();
                if (Objects.equals(e.getCronAppointDay(), dayOfMonth)) {
                    return true;
                }
                int endDay = DateTimeUtil.dateToLocalDate(DateUtil.endOfMonth(new Date())).getDayOfMonth();
                return Objects.equals(dayOfMonth, endDay) && e.getCronAppointDay() > dayOfMonth;
            }
        }).collect(Collectors.toList());

        senMsg(list);
    }

    /**
     * 发送消息
     */
    public void senMsg(List<ChargeCollectionPlanEntity> list) {
        //获取前一天23:59:59
        Date date = DateTimeUtil.localDateTimeToDate(LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MAX)
                .withNano(999999000));
        if (CollectionUtils.isNotEmpty(list)) {
            for (ChargeCollectionPlanEntity planEntity : list) {
                ChargeCollectionManageSendQuery sendQuery = new ChargeCollectionManageSendQuery();
                sendQuery.setCompanyId(planEntity.getCompanyId());
                sendQuery.setPlanId(planEntity.getId());
                sendQuery.setMsgType(planEntity.getChannel());
                sendQuery.setDueDate(date);
                sendQuery.setOperationType(ChargeCollectionTypeEnum.AUTO.getCode());
                chargeCollectionManageService.sengMsg(sendQuery);
            }
        }
    }
}