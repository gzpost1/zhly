package cn.cuiot.dmp.lease.service;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.lease.dto.ClueRecordDTO;
import cn.cuiot.dmp.lease.entity.ClueRecordEntity;
import cn.cuiot.dmp.lease.mapper.ClueRecordMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author caorui
 * @date 2024/6/1
 */
@Slf4j
@Service
public class ClueRecordService extends ServiceImpl<ClueRecordMapper, ClueRecordEntity> {

    /**
     * 查询详情
     */
    public ClueRecordDTO queryForDetail(Long id) {
        ClueRecordEntity clueRecordEntity = Optional.ofNullable(getById(id))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        ClueRecordDTO clueRecordDTO = new ClueRecordDTO();
        BeanUtils.copyProperties(clueRecordEntity, clueRecordDTO);
        return clueRecordDTO;
    }

}
