package cn.cuiot.dmp.system.application.service.impl;

import cn.cuiot.dmp.base.infrastructure.dto.req.CommonOptionTypeReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.CommonOptionTypeRspDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.DepartmentTreeRspDTO;
import cn.cuiot.dmp.common.bean.TreeNode;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.utils.TreeUtil;
import cn.cuiot.dmp.system.application.constant.CommonOptionConstant;
import cn.cuiot.dmp.system.application.param.dto.CommonOptionTypeCreateDTO;
import cn.cuiot.dmp.system.application.param.dto.CommonOptionTypeQueryDTO;
import cn.cuiot.dmp.system.application.param.dto.CommonOptionTypeUpdateDTO;
import cn.cuiot.dmp.system.application.param.dto.CommonOptionTypeTreeNodeCopyDTO;
import cn.cuiot.dmp.system.application.param.vo.*;
import cn.cuiot.dmp.system.application.service.CommonOptionTypeService;
import cn.cuiot.dmp.system.domain.aggregate.CommonOptionPageQuery;
import cn.cuiot.dmp.system.domain.aggregate.CommonOptionType;
import cn.cuiot.dmp.system.domain.aggregate.CommonOption;
import cn.cuiot.dmp.system.domain.repository.CommonOptionRepository;
import cn.cuiot.dmp.system.domain.repository.CommonOptionTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author caorui
 * @date 2024/4/26
 */
@Slf4j
@Service
public class CommonOptionTypeServiceImpl implements CommonOptionTypeService {

    @Autowired
    private CommonOptionRepository commonOptionRepository;

    @Autowired
    private CommonOptionTypeRepository commonOptionTypeRepository;

    @Override
    public CommonOptionTypeVO queryForDetail(Long id) {
        CommonOptionType commonOptionType = commonOptionTypeRepository.queryForDetail(id);
        CommonOptionTypeVO commonOptionTypeVO = new CommonOptionTypeVO();
        BeanUtils.copyProperties(commonOptionType, commonOptionTypeVO);
        return commonOptionTypeVO;
    }

    @Override
    public List<CommonOptionTypeTreeNodeVO> queryByCompany(CommonOptionTypeQueryDTO queryDTO) {
        List<CommonOptionType> commonOptionTypeList = commonOptionTypeRepository.queryByCompany(queryDTO.getCompanyId(), queryDTO.getCategory());
        List<CommonOptionTypeTreeNodeVO> commonOptionTypeTreeNodeVOList = commonOptionTypeList.stream()
                .map(parent -> new CommonOptionTypeTreeNodeVO(
                        parent.getId().toString(), parent.getParentId().toString(),
                        parent.getName(), parent.getLevelType(), parent.getCompanyId()))
                .collect(Collectors.toList());
        if (StringUtils.isBlank(queryDTO.getName())) {
            return TreeUtil.makeTree(commonOptionTypeTreeNodeVOList);
        }
        List<CommonOptionTypeTreeNodeVO> commonOptionTypeTreeNodeList = TreeUtil.makeTree(commonOptionTypeTreeNodeVOList);
        List<String> hitIds = commonOptionTypeTreeNodeVOList.stream()
                .filter(o -> o.getName().contains(queryDTO.getName()))
                .map(TreeNode::getId)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(hitIds)) {
            return new ArrayList<>();
        }
        return TreeUtil.searchNode(commonOptionTypeTreeNodeList, hitIds);
    }

    @Override
    public List<CommonOptionTypeTreeNodeVO> queryCommonOptionTypeTree(CommonOptionTypeQueryDTO queryDTO) {
        List<CommonOptionTypeTreeNodeVO> commonOptionTypeTreeNodeVOList = queryByCompany(queryDTO);
        if (CollectionUtils.isEmpty(commonOptionTypeTreeNodeVOList)) {
            return new ArrayList<>();
        }
        // 查询选项名称和选项值
        CommonOptionPageQuery pageQuery = new CommonOptionPageQuery();
        pageQuery.setCompanyId(queryDTO.getCompanyId());
        pageQuery.setCategory(queryDTO.getCategory());
        pageQuery.setTypeIdList(getTypeIdList(commonOptionTypeTreeNodeVOList.get(0)));
        List<CommonOption> commonOptionList = commonOptionRepository.queryCommonOptionListByType(pageQuery);
        fillCommonOptionTypeTree(commonOptionTypeTreeNodeVOList.get(0), commonOptionList);
        return commonOptionTypeTreeNodeVOList;
    }

    @Override
    public List<CommonOptionTypeTreeNodeVO> queryExcludeChild(CommonOptionTypeQueryDTO queryDTO) {
        AssertUtil.notNull(queryDTO.getId(), "当前节点不能为空");
        List<CommonOptionType> commonOptionTypeList = commonOptionTypeRepository.queryByCompany(queryDTO.getCompanyId(), queryDTO.getCategory());
        List<CommonOptionTypeTreeNodeVO> commonOptionTypeTreeNodeVOList = commonOptionTypeList.stream()
                .map(parent -> new CommonOptionTypeTreeNodeVO(
                        parent.getId().toString(), parent.getParentId().toString(),
                        parent.getName(), parent.getLevelType(), parent.getCompanyId()))
                .collect(Collectors.toList());
        List<CommonOptionTypeTreeNodeVO> commonOptionTypeTreeNodeList = TreeUtil.makeTree(commonOptionTypeTreeNodeVOList);
        CommonOptionTypeTreeNodeVO commonOptionTypeTreeNodeVO = TreeUtil.getTreeNode(commonOptionTypeTreeNodeList,
                queryDTO.getId().toString());
        AssertUtil.notNull(commonOptionTypeTreeNodeVO, "当前节点不能为空");
        List<String> treeIdList = TreeUtil.getChildTreeIdList(commonOptionTypeTreeNodeVO);
        List<String> hitIds = commonOptionTypeTreeNodeVOList.stream()
                .map(TreeNode::getId)
                .filter(id -> !treeIdList.contains(id))
                .collect(Collectors.toList());
        return TreeUtil.searchNode(commonOptionTypeTreeNodeList, hitIds);
    }

    @Override
    public int saveCommonOptionType(CommonOptionTypeCreateDTO commonOptionTypeCreateDTO) {
        AssertUtil.isTrue(CommonOptionConstant.MAX_LEVEL_TYPE >= commonOptionTypeCreateDTO.getLevelType(),
                "常用选项类型超过最大层级");
        CommonOptionType commonOptionTypeParent = commonOptionTypeRepository.queryForDetail(commonOptionTypeCreateDTO.getParentId());
        AssertUtil.notNull(commonOptionTypeParent, "父节点不存在");
        AssertUtil.isTrue(commonOptionTypeCreateDTO.getLevelType() - commonOptionTypeParent.getLevelType() == 1,
                "父节点和子节点只能相差一级");
        CommonOptionType commonOptionType = new CommonOptionType();
        BeanUtils.copyProperties(commonOptionTypeCreateDTO, commonOptionType);
        return commonOptionTypeRepository.saveCommonOptionType(commonOptionType);
    }

    @Override
    public int updateCommonOptionType(CommonOptionTypeUpdateDTO commonOptionTypeUpdateDTO) {
        AssertUtil.isTrue(CommonOptionConstant.MAX_LEVEL_TYPE >= commonOptionTypeUpdateDTO.getLevelType(),
                "常用选项类型超过最大层级");
        CommonOptionType commonOptionTypeParent = commonOptionTypeRepository.queryForDetail(commonOptionTypeUpdateDTO.getParentId());
        AssertUtil.notNull(commonOptionTypeParent, "父节点不存在");
        AssertUtil.isTrue(commonOptionTypeUpdateDTO.getLevelType() - commonOptionTypeParent.getLevelType() == 1,
                "父节点和子节点只能相差一级");
        AssertUtil.isFalse(!Objects.equals(commonOptionTypeParent.getCategory(), commonOptionTypeUpdateDTO.getCategory()), "类别不能改变");
        CommonOptionType commonOptionType = commonOptionTypeRepository.queryForDetail(commonOptionTypeUpdateDTO.getId());
        BeanUtils.copyProperties(commonOptionTypeUpdateDTO, commonOptionType);
        return commonOptionTypeRepository.updateCommonOptionType(commonOptionType);
    }

    @Override
    public int deleteCommonOptionType(CommonOptionTypeQueryDTO queryDTO) {
        AssertUtil.notNull(queryDTO.getId(), "当前节点不能为空");
        List<CommonOptionType> commonOptionTypeList = commonOptionTypeRepository.queryByCompany(queryDTO.getCompanyId(), queryDTO.getCategory());
        List<CommonOptionTypeTreeNodeVO> commonOptionTypeTreeNodeVOList = commonOptionTypeList.stream()
                .map(parent -> new CommonOptionTypeTreeNodeVO(
                        parent.getId().toString(), parent.getParentId().toString(),
                        parent.getName(), parent.getLevelType(), parent.getCompanyId()))
                .collect(Collectors.toList());
        List<CommonOptionTypeTreeNodeVO> commonOptionTypeTreeNodeList = TreeUtil.makeTree(commonOptionTypeTreeNodeVOList);
        CommonOptionTypeTreeNodeVO commonOptionTypeTreeNodeVO = TreeUtil.getTreeNode(commonOptionTypeTreeNodeList,
                queryDTO.getId().toString());
        AssertUtil.notNull(commonOptionTypeTreeNodeVO, "当前节点不能为空");
        List<String> treeIdList = TreeUtil.getChildTreeIdList(commonOptionTypeTreeNodeVO);
        // 批量移动该分类及子节点下所有的表单配置到"全部"分类下
        Long rootTypeId = commonOptionTypeRepository.getRootTypeId(queryDTO.getCompanyId(), queryDTO.getCategory());
        commonOptionRepository.batchMoveCommonOptionDefault(treeIdList, rootTypeId);
        return commonOptionTypeRepository.deleteCommonOptionType(treeIdList);
    }

    @Override
    public List<CommonOptionTypeRspDTO> batchGetCommonOptionType(CommonOptionTypeReqDTO commonOptionTypeReqDTO) {
        List<Long> commonOptionTypeIdList = commonOptionTypeReqDTO.getCommonOptionTypeIdList();
        AssertUtil.notEmpty(commonOptionTypeIdList, "常用选项类型ID列表不能为空");
        // 去重
        List<Long> distinctIdList = commonOptionTypeIdList.stream().distinct().collect(Collectors.toList());
        List<CommonOptionType> commonOptionTypeList = commonOptionTypeRepository.queryForList(distinctIdList);
        // 拼接对象返回
        List<CommonOptionTypeRspDTO> commonOptionTypeRspDTOList = new ArrayList<>();
        for (CommonOptionType commonOptionType : commonOptionTypeList) {
            CommonOptionTypeRspDTO commonOptionTypeRspDTO = new CommonOptionTypeRspDTO();
            commonOptionTypeRspDTO.setCommonOptionTypeId(commonOptionType.getId());
            commonOptionTypeRspDTO.setTreeName(commonOptionType.getPathName());
            commonOptionTypeRspDTOList.add(commonOptionTypeRspDTO);
        }
        return commonOptionTypeRspDTOList;
    }

    @Override
    public PageResult<CommonOptionVO> queryCommonOptionByType(CommonOptionPageQuery pageQuery) {
        PageResult<CommonOption> commonOptionPageResult = commonOptionTypeRepository.queryCommonOptionByType(pageQuery);
        if (CollectionUtils.isEmpty(commonOptionPageResult.getList())) {
            return new PageResult<>();
        }
        fillTreeNameForCommonOption(commonOptionPageResult.getList());
        PageResult<CommonOptionVO> commonOptionVOPageResult = new PageResult<>();
        List<CommonOptionVO> commonOptionVOList = commonOptionPageResult.getList().stream()
                .map(o -> {
                    CommonOptionVO commonOptionVO = new CommonOptionVO();
                    BeanUtils.copyProperties(o, commonOptionVO);
                    commonOptionVO.setCommonOptionSettings(o.getCommonOptionSettings());
                    return commonOptionVO;
                }).collect(Collectors.toList());
        BeanUtils.copyProperties(commonOptionPageResult, commonOptionVOPageResult);
        commonOptionVOPageResult.setList(commonOptionVOList);
        return commonOptionVOPageResult;
    }

    private List<CommonOptionTypeTreeNodeVO> deepCopy(List<CommonOptionTypeTreeNodeVO> commonOptionTypeTreeNodeVOList) {
        CommonOptionTypeTreeNodeCopyDTO commonOptionTypeTreeNodeCopyDTO = new CommonOptionTypeTreeNodeCopyDTO();
        commonOptionTypeTreeNodeCopyDTO.setCommonOptionTypeTreeNodeVOList(commonOptionTypeTreeNodeVOList);
        CommonOptionTypeTreeNodeCopyDTO cloneDTO = SerializationUtils.clone(commonOptionTypeTreeNodeCopyDTO);
        return cloneDTO.getCommonOptionTypeTreeNodeVOList();
    }

    private void fillTreeNameForCommonOption(List<CommonOption> commonOptionList) {
        List<Long> commonOptionTypeIdList = commonOptionList.stream().map(CommonOption::getTypeId).collect(Collectors.toList());
        CommonOptionTypeReqDTO commonOptionTypeReqDTO = new CommonOptionTypeReqDTO(commonOptionTypeIdList);
        List<CommonOptionTypeRspDTO> commonOptionTypeRspDTOList = batchGetCommonOptionType(commonOptionTypeReqDTO);
        for (CommonOption commonOption : commonOptionList) {
            for (CommonOptionTypeRspDTO commonOptionTypeRspDTO : commonOptionTypeRspDTOList) {
                if (commonOptionTypeRspDTO.getCommonOptionTypeId().equals(commonOption.getTypeId())) {
                    commonOption.setTypeName(commonOptionTypeRspDTO.getTreeName());
                    break;
                }
            }
        }
    }

    private List<Long> getTypeIdList(CommonOptionTypeTreeNodeVO commonOptionTypeTreeNodeVO) {
        List<Long> treeIdList = new ArrayList<>();
        treeIdList.add(Long.valueOf(commonOptionTypeTreeNodeVO.getId()));
        commonOptionTypeTreeNodeVO.setType(CommonOptionConstant.COMMON_OPTION_TYPE);
        if (CollectionUtils.isNotEmpty(commonOptionTypeTreeNodeVO.getChildren())) {
            for (CommonOptionTypeTreeNodeVO child : commonOptionTypeTreeNodeVO.getChildren()) {
                treeIdList.addAll(getTypeIdList(child));
            }
        }
        return treeIdList;
    }

    private void fillCommonOptionTypeTree(CommonOptionTypeTreeNodeVO rootTreeNode, List<CommonOption> commonOptionList) {
        for (CommonOption commonOption : commonOptionList) {
            if (rootTreeNode.getId().equals(commonOption.getTypeId().toString())) {
                CommonOptionTypeTreeNodeVO commonOptionTypeTreeNodeVO = new CommonOptionTypeTreeNodeVO();
                commonOptionTypeTreeNodeVO.setId(commonOption.getId().toString());
                commonOptionTypeTreeNodeVO.setParentId(rootTreeNode.getId());
                commonOptionTypeTreeNodeVO.setType(CommonOptionConstant.COMMON_OPTION_NAME);
                commonOptionTypeTreeNodeVO.setName(commonOption.getName());
                if (CollectionUtils.isEmpty(commonOption.getCommonOptionSettings())) {
                    commonOptionTypeTreeNodeVO.setChildren(new ArrayList<>());
                } else {
                    List<CommonOptionTypeTreeNodeVO> commonOptionValueChildList = new ArrayList<>();
                    commonOption.getCommonOptionSettings().forEach(o -> {
                        CommonOptionTypeTreeNodeVO commonOptionTypeValueTreeNodeVO = new CommonOptionTypeTreeNodeVO();
                        commonOptionTypeValueTreeNodeVO.setId(o.getId().toString());
                        commonOptionTypeValueTreeNodeVO.setParentId(commonOption.getId().toString());
                        commonOptionTypeValueTreeNodeVO.setType(CommonOptionConstant.COMMON_OPTION_VALUE);
                        commonOptionTypeValueTreeNodeVO.setName(o.getName());
                        commonOptionValueChildList.add(commonOptionTypeValueTreeNodeVO);
                    });
                    commonOptionTypeTreeNodeVO.setChildren(commonOptionValueChildList);
                }
                rootTreeNode.getChildren().add(commonOptionTypeTreeNodeVO);
            }
        }
        if (CollectionUtils.isNotEmpty(rootTreeNode.getChildren())) {
            for (CommonOptionTypeTreeNodeVO child : rootTreeNode.getChildren()) {
                fillCommonOptionTypeTree(child, commonOptionList);
            }
        }
    }

}
