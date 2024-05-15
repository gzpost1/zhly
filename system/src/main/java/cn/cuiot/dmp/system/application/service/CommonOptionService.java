package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.base.infrastructure.dto.req.CommonOptionReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.CommonOptionRspDTO;
import cn.cuiot.dmp.system.application.param.dto.BatchCommonOptionDTO;
import cn.cuiot.dmp.system.application.param.dto.CommonOptionCreateDTO;
import cn.cuiot.dmp.system.application.param.dto.CommonOptionUpdateDTO;
import cn.cuiot.dmp.system.application.param.vo.CommonOptionVO;

import java.util.List;

/**
 * @author caorui
 * @date 2024/4/26
 */
public interface CommonOptionService {

    /**
     * 根据id获取业务类型详情
     */
    CommonOptionVO queryForDetail(Long id);

    /**
     * 保存
     */
    int saveCommonOption(CommonOptionCreateDTO createDTO);

    /**
     * 更新
     */
    int updateCommonOption(CommonOptionUpdateDTO updateDTO);

    /**
     * 更新状态
     */
    int updateCommonOptionStatus(UpdateStatusParam updateStatusParam);

    /**
     * 删除预校验
     */
    void checkDeleteStatus(Long id);

    /**
     * 删除
     */
    int deleteCommonOption(Long id);

    /**
     * 批量查询
     */
    List<CommonOptionRspDTO> batchQueryCommonOption(CommonOptionReqDTO commonOptionReqDTO);

    /**
     * 批量移动
     */
    int batchMoveCommonOption(BatchCommonOptionDTO batchCommonOptionDTO);

    /**
     * 批量更新状态
     */
    int batchUpdateCommonOptionStatus(BatchCommonOptionDTO batchCommonOptionDTO);

    /**
     * 批量删除
     */
    int batchDeleteCommonOption(List<Long> idList);

}
