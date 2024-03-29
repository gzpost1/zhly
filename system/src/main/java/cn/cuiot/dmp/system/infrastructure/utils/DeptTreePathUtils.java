package cn.cuiot.dmp.system.infrastructure.utils;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.system.infrastructure.entity.DepartmentEntity;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.DepartmentDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.UserDao;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * deptTreePath获取
 * @author huwei
 */
@Component
public class DeptTreePathUtils {

    @Autowired
    private UserDao userDao;
    @Autowired
    private DepartmentDao departmentDao;

    /**
     * 通过组织id, 用户id 获取用户的组织path
     * @param orgId
     * @param userId
     */
    public String getDeptTreePath(String orgId, String userId){
        // 获取deptId
        String deptId = userDao.getDeptId(userId, orgId);
        // 获取deptTree
        if (StringUtils.isEmpty(deptId)) {
            return null;
        }
        return departmentDao.selectByPrimary(Long.parseLong(deptId)).getPath();
    }

    /**
     * 通过组织id, 用户id ,组织id获取path，如果deptId为null，则返回当前用户的path，反之则返回deptId的path
     * @param orgId 租户id
     * @param userId 用户id
     * @param deptId 组织id
     * @return
     */
    public String getDeptTreePath(String orgId, String userId, Long deptId){
        // 获取deptTree
        String path = departmentDao.getPathBySpacePath(departmentDao.selectByPrimary(Long.parseLong(userDao.getDeptId(userId, orgId))).getPath()).getPath();
        if (Objects.nonNull(deptId)) {
            DepartmentEntity departmentEntity = departmentDao.selectByPrimary(deptId);
            if (Objects.isNull(departmentEntity) || !departmentEntity.getPath().startsWith(path)) {
                throw new BusinessException(ResultCode.DEPARTMENT_QUERY_VIRES);
            }
            path = departmentEntity.getPath();
        }
        return path;
    }

    /**
     * 通过组织id, 用户id ,组织树path获取path，如果deptTreePath为null，则返回当前用户的path，反之则返回deptTreePath
     * @param orgId 租户id
     * @param userId 用户id
     * @param deptTreePath 组织树
     * @return
     */
    public String getDeptTreePath(String orgId, String userId, String deptTreePath){
        // 获取deptTree
        String path = departmentDao.selectByPrimary(Long.parseLong(userDao.getDeptId(userId, orgId))).getPath();
        if (!StringUtils.isEmpty(deptTreePath) && !deptTreePath.startsWith(path)) {
            throw new BusinessException(ResultCode.DEPARTMENT_QUERY_VIRES);
        }
        return StringUtils.isEmpty(deptTreePath) ? path : deptTreePath;
    }

    /**
     * 校验deptIdList是否为当前用户下的
     * @param orgId 租户id
     * @param userId 用户id
     * @param deptIdList 组织id
     * @return
     */
    public void checkDeptIdList(String orgId, String userId, List<Long> deptIdList) {
        if (CollectionUtils.isEmpty(deptIdList)) {
            throw new BusinessException(ResultCode.PARAM_CANNOT_NULL);
        }
        List<Long> distinctIdList = deptIdList.stream().distinct().collect(Collectors.toList());
        List<Long> idList = departmentDao.checkDeptIdList(orgId, userId, distinctIdList);
        if (deptIdList.size() != idList.size()) {
            throw new BusinessException(ResultCode.DEPT_NOT_EXISTS);
        }
    }
}
