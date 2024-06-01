package cn.cuiot.dmp.lease.service;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.lease.dto.ClueDTO;
import cn.cuiot.dmp.lease.entity.ClueEntity;
import cn.cuiot.dmp.lease.mapper.ClueMapper;
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
public class ClueService extends ServiceImpl<ClueMapper, ClueEntity> {

    /**
     * 查询详情
     */
    public ClueDTO queryForDetail(Long id) {
        ClueEntity clueEntity = Optional.ofNullable(getById(id))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        ClueDTO clueDTO = new ClueDTO();
        BeanUtils.copyProperties(clueEntity, clueDTO);
        return clueDTO;
    }

}
