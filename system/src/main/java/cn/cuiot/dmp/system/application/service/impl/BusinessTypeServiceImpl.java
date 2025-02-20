package cn.cuiot.dmp.system.application.service.impl;

import cn.cuiot.dmp.base.infrastructure.dto.req.BusinessTypeReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.BusinessTypeRspDTO;
import cn.cuiot.dmp.common.bean.TreeNode;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.utils.TreeUtil;
import cn.cuiot.dmp.system.application.constant.BusinessTypeConstant;
import cn.cuiot.dmp.system.application.param.dto.BusinessTypeCreateDTO;
import cn.cuiot.dmp.system.application.param.dto.BusinessTypeQueryDTO;
import cn.cuiot.dmp.system.application.param.dto.BusinessTypeTreeNodeCopyDTO;
import cn.cuiot.dmp.system.application.param.dto.BusinessTypeUpdateDTO;
import cn.cuiot.dmp.system.application.param.vo.BusinessTypeTreeNodeVO;
import cn.cuiot.dmp.system.application.param.vo.BusinessTypeVO;
import cn.cuiot.dmp.system.application.service.BusinessTypeService;
import cn.cuiot.dmp.system.domain.aggregate.BusinessType;
import cn.cuiot.dmp.system.domain.repository.BusinessTypeRepository;
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
public class BusinessTypeServiceImpl implements BusinessTypeService {

    @Autowired
    private BusinessTypeRepository businessTypeRepository;

    @Override
    public BusinessTypeVO queryForDetail(Long id) {
        BusinessType businessType = businessTypeRepository.queryForDetail(id);
        BusinessTypeVO businessTypeVO = new BusinessTypeVO();
        BeanUtils.copyProperties(businessType, businessTypeVO);
        return businessTypeVO;
    }

    @Override
    public List<BusinessTypeTreeNodeVO> queryByCompany(BusinessTypeQueryDTO queryDTO) {
        List<BusinessType> businessTypeList = businessTypeRepository.queryByCompany(queryDTO.getCompanyId());
        List<BusinessTypeTreeNodeVO> businessTypeTreeNodeVOList = businessTypeList.stream()
                .map(parent -> new BusinessTypeTreeNodeVO(
                        parent.getId().toString(), parent.getParentId().toString(),
                        parent.getName(), parent.getLevelType(), parent.getCompanyId()))
                .collect(Collectors.toList());
        if (StringUtils.isBlank(queryDTO.getName())) {
            return TreeUtil.makeTree(businessTypeTreeNodeVOList);
        }
        List<BusinessTypeTreeNodeVO> businessTypeTreeNodeList = TreeUtil.makeTree(businessTypeTreeNodeVOList);
        List<String> hitIds = businessTypeTreeNodeVOList.stream()
                .filter(o -> o.getName().contains(queryDTO.getName()))
                .map(TreeNode::getId)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(hitIds)) {
            return new ArrayList<>();
        }
        return TreeUtil.searchNode(businessTypeTreeNodeList, hitIds);
    }

    @Override
    public List<BusinessTypeTreeNodeVO> queryExcludeChild(BusinessTypeQueryDTO queryDTO) {
        AssertUtil.notNull(queryDTO.getId(), "当前节点不能为空");
        List<BusinessType> businessTypeList = businessTypeRepository.queryByCompany(queryDTO.getCompanyId());
        List<BusinessTypeTreeNodeVO> businessTypeTreeNodeVOList = businessTypeList.stream()
                .map(parent -> new BusinessTypeTreeNodeVO(
                        parent.getId().toString(), parent.getParentId().toString(),
                        parent.getName(), parent.getLevelType(), parent.getCompanyId()))
                .collect(Collectors.toList());
        List<BusinessTypeTreeNodeVO> businessTypeTreeNodeList = TreeUtil.makeTree(businessTypeTreeNodeVOList);
        BusinessTypeTreeNodeVO businessTypeTreeNodeVO = TreeUtil.getTreeNode(businessTypeTreeNodeList,
                queryDTO.getId().toString());
        AssertUtil.notNull(businessTypeTreeNodeVO, "当前节点不能为空");
        List<String> treeIdList = TreeUtil.getChildTreeIdList(businessTypeTreeNodeVO);
        List<String> hitIds = businessTypeTreeNodeVOList.stream()
                .map(TreeNode::getId)
                .filter(id -> !treeIdList.contains(id))
                .collect(Collectors.toList());
        return TreeUtil.searchNode(businessTypeTreeNodeList, hitIds);
    }

    @Override
    public int saveBusinessType(BusinessTypeCreateDTO businessTypeCreateDTO) {
        AssertUtil.isTrue(BusinessTypeConstant.MAX_LEVEL_TYPE >= businessTypeCreateDTO.getLevelType(),
                "业务类型超过最大层级");
        BusinessType businessTypeParent = businessTypeRepository.queryForDetail(businessTypeCreateDTO.getParentId());
        AssertUtil.notNull(businessTypeParent, "父节点不存在");
        AssertUtil.isTrue(businessTypeCreateDTO.getLevelType() - businessTypeParent.getLevelType() == 1,
                "父节点和子节点只能相差一级");
        BusinessType businessType = new BusinessType();
        BeanUtils.copyProperties(businessTypeCreateDTO, businessType);
        return businessTypeRepository.saveBusinessType(businessType);
    }

    @Override
    public int updateBusinessType(BusinessTypeUpdateDTO businessTypeUpdateDTO) {
        AssertUtil.isTrue(BusinessTypeConstant.MAX_LEVEL_TYPE >= businessTypeUpdateDTO.getLevelType(),
                "业务类型超过最大层级");
        BusinessType businessTypeParent = businessTypeRepository.queryForDetail(businessTypeUpdateDTO.getParentId());
        AssertUtil.notNull(businessTypeParent, "父节点不存在");
        AssertUtil.isTrue(businessTypeUpdateDTO.getLevelType() - businessTypeParent.getLevelType() == 1,
                "父节点和子节点只能相差一级");
        BusinessType businessType = businessTypeRepository.queryForDetail(businessTypeUpdateDTO.getId());
        BeanUtils.copyProperties(businessTypeUpdateDTO, businessType);
        return businessTypeRepository.updateBusinessType(businessType);
    }

    @Override
    public void checkDeleteStatus(Long id) {
        businessTypeRepository.checkDeleteStatus(id);
    }

    @Override
    public int deleteBusinessType(BusinessTypeQueryDTO queryDTO) {
        AssertUtil.notNull(queryDTO.getId(), "当前节点不能为空");
        List<BusinessType> businessTypeList = businessTypeRepository.queryByCompany(queryDTO.getCompanyId());
        List<BusinessTypeTreeNodeVO> businessTypeTreeNodeVOList = businessTypeList.stream()
                .map(parent -> new BusinessTypeTreeNodeVO(
                        parent.getId().toString(), parent.getParentId().toString(),
                        parent.getName(), parent.getLevelType(), parent.getCompanyId()))
                .collect(Collectors.toList());
        List<BusinessTypeTreeNodeVO> businessTypeTreeNodeList = TreeUtil.makeTree(businessTypeTreeNodeVOList);
        BusinessTypeTreeNodeVO businessTypeTreeNodeVO = TreeUtil.getTreeNode(businessTypeTreeNodeList,
                queryDTO.getId().toString());
        AssertUtil.notNull(businessTypeTreeNodeVO, "当前节点不能为空");
        List<String> treeIdList = TreeUtil.getChildTreeIdList(businessTypeTreeNodeVO);
        return businessTypeRepository.deleteBusinessType(treeIdList);
    }

    @Override
    public List<BusinessTypeRspDTO> batchGetBusinessType(BusinessTypeReqDTO businessTypeReqDTO) {
        List<Long> businessTypeIdList = businessTypeReqDTO.getBusinessTypeIdList();
        AssertUtil.notEmpty(businessTypeIdList, "业务类型ID列表不能为空");
        // 去重
        List<Long> distinctIdList = businessTypeIdList.stream().distinct().collect(Collectors.toList());
        List<BusinessType> businessTypeList = businessTypeRepository.queryForList(distinctIdList);
        // 拼接对象返回
        List<BusinessTypeRspDTO> businessTypeRspDTOList = new ArrayList<>();
        for (BusinessType businessType : businessTypeList) {
            BusinessTypeRspDTO businessTypeRspDTO = new BusinessTypeRspDTO();
            businessTypeRspDTO.setBusinessTypeId(businessType.getId());
            businessTypeRspDTO.setTreeName(businessType.getPathName());
            businessTypeRspDTOList.add(businessTypeRspDTO);
        }
        return businessTypeRspDTOList;
    }

    private List<BusinessTypeTreeNodeVO> deepCopy(List<BusinessTypeTreeNodeVO> businessTypeTreeNodeVOList) {
        BusinessTypeTreeNodeCopyDTO businessTypeTreeNodeCopyDTO = new BusinessTypeTreeNodeCopyDTO();
        businessTypeTreeNodeCopyDTO.setBusinessTypeTreeNodeVOList(businessTypeTreeNodeVOList);
        BusinessTypeTreeNodeCopyDTO cloneDTO = SerializationUtils.clone(businessTypeTreeNodeCopyDTO);
        return cloneDTO.getBusinessTypeTreeNodeVOList();
    }

}
