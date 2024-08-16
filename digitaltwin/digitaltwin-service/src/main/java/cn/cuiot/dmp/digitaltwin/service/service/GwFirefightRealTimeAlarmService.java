package cn.cuiot.dmp.digitaltwin.service.service;

import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.digitaltwin.service.entity.dto.GwFirefightRealTimeAlarmDto;
import cn.cuiot.dmp.digitaltwin.service.entity.GwFirefightRealTimeAlarmEntity;
import cn.cuiot.dmp.digitaltwin.service.entity.vo.GwFirefightRealTimeAlarmVo;
import cn.cuiot.dmp.digitaltwin.service.enums.GwFirefightNoticeRecordConstant;
import cn.cuiot.dmp.digitaltwin.service.enums.GwFirefightRealTimeAlarmTypeEnum;
import cn.cuiot.dmp.digitaltwin.service.mapper.GwFirefightRealTimeAlarmMapper;
import cn.cuiot.dmp.digitaltwin.service.entity.query.GwFirefightDeviceQuery;
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

/**
 * 格物消防-实时报警 业务层
 *
 * @Author: zc
 * @Date: 2024-06-14
 */
@Service
public class GwFirefightRealTimeAlarmService extends ServiceImpl<GwFirefightRealTimeAlarmMapper, GwFirefightRealTimeAlarmEntity> {

    @Autowired
    private GwFirefightNoticeRecordService gwFirefightNoticeRecordService;

    /**
     * 保存
     *
     * @Param dto 参数
     */
    public void save(GwFirefightRealTimeAlarmDto dto) {
        GwFirefightRealTimeAlarmEntity alarmEntity = new GwFirefightRealTimeAlarmEntity();
        BeanUtils.copyProperties(dto, alarmEntity);
        if (StringUtils.isNotBlank(dto.getReportTime())) {
            alarmEntity.setReportTime(DateTimeUtil.stringToDate(dto.getReportTime()));
            alarmEntity.setReportDate(DateTimeUtil.stringToLocalDate(dto.getReportTime()));
        }
        save(alarmEntity);
    }

    /**
     * 查询列表
     *
     * @return List<GwFirefightRealTimeAlarmVo>
     * @Param beginDate 开始日期
     * @Param endDate 结束日期
     */
    public List<GwFirefightRealTimeAlarmVo> queryAlarm(GwFirefightDeviceQuery query) {
        if (StringUtils.isBlank(query.getDeviceId())) {
            return Lists.newArrayList();
        }

        LocalDate now = LocalDate.now();
        query.setEndDate(now);
        query.setBeginDate(now.minusDays(6));
        List<GwFirefightRealTimeAlarmVo> vos = baseMapper.queryAlarm(query);

        if (CollectionUtils.isNotEmpty(vos)) {
            AtomicInteger sort = new AtomicInteger(1);
            vos.forEach(item -> {
                //设备类型名称
                item.setTypeStr(GwFirefightRealTimeAlarmTypeEnum.getDescByType(item.getType()));
                //序号
                item.setSort(sort.getAndIncrement());
            });
            //判断是需要弹框再进行操作
            if (Objects.equals(query.getNeedNotice(), EntityConstants.YES)) {
                setIsNotice(vos);
            }
        }
        return vos;
    }

    /**
     * 设备是否已通知
     */
    private void setIsNotice(List<GwFirefightRealTimeAlarmVo> vos) {
        Long id = vos.get(0).getId();
        String dataId = gwFirefightNoticeRecordService.queryDataIdCache(GwFirefightNoticeRecordConstant.REALTIME_ALARM);

        // 尝试将dataId转换为Long，如果失败则视为小于id
        Long lastDataId = StringUtils.isNotBlank(dataId) ? Long.parseLong(dataId) : 0L;

        // 如果id大于lastDataId，则设置isNotified为NO
        if (id > lastDataId) {
            vos.get(0).setIsNotified(EntityConstants.NO);
            // 都更新缓存
            gwFirefightNoticeRecordService.setDataIdCache(id + "", GwFirefightNoticeRecordConstant.REALTIME_ALARM);
        }
    }
}
