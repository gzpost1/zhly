package cn.cuiot.dmp.archive.application.service;

import cn.cuiot.dmp.archive.application.param.query.HouseKeeperQuery;
import cn.cuiot.dmp.archive.infrastructure.entity.HouseKeeperEntity;
import cn.cuiot.dmp.archive.infrastructure.persistence.mapper.HouseKeeperMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 管家管理 服务类
 * </p>
 *
 * @author wuyongchong
 * @since 2024-06-07
 */
@Service
public class HouseKeeperService extends ServiceImpl<HouseKeeperMapper, HouseKeeperEntity> {

    @Autowired
    private HouseKeeperMapper houseKeeperMapper;

    /**
     * 分页查询
     */
    public IPage<HouseKeeperEntity> queryForPage(HouseKeeperQuery query) {
        return houseKeeperMapper
                .queryForList(new Page<HouseKeeperEntity>(query.getPageNo(), query.getPageSize()),
                        query.getCompanyId(), query.getDeptPath(), query.getCommunityId(),
                        query.getCommunityIdList(), query.getStatus());
    }

    /**
     * 获取详情
     */
    public HouseKeeperEntity queryForDetail(Long id, Long companyId) {

        return null;
    }

}
