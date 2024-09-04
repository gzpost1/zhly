package cn.cuiot.dmp.externalapi.service.service.unicom;

import cn.cuiot.dmp.externalapi.service.entity.unicom.UnicomEntranceGuardPersonManageEntity;
import cn.cuiot.dmp.externalapi.service.vo.unicom.UnicomEntranceGuardPersonManageQueryVO;
import cn.cuiot.dmp.externalapi.service.mapper.unicom.UnicomEntranceGuardPersonManageMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 联通格物门禁-人员管理(UnicomEntranceGuardPersonManageEntity)表服务实现类
 *
 * @author Gxp
 * @since 2024-08-22 15:47:12
 */
@Service
public class UnicomEntranceGuardPersonManageService extends ServiceImpl<UnicomEntranceGuardPersonManageMapper, UnicomEntranceGuardPersonManageEntity> {


    /**
     * 查询分页
     * @param vo
     * @return
     */
    @Transactional(readOnly = true)
    public IPage<UnicomEntranceGuardPersonManageEntity> queryForPage(UnicomEntranceGuardPersonManageQueryVO vo){
        return baseMapper.selectPage(new Page<>(vo.getPageNo(), vo.getPageSize()), buildWrapper(vo));
    }


    /**
     * 构建查询条件
     *
     * @param vo
     * @return
     */
    private LambdaQueryWrapper<UnicomEntranceGuardPersonManageEntity> buildWrapper(UnicomEntranceGuardPersonManageQueryVO vo){
        return new LambdaQueryWrapper<UnicomEntranceGuardPersonManageEntity>()
                .like(StringUtils.isNotBlank(vo.getName()), UnicomEntranceGuardPersonManageEntity::getName, vo.getName())
                .orderByDesc(UnicomEntranceGuardPersonManageEntity::getPhotoUrl);
    }


}
