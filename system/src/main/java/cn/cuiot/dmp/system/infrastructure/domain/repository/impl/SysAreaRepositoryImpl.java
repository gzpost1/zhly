package cn.cuiot.dmp.system.infrastructure.domain.repository.impl;

import cn.cuiot.dmp.system.domain.aggregate.AreaTreeNode;
import cn.cuiot.dmp.system.domain.aggregate.SysArea;
import cn.cuiot.dmp.system.domain.repository.SysAreaRepository;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.SysAreaMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

/**
 * @author caorui
 * @date 2024/5/14
 */
@Slf4j
@Repository
public class SysAreaRepositoryImpl implements SysAreaRepository {

    @Autowired
    private SysAreaMapper sysAreaMapper;

    @Override
    public List<AreaTreeNode> getAreaTree() {
        return sysAreaMapper.getAreaTree();
    }

    @Override
    public String getAreaName(String areaCode) {
        SysArea sysArea = sysAreaMapper.getAreaInfoByAreaCode(areaCode);
        if (Objects.nonNull(sysArea)) {
            if (StringUtils.isBlank(sysArea.getProvinceName())) {
                return sysArea.getName();
            }
            String provinceName = getTrimName(sysArea.getProvinceName());
            String cityName = getTrimName(sysArea.getCityName());
            String countyName = getTrimName(sysArea.getCountyName());
            StringBuilder builder = new StringBuilder(provinceName);

            if (StringUtils.isNotBlank(cityName)) {
                if (!"市辖区".equals(cityName)) {
                    builder.append("/").append(cityName);
                }
            }
            if (StringUtils.isNotBlank(countyName)) {
                builder.append("/").append(countyName);
            }
            return builder.toString();
        }
        return null;
    }

    private String getTrimName(String name) {
        if (StringUtils.isBlank(name)) {
            return "";
        }
        return name.trim();
    }

}
