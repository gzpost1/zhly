package cn.cuiot.dmp.system.infrastructure.utils;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.system.infrastructure.entity.DepartmentEntity;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.DepartmentDao;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author wen
 * @classname DepartmentUtil
 * @description 验证工具
 * @date 2020-06-23
 */
@Component
@Slf4j
public class DepartmentUtil {

    @Autowired
    private DepartmentDao departmentDao;

    public Boolean checkPrivilege(String shortDeptId, String longDeptId) {
        if (Strings.isBlank(shortDeptId) || Strings.isBlank(longDeptId)) {
            return false;
        }
        DepartmentEntity shortDept = departmentDao.selectByPrimary(Long.parseLong(shortDeptId));
        DepartmentEntity longDept = departmentDao.selectByPrimary(Long.parseLong(longDeptId));
        if (shortDept != null && longDept != null) {
            return longDept.getPath().contains(shortDept.getPath());
        }
        return true;
    }

    /**
     * 根据用户id获取组织信息
     *
     * @param userId
     * @return
     */
    public DepartmentDto getUserDept(String userId) {
        try {
            DepartmentDto dept = departmentDao.getPathByUser(userId);
            if (Objects.isNull(dept) || StringUtils.isEmpty(dept.getPath())) {
                throw new BusinessException(ResultCode.QUERY_USER_DEPT_ERROR);
            }
            return dept;
        } catch (BusinessException e) {
            throw new BusinessException(ResultCode.QUERY_USER_DEPT_ERROR, e);
        }
    }
}
