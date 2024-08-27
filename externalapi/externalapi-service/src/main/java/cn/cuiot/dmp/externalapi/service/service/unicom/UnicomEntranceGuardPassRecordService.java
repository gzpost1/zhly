package cn.cuiot.dmp.externalapi.service.service.unicom;

import cn.cuiot.dmp.externalapi.service.entity.unicom.UnicomEntranceGuardPassRecordEntity;
import cn.cuiot.dmp.externalapi.service.entity.unicom.vo.UnicomEntranceGuardPassRecordQueryVO;
import cn.cuiot.dmp.externalapi.service.mapper.unicom.UnicomEntranceGuardPassRecordMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 联通格物门禁-通行记录(UnicomEntranceGuardPassRecordEntity)表服务实现类
 *
 * @author Gxp
 * @since 2024-08-22 16:04:57
 */
@Service
public class UnicomEntranceGuardPassRecordService extends ServiceImpl<UnicomEntranceGuardPassRecordMapper, UnicomEntranceGuardPassRecordEntity> {


    /**
     * 查询分页
     * @param vo
     * @return
     */
    @Transactional(readOnly = true)
    public IPage<UnicomEntranceGuardPassRecordEntity> queryForPage(UnicomEntranceGuardPassRecordQueryVO vo){
        return baseMapper.selectPage(new Page<>(vo.getPageNo(), vo.getPageSize()), buildWrapper(vo));
    }


    /**
     * 构建查询条件
     *
     * @param vo
     * @return
     */
    private LambdaQueryWrapper<UnicomEntranceGuardPassRecordEntity> buildWrapper(UnicomEntranceGuardPassRecordQueryVO vo){
        return new LambdaQueryWrapper<UnicomEntranceGuardPassRecordEntity>()
                .like(StringUtils.isNotBlank(vo.getPersonName()), UnicomEntranceGuardPassRecordEntity::getPersonName, vo.getPersonName());
    }

}
