package cn.cuiot.dmp.lease.service;

import cn.cuiot.dmp.lease.dto.clue.ClueRecordDTO;

/**
 * @author caorui
 * @date 2024/6/2
 */
public interface ClueRecordService {

    /**
     * 查询详情
     */
    ClueRecordDTO queryForDetail(Long id);

}
