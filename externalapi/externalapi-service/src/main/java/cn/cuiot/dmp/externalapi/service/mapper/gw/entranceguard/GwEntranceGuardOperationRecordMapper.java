package cn.cuiot.dmp.externalapi.service.mapper.gw.entranceguard;

import cn.cuiot.dmp.externalapi.service.entity.gw.entranceguard.GwEntranceGuardOperationRecordEntity;
import cn.cuiot.dmp.externalapi.service.query.gw.entranceguard.GwEntranceGuardOperationQuery;
import cn.cuiot.dmp.externalapi.service.vo.gw.entranceguard.GwEntranceGuardOperationPageVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * 格物门禁-操作记录 mapper 接口
 *
 * @Author: zc
 * @Date: 2024-09-11
 */
public interface GwEntranceGuardOperationRecordMapper extends BaseMapper<GwEntranceGuardOperationRecordEntity> {

    /**
     * 分页
     *
     * @param page  分页参数
     * @param query 参数
     * @return IPage
     */
    IPage<GwEntranceGuardOperationPageVO> queryForPage(Page page, @Param("query") GwEntranceGuardOperationQuery query);
}