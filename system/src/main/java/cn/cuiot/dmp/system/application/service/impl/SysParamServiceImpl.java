package cn.cuiot.dmp.system.application.service.impl;

import cn.cuiot.dmp.domain.types.id.UserId;
import cn.cuiot.dmp.system.application.service.SysParamService;
import cn.cuiot.dmp.system.infrastructure.entity.DepartmentEntity;
import cn.cuiot.dmp.system.infrastructure.entity.SysParamEntity;
import cn.cuiot.dmp.system.infrastructure.entity.dto.DepartmentDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.GetSysParamResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.SysParamDto;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.DepartmentDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.SysParamDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.UserDao;
import cn.cuiot.dmp.system.user_manage.domain.entity.User;
import cn.cuiot.dmp.system.user_manage.repository.UserRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wensq
 * @classname PropertyComplaintServiceImpl
 * @description
 * @date 2022/12/29
 */
@Slf4j
@Service
public class SysParamServiceImpl implements SysParamService {

    @Autowired
    SysParamDao sysParamDao;

    @Autowired
    DepartmentDao departmentDao;

    @Autowired
    UserDao userDao;
    @Autowired
    UserRepository userRepository;

    private static final String ZERO = "0";

    private static final String USER_DELETED = "该用户已被删除";


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addOrUpdate(SysParamDto dto) {
        SysParamEntity sysParam = sysParamDao.getByOrgId(dto.getDeptTreePath());
        DepartmentDto pathByUser = departmentDao.getPathByUser(String.valueOf(dto.getUserId()));
        String userPath = pathByUser.getPath();
        if (Objects.isNull(sysParam)) {
            // 新增配置
            dto.setUpdaterPath(userPath);
            sysParamDao.insert(dto);
        } else {
            // 编辑配置
            dto.setUpdaterPath(userPath);
            String overwrite = dto.getOverwrite();
            if (ZERO.equals(overwrite)) {
                // 覆盖，1删除组织及下级配置
                sysParamDao.deleteAll(dto.getDeptTreePath());
            } else {
                // 删除本级组织配置
                sysParamDao.deleteByPath(dto.getDeptTreePath());
            }
            // 新增配置
            sysParamDao.insert(dto);
        }
        return 0;
    }

    @Override
    public GetSysParamResDto getByPath(String reqPath, String userId) {
        GetSysParamResDto res = new GetSysParamResDto();
        res.setSelf("1");
        DepartmentDto pathByUser = departmentDao.getPathByUser(userId);
        String userPath = pathByUser.getPath();
        if (userPath.equals(reqPath)) {
            // 入参组织为用户自身组织
            res.setSelf("0");
        }
        if (StringUtils.isEmpty(reqPath)) {
            reqPath = userPath;
        }
        SysParamEntity sysParam = sysParamDao.getByOrgId(reqPath);
        if (Objects.nonNull(sysParam)) {
            // 本级存在自定义配置，直接返回
            checkSysParamConfig(sysParam, reqPath);
            BeanUtils.copyProperties(sysParam, res);
            return res;
        }
        // 本级不存在自定义配置，查询根组织配置
        String formatPath = reqPath.replaceAll("_", "-");
        List<String> pathList = Arrays.asList(formatPath.split("-"));
        int indexOf = StringUtils.ordinalIndexOf(formatPath, "-", 2);
        String orgPath = null;
        try {
            orgPath = reqPath.substring(0, indexOf);
        } catch (Exception e) {
            // 组织层级低于三级且查不到配置
            log.info("组织层级低于三级且无系统配置：{}", reqPath);
            return null;
        }
        List<SysParamEntity> list = sysParamDao.getByPathAll(orgPath);
        Collections.reverse(pathList);
        for (String path : pathList) {
            Optional<SysParamEntity> optionalSysParamEntity = list.stream().filter(param -> param.getDeptTreePath().endsWith(path)).findFirst();
            if (optionalSysParamEntity.isPresent()) {
                SysParamEntity sysParamEntity = optionalSysParamEntity.get();
                checkSysParamConfig(sysParamEntity, reqPath);
                BeanUtils.copyProperties(sysParamEntity, res);
                return res;
            }
        }
        log.info("未查询到组织对应系统参数：{}", reqPath);
        return null;
    }

    @Override
    public Boolean getSysParamWhether(String path) {
        int count = sysParamDao.getSysParamWhether(path);
        return count >= 1;
    }

    /**
     * 判断配置是否为上级修改
     *
     * @param dto
     */
    private void checkSysParamConfig(SysParamEntity dto, String reqPath) {
        String updaterPath = dto.getUpdaterPath();
        // 若修改者组织存在且与生效组织不相同，则说明是上级修改配置
        if (!StringUtils.isEmpty(updaterPath) && !updaterPath.equals(reqPath)) {
            // 获取组织名称
            DepartmentEntity deptNameByPath = departmentDao.getDeptNameByPath(updaterPath);
            dto.setUpdaterPath(deptNameByPath.getDepartmentName());
            // 获取操作人姓名
            User user = userRepository.find(new UserId(dto.getCreatedBy()));
            if (user == null) {
                dto.setCreatedBy(USER_DELETED);
            } else {
                dto.setCreatedBy(user.getUsername());
            }
        } else {
            dto.setUpdaterPath(null);
            dto.setCreatedBy(null);
            dto.setCreatedOn(null);
        }
    }
}
