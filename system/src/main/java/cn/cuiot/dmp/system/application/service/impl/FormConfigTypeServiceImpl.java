package cn.cuiot.dmp.system.application.service.impl;

import cn.cuiot.dmp.base.infrastructure.dto.req.FormConfigTypeReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.FormConfigTypeRspDTO;
import cn.cuiot.dmp.common.bean.TreeNode;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.utils.TreeUtil;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.system.application.constant.FormConfigConstant;
import cn.cuiot.dmp.system.application.param.dto.FormConfigTypeTreeNodeCopyDTO;
import cn.cuiot.dmp.system.application.param.dto.FormConfigTypeCreateDTO;
import cn.cuiot.dmp.system.application.param.dto.FormConfigTypeQueryDTO;
import cn.cuiot.dmp.system.application.param.dto.FormConfigTypeUpdateDTO;
import cn.cuiot.dmp.system.application.param.vo.FormConfigTypeTreeNodeVO;
import cn.cuiot.dmp.system.application.param.vo.FormConfigTypeVO;
import cn.cuiot.dmp.system.application.param.vo.FormConfigVO;
import cn.cuiot.dmp.system.application.service.FormConfigTypeService;
import cn.cuiot.dmp.system.domain.aggregate.FormConfig;
import cn.cuiot.dmp.system.domain.aggregate.FormConfigPageQuery;
import cn.cuiot.dmp.system.domain.aggregate.FormConfigType;
import cn.cuiot.dmp.system.domain.repository.FormConfigRepository;
import cn.cuiot.dmp.system.domain.repository.FormConfigTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author caorui
 * @date 2024/4/26
 */
@Slf4j
@Service
public class FormConfigTypeServiceImpl implements FormConfigTypeService {

    @Autowired
    private FormConfigRepository formConfigRepository;

    @Autowired
    private FormConfigTypeRepository formConfigTypeRepository;

    @Override
    public FormConfigTypeVO queryForDetail(Long id) {
        FormConfigType formConfigType = formConfigTypeRepository.queryForDetail(id);
        FormConfigTypeVO formConfigTypeVO = new FormConfigTypeVO();
        BeanUtils.copyProperties(formConfigType, formConfigTypeVO);
        return formConfigTypeVO;
    }

    @Override
    public List<FormConfigTypeTreeNodeVO> queryByCompany(FormConfigTypeQueryDTO queryDTO) {
        List<FormConfigType> formConfigTypeList = formConfigTypeRepository.queryByCompany(queryDTO.getCompanyId());
        List<FormConfigTypeTreeNodeVO> formConfigTypeTreeNodeVOList = formConfigTypeList.stream()
                .map(parent -> new FormConfigTypeTreeNodeVO(
                        parent.getId().toString(), parent.getParentId().toString(),
                        parent.getName(), parent.getLevelType(), parent.getCompanyId()))
                .collect(Collectors.toList());
        if (StringUtils.isBlank(queryDTO.getName())) {
            return TreeUtil.makeTree(formConfigTypeTreeNodeVOList);
        }
        List<FormConfigTypeTreeNodeVO> formConfigTypeTreeNodeList = TreeUtil.makeTree(formConfigTypeTreeNodeVOList);
        List<String> hitIds = formConfigTypeTreeNodeVOList.stream()
                .filter(o -> o.getName().contains(queryDTO.getName()))
                .map(TreeNode::getId)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(hitIds)) {
            return new ArrayList<>();
        }
        return TreeUtil.searchNode(formConfigTypeTreeNodeList, hitIds);
    }

    @Override
    public List<FormConfigTypeTreeNodeVO> queryExcludeChild(FormConfigTypeQueryDTO queryDTO) {
        AssertUtil.notNull(queryDTO.getId(), "当前节点不能为空");
        List<FormConfigType> formConfigTypeList = formConfigTypeRepository.queryByCompany(queryDTO.getCompanyId());
        List<FormConfigTypeTreeNodeVO> formConfigTypeTreeNodeVOList = formConfigTypeList.stream()
                .map(parent -> new FormConfigTypeTreeNodeVO(
                        parent.getId().toString(), parent.getParentId().toString(),
                        parent.getName(), parent.getLevelType(), parent.getCompanyId()))
                .collect(Collectors.toList());
        List<FormConfigTypeTreeNodeVO> formConfigTypeTreeNodeList = TreeUtil.makeTree(formConfigTypeTreeNodeVOList);
        FormConfigTypeTreeNodeVO formConfigTypeTreeNodeVO = TreeUtil.getTreeNode(formConfigTypeTreeNodeList,
                queryDTO.getId().toString());
        AssertUtil.notNull(formConfigTypeTreeNodeVO, "当前节点不能为空");
        List<String> treeIdList = TreeUtil.getChildTreeIdList(formConfigTypeTreeNodeVO);
        List<String> hitIds = formConfigTypeTreeNodeVOList.stream()
                .map(TreeNode::getId)
                .filter(id -> !treeIdList.contains(id))
                .collect(Collectors.toList());
        return TreeUtil.searchNode(formConfigTypeTreeNodeList, hitIds);
    }

    @Override
    public int saveFormConfigType(FormConfigTypeCreateDTO formConfigTypeCreateDTO) {
        AssertUtil.isTrue(FormConfigConstant.MAX_LEVEL_TYPE >= formConfigTypeCreateDTO.getLevelType(),
                "表单配置类型超过最大层级");
        FormConfigType formConfigTypeParent = formConfigTypeRepository.queryForDetail(formConfigTypeCreateDTO.getParentId());
        AssertUtil.notNull(formConfigTypeParent, "父节点不存在");
        AssertUtil.isTrue(formConfigTypeCreateDTO.getLevelType() - formConfigTypeParent.getLevelType() == 1,
                "父节点和子节点只能相差一级");
        FormConfigType formConfigType = new FormConfigType();
        BeanUtils.copyProperties(formConfigTypeCreateDTO, formConfigType);
        return formConfigTypeRepository.saveFormConfigType(formConfigType);
    }

    @Override
    public int updateFormConfigType(FormConfigTypeUpdateDTO formConfigTypeUpdateDTO) {
        AssertUtil.isTrue(FormConfigConstant.MAX_LEVEL_TYPE >= formConfigTypeUpdateDTO.getLevelType(),
                "表单配置类型超过最大层级");
        FormConfigType formConfigTypeParent = formConfigTypeRepository.queryForDetail(formConfigTypeUpdateDTO.getParentId());
        AssertUtil.notNull(formConfigTypeParent, "父节点不存在");
        AssertUtil.isTrue(formConfigTypeUpdateDTO.getLevelType() - formConfigTypeParent.getLevelType() == 1,
                "父节点和子节点只能相差一级");
        FormConfigType formConfigType = formConfigTypeRepository.queryForDetail(formConfigTypeUpdateDTO.getId());
        BeanUtils.copyProperties(formConfigTypeUpdateDTO, formConfigType);
        return formConfigTypeRepository.updateFormConfigType(formConfigType);
    }

    @Override
    public int deleteFormConfigType(FormConfigTypeQueryDTO queryDTO) {
        AssertUtil.notNull(queryDTO.getId(), "当前节点不能为空");
        List<FormConfigType> formConfigTypeList = formConfigTypeRepository.queryByCompany(queryDTO.getCompanyId());
        List<FormConfigTypeTreeNodeVO> formConfigTypeTreeNodeVOList = formConfigTypeList.stream()
                .map(parent -> new FormConfigTypeTreeNodeVO(
                        parent.getId().toString(), parent.getParentId().toString(),
                        parent.getName(), parent.getLevelType(), parent.getCompanyId()))
                .collect(Collectors.toList());
        List<FormConfigTypeTreeNodeVO> formConfigTypeTreeNodeList = TreeUtil.makeTree(formConfigTypeTreeNodeVOList);
        FormConfigTypeTreeNodeVO formConfigTypeTreeNodeVO = TreeUtil.getTreeNode(formConfigTypeTreeNodeList,
                queryDTO.getId().toString());
        AssertUtil.notNull(formConfigTypeTreeNodeVO, "当前节点不能为空");
        List<String> treeIdList = TreeUtil.getChildTreeIdList(formConfigTypeTreeNodeVO);
        // 批量移动该分类及子节点下所有的表单配置到"全部"分类下
        formConfigRepository.batchMoveFormConfigDefault(treeIdList);
        return formConfigTypeRepository.deleteFormConfigType(treeIdList);
    }

    @Override
    public List<FormConfigTypeRspDTO> batchGetFormConfigType(FormConfigTypeReqDTO formConfigTypeReqDTO) {
        List<Long> formConfigTypeIdList = formConfigTypeReqDTO.getFormConfigTypeIdList();
        AssertUtil.notEmpty(formConfigTypeIdList, "表单配置类型ID列表不能为空");
        // 去重
        List<Long> distinctIdList = formConfigTypeIdList.stream().distinct().collect(Collectors.toList());
        List<FormConfigType> formConfigTypeList = formConfigTypeRepository.queryForList(distinctIdList);
        // 拼接对象返回
        List<FormConfigTypeRspDTO> formConfigTypeRspDTOList = new ArrayList<>();
        for (FormConfigType formConfigType : formConfigTypeList) {
            FormConfigTypeRspDTO formConfigTypeRspDTO = new FormConfigTypeRspDTO();
            formConfigTypeRspDTO.setFormConfigTypeId(formConfigType.getId());
            formConfigTypeRspDTO.setTreeName(formConfigType.getPathName());
            formConfigTypeRspDTOList.add(formConfigTypeRspDTO);
        }
        return formConfigTypeRspDTOList;
    }

    @Override
    public PageResult<FormConfigVO> queryFormConfigByType(FormConfigPageQuery pageQuery) {
        PageResult<FormConfig> formConfigPageResult = formConfigTypeRepository.queryFormConfigByType(pageQuery);
        if (CollectionUtils.isEmpty(formConfigPageResult.getList())) {
            return new PageResult<>();
        }
        fillTreeNameForFormConfig(formConfigPageResult.getList());
        PageResult<FormConfigVO> formConfigVOPageResult = new PageResult<>();
        List<FormConfigVO> formConfigVOList = formConfigPageResult.getList().stream()
                .map(o -> {
                    FormConfigVO formConfigVO = new FormConfigVO();
                    BeanUtils.copyProperties(o, formConfigVO);
                    return formConfigVO;
                }).collect(Collectors.toList());
        BeanUtils.copyProperties(formConfigPageResult, formConfigVOPageResult);
        formConfigVOPageResult.setList(formConfigVOList);
        return formConfigVOPageResult;
    }

    private List<FormConfigTypeTreeNodeVO> deepCopy(List<FormConfigTypeTreeNodeVO> FormConfigTypeTreeNodeVOList) {
        FormConfigTypeTreeNodeCopyDTO formConfigTypeTreeNodeCopyDTO = new FormConfigTypeTreeNodeCopyDTO();
        formConfigTypeTreeNodeCopyDTO.setFormConfigTypeTreeNodeVOList(FormConfigTypeTreeNodeVOList);
        FormConfigTypeTreeNodeCopyDTO cloneDTO = SerializationUtils.clone(formConfigTypeTreeNodeCopyDTO);
        return cloneDTO.getFormConfigTypeTreeNodeVOList();
    }

    private void fillTreeNameForFormConfig(List<FormConfig> formConfigList) {
        List<Long> formConfigTypeIdList = formConfigList.stream().map(FormConfig::getTypeId).collect(Collectors.toList());
        FormConfigTypeReqDTO formConfigTypeReqDTO = new FormConfigTypeReqDTO(formConfigTypeIdList);
        List<FormConfigTypeRspDTO> formConfigTypeRspDTOList = batchGetFormConfigType(formConfigTypeReqDTO);
        for (FormConfig formConfig : formConfigList) {
            for (FormConfigTypeRspDTO formConfigTypeRspDTO : formConfigTypeRspDTOList) {
                if (formConfigTypeRspDTO.getFormConfigTypeId().equals(formConfig.getTypeId())) {
                    formConfig.setTypeName(formConfigTypeRspDTO.getTreeName());
                    break;
                }
            }
        }
    }

}
