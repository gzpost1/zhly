package cn.cuiot.dmp.lease.service;

import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.lease.dto.clue.*;

import java.util.List;

/**
 * @author caorui
 * @date 2024/6/2
 */
public interface ClueService {

    /**
     * 查询详情
     */
    ClueDTO queryForDetail(Long id);

    /**
     * 查询列表
     */
    List<ClueDTO> queryForList(CluePageQueryDTO queryDTO);

    /**
     * 查询列表
     */
    PageResult<ClueDTO> queryForPage(CluePageQueryDTO queryDTO);

    /**
     * 保存
     */
    int saveClue(ClueCreateDTO createDTO);

    /**
     * 更新
     */
    int updateClue(ClueUpdateDTO updateDTO);

    /**
     * 分配
     */
    int distributeClue(Long currentFollowerId);

    /**
     * 跟进
     */
    int followClue(ClueFollowDTO followDTO);

    /**
     * 完成
     */
    int finishClue(ClueFinishDTO finishDTO);

    /**
     * 删除
     */
    int deleteClue(Long id);

    /**
     * 批量分配
     */
    int batchDistributeClue(ClueBatchUpdateDTO batchUpdateDTO);

    /**
     * 批量完成
     */
    int batchFinishClue(ClueBatchUpdateDTO batchUpdateDTO);

    /**
     * 批量删除
     */
    int batchDeleteClue(List<Long> idList);

}
