package cn.cuiot.dmp.archive.application.service;

import cn.cuiot.dmp.archive.infrastructure.persistence.mapper.ArchiveOptionMapper;
import cn.cuiot.dmp.archive.infrastructure.vo.ArchiveOptionItemVo;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: wuyongchong
 * @date: 2024/6/13 11:29
 */
@Service
public class ArchiveOptionService {

    @Autowired
    private ArchiveOptionMapper archiveOptionMapper;

    /**
     * 获得系统配置选项列表
     */
    public List<ArchiveOptionItemVo> getArchiveOptionItems(Byte systemOptionType, Long companyId) {
        return archiveOptionMapper.selectArchiveOptionItems(systemOptionType, companyId);
    }

    /**
     * 获得选项ID
     */
    public Long getArchiveOptionItemId(List<ArchiveOptionItemVo> list, String configName,
            String itemName) {
        if (CollectionUtils.isEmpty(list) || StringUtils.isBlank(itemName)) {
            return null;
        }
        Optional<ArchiveOptionItemVo> findOptional = list.stream()
                .filter(item -> item.getConfigName().equals(configName) && item.getItemName()
                        .equals(itemName)).findFirst();
        if (findOptional.isPresent()) {
            return findOptional.get().getItemId();
        }
        return null;
    }

    /**
     * 获得选项名
     */
    public String getArchiveOptionItemName(List<ArchiveOptionItemVo> list, Long itemId) {
        if (CollectionUtils.isEmpty(list) || Objects.isNull(itemId)) {
            return null;
        }
        Optional<ArchiveOptionItemVo> findOptional = list.stream()
                .filter(item -> item.getItemId().equals(itemId)).findFirst();
        if (findOptional.isPresent()) {
            return findOptional.get().getItemName();
        }
        return null;
    }

}
