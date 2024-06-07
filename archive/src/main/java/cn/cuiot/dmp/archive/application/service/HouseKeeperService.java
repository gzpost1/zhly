package cn.cuiot.dmp.archive.application.service;

import cn.cuiot.dmp.archive.application.param.query.HouseKeeperQuery;
import cn.cuiot.dmp.archive.infrastructure.entity.HouseKeeperEntity;
import cn.cuiot.dmp.archive.infrastructure.persistence.mapper.HouseKeeperMapper;
import cn.cuiot.dmp.common.constant.EntityConstants;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
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
        List<HouseKeeperEntity> selectList = houseKeeperMapper.selectList(
                Wrappers.<HouseKeeperEntity>lambdaQuery().eq(HouseKeeperEntity::getId, id)
                        .eq(HouseKeeperEntity::getCompanyId, companyId));
        if (CollectionUtils.isNotEmpty(selectList)) {
            return selectList.get(0);
        }
        return null;
    }

    /**
     * 创建
     */
    public void createHouseKeeper(HouseKeeperEntity entity) {
        houseKeeperMapper.insert(entity);
    }

    /**
     * 修改
     */
    public void updateHouseKeeper(HouseKeeperEntity entity) {
        houseKeeperMapper.updateById(entity);
    }

    /**
     * 删除
     */
    public void deleteHouseKeeper(Long id, Long companyId) {
        LambdaUpdateWrapper<HouseKeeperEntity> lambdaUpdate = Wrappers.lambdaUpdate();
        lambdaUpdate.set(HouseKeeperEntity::getDeleted, EntityConstants.DELETED);
        lambdaUpdate.eq(HouseKeeperEntity::getId, id);
        lambdaUpdate.eq(HouseKeeperEntity::getCompanyId, companyId);
        houseKeeperMapper.update(null, lambdaUpdate);
    }

    /**
     * 修改状态
     */
    public void updateStatus(Long id, Byte status, Long companyId) {
        LambdaUpdateWrapper<HouseKeeperEntity> lambdaUpdate = Wrappers.lambdaUpdate();
        lambdaUpdate.set(HouseKeeperEntity::getStatus, status);
        lambdaUpdate.eq(HouseKeeperEntity::getId, id);
        lambdaUpdate.eq(HouseKeeperEntity::getCompanyId, companyId);
        houseKeeperMapper.update(null, lambdaUpdate);
    }

    /**
     * 查询列表
     * @param query
     * @return
     */
    public List<HouseKeeperEntity> queryForList(HouseKeeperQuery query) {
        return houseKeeperMapper
                .queryForList(query.getCompanyId(), query.getDeptPath(), query.getCommunityId(),
                        query.getCommunityIdList(), query.getStatus());
    }

}
