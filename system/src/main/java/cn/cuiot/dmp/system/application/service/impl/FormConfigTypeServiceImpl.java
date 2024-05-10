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
import cn.cuiot.dmp.system.application.service.FormConfigTypeService;
import cn.cuiot.dmp.system.domain.aggregate.FormConfig;
import cn.cuiot.dmp.system.domain.aggregate.FormConfigPageQuery;
import cn.cuiot.dmp.system.domain.aggregate.FormConfigType;
import cn.cuiot.dmp.system.domain.repository.FormConfigTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author caorui
 * @date 2024/4/26
 */
@Slf4j
@Service
public class FormConfigTypeServiceImpl implements FormConfigTypeService {

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
                "业务类型超过最大层级");
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
                "业务类型超过最大层级");
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
        return formConfigTypeRepository.deleteFormConfigType(treeIdList);
    }

    @Override
    public List<FormConfigTypeRspDTO> batchGetFormConfigType(FormConfigTypeReqDTO formConfigTypeReqDTO) {
        Long orgId = formConfigTypeReqDTO.getOrgId();
        List<Long> formConfigTypeIdList = formConfigTypeReqDTO.getFormConfigTypeIdList();
        AssertUtil.notNull(orgId, "组织id不能为空");
        AssertUtil.notEmpty(formConfigTypeIdList, "表单配置类型ID列表不能为空");
        List<FormConfigType> formConfigTypeList = formConfigTypeRepository.queryByCompany(orgId);
        // 拼接树型结构
        List<FormConfigTypeTreeNodeVO> formConfigTypeTreeNodeVOList = formConfigTypeList.stream()
                .map(parent -> new FormConfigTypeTreeNodeVO(
                        parent.getId().toString(), parent.getParentId().toString(),
                        parent.getName(), parent.getLevelType(), parent.getCompanyId()))
                .collect(Collectors.toList());
        List<FormConfigTypeTreeNodeVO> formConfigTypeTreeNodeList = TreeUtil.makeTree(formConfigTypeTreeNodeVOList);
        // 去重
        List<Long> distinctIdList = formConfigTypeIdList.stream().distinct().collect(Collectors.toList());
        // 获取调用id和对应树型名称的map
        Map<Long, String> invokeIdTreeNameMap = new HashMap<>();
        for (Long id : distinctIdList) {
            List<String> hitIds = new ArrayList<>();
            hitIds.add(id.toString());
            // 获取单个节点的树形结构
            List<FormConfigTypeTreeNodeVO> tmpFormConfigTypeTreeNodeList = deepCopy(formConfigTypeTreeNodeList);
            List<FormConfigTypeTreeNodeVO> invokeTreeNodeList = TreeUtil.searchNode(tmpFormConfigTypeTreeNodeList, hitIds);
            String treeName = "";
            if (CollectionUtils.isEmpty(invokeTreeNodeList)) {
                invokeIdTreeNameMap.put(id, treeName);
                continue;
            }
            FormConfigTypeTreeNodeVO rootFormConfigTypeTreeNodeVO = invokeTreeNodeList.get(0);
            treeName = TreeUtil.getParentTreeName(rootFormConfigTypeTreeNodeVO);
            invokeIdTreeNameMap.put(id, treeName);
        }
        // 拼接对象返回
        List<FormConfigTypeRspDTO> formConfigTypeRspDTOList = new ArrayList<>();
        for (Long id : distinctIdList) {
            FormConfigTypeRspDTO formConfigTypeRspDTO = new FormConfigTypeRspDTO();
            String treeName = invokeIdTreeNameMap.get(id);
            formConfigTypeRspDTO.setFormConfigTypeId(id);
            formConfigTypeRspDTO.setTreeName(treeName);
            formConfigTypeRspDTOList.add(formConfigTypeRspDTO);
        }
        return formConfigTypeRspDTOList;
    }

    @Override
    public PageResult<FormConfig> queryFormConfigByType(FormConfigPageQuery pageQuery) {
        PageResult<FormConfig> formConfigPageResult = formConfigTypeRepository.queryFormConfigByType(pageQuery);
        if (CollectionUtils.isEmpty(formConfigPageResult.getList())) {
            return formConfigPageResult;
        }
        fillTreeNameForFormConfig(formConfigPageResult.getList());
        return formConfigPageResult;
    }

    private List<FormConfigTypeTreeNodeVO> deepCopy(List<FormConfigTypeTreeNodeVO> FormConfigTypeTreeNodeVOList) {
        FormConfigTypeTreeNodeCopyDTO formConfigTypeTreeNodeCopyDTO = new FormConfigTypeTreeNodeCopyDTO();
        formConfigTypeTreeNodeCopyDTO.setFormConfigTypeTreeNodeVOList(FormConfigTypeTreeNodeVOList);
        FormConfigTypeTreeNodeCopyDTO cloneDTO = SerializationUtils.clone(formConfigTypeTreeNodeCopyDTO);
        return cloneDTO.getFormConfigTypeTreeNodeVOList();
    }

    private void fillTreeNameForFormConfig(List<FormConfig> formConfigList) {
        List<Long> formConfigTypeIdList = formConfigList.stream().map(FormConfig::getId).collect(Collectors.toList());
        Long orgId = formConfigList.get(0).getCompanyId();
        FormConfigTypeReqDTO formConfigTypeReqDTO = new FormConfigTypeReqDTO(orgId, formConfigTypeIdList);
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
