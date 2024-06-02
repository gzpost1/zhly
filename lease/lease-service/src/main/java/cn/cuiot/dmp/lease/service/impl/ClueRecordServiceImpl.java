package cn.cuiot.dmp.lease.service.impl;

import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.lease.dto.clue.ClueRecordDTO;
import cn.cuiot.dmp.lease.entity.ClueRecordEntity;
import cn.cuiot.dmp.lease.mapper.ClueRecordMapper;
import cn.cuiot.dmp.lease.service.ClueRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author caorui
 * @date 2024/6/1
 */
@Slf4j
@Service
public class ClueRecordServiceImpl extends ServiceImpl<ClueRecordMapper, ClueRecordEntity> implements ClueRecordService {

    @Override
    public ClueRecordDTO queryForDetail(Long id) {
        ClueRecordEntity clueRecordEntity = Optional.ofNullable(getById(id))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        ClueRecordDTO clueRecordDTO = new ClueRecordDTO();
        BeanUtils.copyProperties(clueRecordEntity, clueRecordDTO);
        return clueRecordDTO;
    }

}
