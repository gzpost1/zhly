package cn.cuiot.dmp.digitaltwin.service.service;

import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.digitaltwin.service.entity.dto.GwFirefightAlarmConfirmationDto;
import cn.cuiot.dmp.digitaltwin.service.entity.GwFirefightAlarmConfirmationEntity;
import cn.cuiot.dmp.digitaltwin.service.enums.GwFirefightAlarmConfirmationDealResultEnum;
import cn.cuiot.dmp.digitaltwin.service.enums.GwFirefightAlarmConfirmationFireResultEnum;
import cn.cuiot.dmp.digitaltwin.service.enums.GwFirefightAlarmConfirmationfaultResultEnum;
import cn.cuiot.dmp.digitaltwin.service.enums.GwFirefightNoticeRecordConstant;
import cn.cuiot.dmp.digitaltwin.service.mapper.GwFirefightAlarmConfirmationMapper;
import cn.cuiot.dmp.digitaltwin.service.entity.query.GwFirefightDeviceQuery;
import cn.cuiot.dmp.digitaltwin.service.entity.vo.GwFirefightAlarmConfirmationVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 格物消防-报警确认 业务层
 *
 * @Author: zc
 * @Date: 2024-06-14
 */
@Service
public class GwFirefightAlarmConfirmationService extends ServiceImpl<GwFirefightAlarmConfirmationMapper, GwFirefightAlarmConfirmationEntity> {

    @Autowired
    private GwFirefightNoticeRecordService gwFirefightNoticeRecordService;

    /**
     * 保存
     *
     * @Param dto 参数
     */
    public void save(GwFirefightAlarmConfirmationDto dto) {
        GwFirefightAlarmConfirmationEntity alarmEntity = new GwFirefightAlarmConfirmationEntity();
        BeanUtils.copyProperties(dto, alarmEntity);
        if (StringUtils.isNotBlank(dto.getReportTime())) {
            alarmEntity.setReportTime(DateTimeUtil.stringToDate(dto.getReportTime()));
            alarmEntity.setReportDate(DateTimeUtil.dateToLocalDate(alarmEntity.getReportTime()));
        }
        save(alarmEntity);
    }

    /**
     * 烟感监测-报警处理消息
     *
     * @return IdmResDTO
     * @Param
     */
    public List<GwFirefightAlarmConfirmationVo> queryAlarmConfirmation(GwFirefightDeviceQuery query) {
        List<GwFirefightAlarmConfirmationVo> result = Lists.newArrayList();

        if (StringUtils.isBlank(query.getDeviceId())) {
            return result;
        }

        LocalDate now = LocalDate.now();
        query.setEndDate(now);
        query.setBeginDate(now.minusDays(6));
        List<GwFirefightAlarmConfirmationEntity> list = baseMapper.queryAlarmConfirmation(query);

        if (CollectionUtils.isNotEmpty(list)) {
            AtomicInteger sort = new AtomicInteger(1);

            result = list.stream().map(item -> {
                GwFirefightAlarmConfirmationVo vo = new GwFirefightAlarmConfirmationVo();
                vo.setId(item.getId());
                vo.setDealType(StringUtils.isNotBlank(item.getDealType()) ? "火警" : "故障");
                vo.setReportTime(item.getReportTime());
                vo.setAlarmStatus(GwFirefightAlarmConfirmationDealResultEnum.getDescByType(item.getAlarmStatus()));
                vo.setSort(sort.getAndIncrement());

                switch (vo.getDealType()) {
                    case "火警":
                        vo.setDealResult(GwFirefightAlarmConfirmationFireResultEnum.getDescByType(item.getDealType()));
                        break;
                    case "故障":
                        vo.setDealResult(GwFirefightAlarmConfirmationfaultResultEnum.getDescByType(item.getDisposeResult()));
                        break;
                    default:
                        vo.setDealResult(null);
                }
                return vo;
            }).collect(Collectors.toList());
            //判断是需要弹框再进行操作
            if (Objects.equals(query.getNeedNotice(), EntityConstants.YES)) {
                setIsNotice(result);
            }
        }
        return result;
    }

    /**
     * 设备是否已通知
     */
    private void setIsNotice(List<GwFirefightAlarmConfirmationVo> vos) {
        Long id = vos.get(0).getId();
        String dataId = gwFirefightNoticeRecordService.queryDataIdCache(GwFirefightNoticeRecordConstant.ALARM_CONFIRMATION);

        // 尝试将dataId转换为Long，如果失败则视为小于id
        Long lastDataId = StringUtils.isNotBlank(dataId) ? Long.parseLong(dataId) : 0L;

        // 如果id大于lastDataId，则设置isNotified为NO
        if (id > lastDataId) {
            vos.get(0).setIsNotified(EntityConstants.NO);
            // 都更新缓存
            gwFirefightNoticeRecordService.setDataIdCache(id + "", GwFirefightNoticeRecordConstant.ALARM_CONFIRMATION);
        }
    }
}
