package cn.cuiot.dmp.system.application.service.impl;

import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.common.utils.SnowflakeIdWorkerUtil;
import cn.cuiot.dmp.domain.types.id.OrganizationId;
import cn.cuiot.dmp.system.application.service.SysParamService;
import cn.cuiot.dmp.system.domain.entity.Organization;
import cn.cuiot.dmp.system.domain.repository.OrganizationRepository;
import cn.cuiot.dmp.system.infrastructure.entity.SysParamEntity;
import cn.cuiot.dmp.system.infrastructure.entity.dto.GetSysParamResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.SysParamDto;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.DepartmentDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.SysParamDao;
import java.util.Objects;
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
    private OrganizationRepository organizationRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addOrUpdate(SysParamDto dto) {
        SysParamEntity sysParam = sysParamDao.getByOrgId(dto.getSessionOrgId().toString());
        DepartmentDto pathByUser = departmentDao
                .getPathByUser(String.valueOf(dto.getSessionUserId()));
        String userPath = pathByUser.getPath();
        if (Objects.isNull(sysParam)) {
            // 新增配置
            dto.setId(SnowflakeIdWorkerUtil.nextId());
            sysParamDao.insert(dto);
        } else {
            sysParamDao.update(dto);
        }
        return 0;
    }

    @Override
    public GetSysParamResDto getByPath(Long sessionOrgId) {
        GetSysParamResDto res = new GetSysParamResDto();
        SysParamEntity sysParam = sysParamDao.getByOrgId(sessionOrgId.toString());
        if (Objects.nonNull(sysParam)) {
            BeanUtils.copyProperties(sysParam, res);
        }
        Organization organization = organizationRepository
                .find(new OrganizationId(sessionOrgId));
        if(StringUtils.isBlank(res.getTitle())){
            res.setTitle(organization.getOrgName());
        }
        return res;
    }

}
