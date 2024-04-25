package cn.cuiot.dmp.system.application.service.impl;

import cn.cuiot.dmp.common.constant.NumberConst;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.system.application.enums.DepartmentGroupEnum;
import cn.cuiot.dmp.system.application.enums.HasChildrenEnum;
import cn.cuiot.dmp.system.application.service.TreeService;
import cn.cuiot.dmp.system.infrastructure.entity.DepartmentEntity;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.DeptTreeReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.DeptTreeResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.SpaceTreeReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.SpaceTreeResDto;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.DepartmentDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.HouseDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.OrganizationDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.SpaceDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.TreeDao;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * @author hk
 * @classname TreeServiceImpl
 * @description 树管理实现类
 * @date 2023/04/07
 */
@Slf4j
@Service
public class TreeServiceImpl implements TreeService {

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private OrganizationDao organizationDao;

    @Autowired
    private SpaceDao spaceDao;

    @Autowired
    private HouseDao houseDao;

    @Autowired
    private TreeDao treeDao;

    @Override
    public List<DeptTreeResDto> getDeptTree(DeptTreeReqDto deptTreeReqDto) {
        // 查询登录用户信息
        DepartmentDto departmentDto = departmentDao.getPathByUser(deptTreeReqDto.getUserId());
        // 首次查询，查询当前层级
        if (StringUtils.isEmpty(deptTreeReqDto.getDeptId())) {
            deptTreeReqDto.setDeptId(String.valueOf(departmentDto.getId()));
            deptTreeReqDto.setInit(Boolean.TRUE);
        }
        // 非首次查询，查询下级
        else {
            deptTreeReqDto.setInit(Boolean.FALSE);
        }
        // 根据入参deptId查询reqPath
        DepartmentEntity departmentEntity = departmentDao.selectByPrimary(Long.parseLong(deptTreeReqDto.getDeptId()));
        if (departmentEntity == null) {
            throw new BusinessException(ResultCode.DEPT_NOT_EXISTS);
        }
        String rootDeptTreePath = departmentEntity.getPath();
        // 鉴权校验
        if (!rootDeptTreePath.startsWith(departmentDto.getPath())) {
            throw new BusinessException(ResultCode.DEPT_NOT_EXISTS);
        }
        // 组织名称搜索，多层查询
        else if (!StringUtils.isEmpty(deptTreeReqDto.getDeptName())) {
            // 下级组织
            List<DeptTreeResDto> deptChildList = treeDao.getDeptChildList(deptTreeReqDto.getDGroup(), deptTreeReqDto.getInit(),deptTreeReqDto.getDeptId(),
                    deptTreeReqDto.getDeptName(), rootDeptTreePath, deptTreeReqDto.getOrgId(), deptTreeReqDto.getOrgTypeList(), deptTreeReqDto.getOrgLabelList());
            List<String> deptTreePathList = deptChildList.stream().map(DeptTreeResDto::getDeptTreePath).collect(Collectors.toList());
            return packageDeptTreeList(rootDeptTreePath, deptTreePathList);
        }
        // 无搜索条件，仅查询下一级
        else {
            // 下级组织
            List<DeptTreeResDto> deptChildList = treeDao.getDeptChildList(deptTreeReqDto.getDGroup(), deptTreeReqDto.getInit(),deptTreeReqDto.getDeptId(),
                    deptTreeReqDto.getDeptName(), rootDeptTreePath, deptTreeReqDto.getOrgId(), deptTreeReqDto.getOrgTypeList(), deptTreeReqDto.getOrgLabelList());
            return deptChildList;
        }
    }

    @Override
    public List<SpaceTreeResDto> getSpaceTree(SpaceTreeReqDto spaceTreeReqDto) {
        List<SpaceTreeResDto> spaceList = new ArrayList<>();
        // 获取登录用户信息
        DepartmentDto userDeptDto = departmentDao.getPathByUser(spaceTreeReqDto.getUserId());
        String reqPath = userDeptDto.getPath();
        Integer dGroup = Integer.parseInt(userDeptDto.getDGroup());
        if (StringUtils.isNotEmpty(spaceTreeReqDto.getSpaceId())) {
            // 入参空间组织信息
            DepartmentEntity deptEntity = departmentDao.selectByPrimary(Long.parseLong(spaceTreeReqDto.getSpaceId()));
            reqPath = deptEntity.getPath();
            // 鉴权校验
            if (!reqPath.startsWith(userDeptDto.getPath())) {
                throw new BusinessException(ResultCode.DEPT_NOT_EXISTS);
            }
            dGroup = deptEntity.getDGroup();
        } else {
            spaceTreeReqDto.setSpaceId(String.valueOf(userDeptDto.getId()));
        }
        // dgroup为房屋级已经是最后一级组织，不需要查询下级
        if (DepartmentGroupEnum.HOUSE.getCode().equals(dGroup)) {
            throw new BusinessException(ResultCode.DEPARTMENT_NOT_HAS_CHILDREN);
        }
        if (spaceTreeReqDto.getInit()) {
            // 获取所有下级组织信息
            List<SpaceTreeResDto> list = departmentDao.getSpaceListByParentId(userDeptDto.getId(),
                    dGroup, reqPath);
            SpaceTreeResDto resDto = new SpaceTreeResDto();
            resDto.setSpaceId(userDeptDto.getId().toString());
            resDto.setSpaceName(userDeptDto.getName());
            resDto.setHasChild(list.size() == 0 ? HasChildrenEnum.DEPT_NO_CHILDREN.getCode() : HasChildrenEnum.DEPT_HAS_CHILDREN.getCode());
            resDto.setDGroup(dGroup);
            resDto.setSpaceTreePath(reqPath);
            spaceList.add(resDto);
        } else {
            // 获取所有下级组织信息
            spaceList = departmentDao.getSpaceListByParentId(Long.parseLong(spaceTreeReqDto.getSpaceId()),
                dGroup, reqPath);
        }
        return spaceList;
    }

    /**
     * 拼装DeptTreeReqDto结果集
     *
     * @param rootDeptTreePath      根组织树
     * @param childDeptTreePathList 子组织树列表
     * @return
     */
    private List<DeptTreeResDto> packageDeptTreeList(String rootDeptTreePath, List<String> childDeptTreePathList) {
        if (CollectionUtils.isEmpty(childDeptTreePathList)) {
            return Collections.emptyList();
        }
        // 组织树列表查询
        List<DeptTreeResDto> deptTreeResDtoList = treeDao.getDeptList(rootDeptTreePath, childDeptTreePathList);
        // 结果转map
        Map<String, DeptTreeResDto> deptTreeResDtoMap = deptTreeResDtoList.stream()
                .collect(Collectors.toMap(k -> k.getDeptTreePath().replace("_", "-"), v -> v));
        // 遍历列表设置父子关系
        for (DeptTreeResDto deptTreeResDto : deptTreeResDtoList) {
            String childDeptTreePath = deptTreeResDto.getDeptTreePath();
            if (StringUtils.isEmpty(childDeptTreePath)) {
                continue;
            }
            childDeptTreePath = childDeptTreePath.replace("_", "-");
            if (!childDeptTreePath.contains("-")) {
                continue;
            }
            // 截取父节点deptTreePath
            String parentDeptTreePath = childDeptTreePath.substring(0, childDeptTreePath.lastIndexOf("-"));
            DeptTreeResDto parentDeptTreeResDto = deptTreeResDtoMap.get(parentDeptTreePath);
            if (parentDeptTreeResDto != null) {
                List<DeptTreeResDto> childDeptTreeTesDtoList = parentDeptTreeResDto.getChildList();
                childDeptTreeTesDtoList = (childDeptTreeTesDtoList == null ? new ArrayList<>() : childDeptTreeTesDtoList);
                childDeptTreeTesDtoList.add(deptTreeResDto);
                parentDeptTreeResDto.setChildList(childDeptTreeTesDtoList);
            }
        }
        List<DeptTreeResDto> deptTreePathList = new ArrayList<>(NumberConst.ONE);
        deptTreePathList.add(deptTreeResDtoMap.get(rootDeptTreePath));
        return deptTreePathList;
    }

}
