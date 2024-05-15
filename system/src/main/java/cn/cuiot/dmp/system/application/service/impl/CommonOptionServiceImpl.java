package cn.cuiot.dmp.system.application.service.impl;

import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.base.infrastructure.dto.req.CommonOptionReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.CommonOptionRspDTO;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.system.application.param.dto.BatchCommonOptionDTO;
import cn.cuiot.dmp.system.application.param.dto.CommonOptionCreateDTO;
import cn.cuiot.dmp.system.application.param.dto.CommonOptionUpdateDTO;
import cn.cuiot.dmp.system.application.param.vo.CommonOptionVO;
import cn.cuiot.dmp.system.application.service.CommonOptionService;
import cn.cuiot.dmp.system.domain.aggregate.CommonOption;
import cn.cuiot.dmp.system.domain.repository.CommonOptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author caorui
 * @date 2024/4/26
 */
@Slf4j
@Service
public class CommonOptionServiceImpl implements CommonOptionService {

    @Autowired
    private CommonOptionRepository commonOptionRepository;

    @Override
    public CommonOptionVO queryForDetail(Long id) {
        CommonOption commonOption = commonOptionRepository.queryForDetail(id);
        CommonOptionVO commonOptionVO = new CommonOptionVO();
        BeanUtils.copyProperties(commonOption, commonOptionVO);
        return commonOptionVO;
    }

    @Override
    public int saveCommonOption(CommonOptionCreateDTO createDTO) {
        CommonOption commonOption = new CommonOption();
        BeanUtils.copyProperties(createDTO, commonOption);
        return commonOptionRepository.saveCommonOption(commonOption);
    }

    @Override
    public int updateCommonOption(CommonOptionUpdateDTO updateDTO) {
        CommonOption commonOption = new CommonOption();
        BeanUtils.copyProperties(updateDTO, commonOption);
        return commonOptionRepository.updateCommonOption(commonOption);
    }

    @Override
    public int updateCommonOptionStatus(UpdateStatusParam updateStatusParam) {
        CommonOption commonOption = new CommonOption();
        BeanUtils.copyProperties(updateStatusParam, commonOption);
        return commonOptionRepository.updateCommonOptionStatus(commonOption);
    }

    @Override
    public void checkDeleteStatus(Long id) {
        commonOptionRepository.checkDeleteStatus(id);
    }

    @Override
    public int deleteCommonOption(Long id) {
        return commonOptionRepository.deleteCommonOption(id);
    }

    @Override
    public List<CommonOptionRspDTO> batchQueryCommonOption(CommonOptionReqDTO commonOptionReqDTO) {
        AssertUtil.notEmpty(commonOptionReqDTO.getIdList(), "常用选项ID列表不能为空");
        Byte status = null;
        if (Objects.nonNull(commonOptionReqDTO.getStatus())) {
            status = commonOptionReqDTO.getStatus();
        }
        List<Long> idList = commonOptionReqDTO.getIdList();
        List<CommonOption> commonOptionList = commonOptionRepository.batchQueryCommonOption(status, idList);
        return commonOptionList.stream()
                .map(o -> {
                    CommonOptionRspDTO commonOptionRspDTO = new CommonOptionRspDTO();
                    BeanUtils.copyProperties(o, commonOptionRspDTO);
                    return commonOptionRspDTO;
                }).collect(Collectors.toList());
    }

    @Override
    public int batchMoveCommonOption(BatchCommonOptionDTO batchCommonOptionDTO) {
        AssertUtil.notNull(batchCommonOptionDTO.getTypeId(), "常用选项分类不能为空");
        AssertUtil.notEmpty(batchCommonOptionDTO.getIdList(), "常用选项ID列表不能为空");
        Long typeId = batchCommonOptionDTO.getTypeId();
        List<Long> idList = batchCommonOptionDTO.getIdList();
        return commonOptionRepository.batchMoveCommonOption(typeId, idList);
    }

    @Override
    public int batchUpdateCommonOptionStatus(BatchCommonOptionDTO batchCommonOptionDTO) {
        AssertUtil.notNull(batchCommonOptionDTO.getStatus(), "常用选项状态不能为空");
        AssertUtil.notEmpty(batchCommonOptionDTO.getIdList(), "常用选项ID列表不能为空");
        Byte status = batchCommonOptionDTO.getStatus();
        List<Long> idList = batchCommonOptionDTO.getIdList();
        return commonOptionRepository.batchUpdateCommonOptionStatus(status, idList);
    }

    @Override
    public int batchDeleteCommonOption(List<Long> idList) {
        return commonOptionRepository.batchDeleteCommonOption(idList);
    }
}
