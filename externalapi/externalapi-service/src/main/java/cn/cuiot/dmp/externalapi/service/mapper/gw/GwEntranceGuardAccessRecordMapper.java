package cn.cuiot.dmp.externalapi.service.mapper.gw;

import cn.cuiot.dmp.externalapi.service.entity.gw.GwEntranceGuardAccessRecordEntity;
import cn.cuiot.dmp.externalapi.service.query.gw.GwEntranceGuardAccessRecordQuery;
import cn.cuiot.dmp.externalapi.service.vo.gw.GwEntranceGuardAccessRecordVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * 格物门禁-通过记录 mapper接口
 *
 * @Author: zc
 * @Date: 2024-09-10
 */
public interface GwEntranceGuardAccessRecordMapper extends BaseMapper<GwEntranceGuardAccessRecordEntity> {

    /**
     * 分页
     *
     * @param page  分页参数
     * @param query 参数
     * @return IPage
     */
    IPage<GwEntranceGuardAccessRecordVO> queryForPage(Page<?> page, @Param("query") GwEntranceGuardAccessRecordQuery query);
}