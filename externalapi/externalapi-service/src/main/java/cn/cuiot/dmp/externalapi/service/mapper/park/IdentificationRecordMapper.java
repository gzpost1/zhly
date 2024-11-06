package cn.cuiot.dmp.externalapi.service.mapper.park;

import cn.cuiot.dmp.externalapi.service.entity.park.IdentificationRecordEntity;
import cn.cuiot.dmp.externalapi.service.query.IdentificationRecordQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * 识别记录上报记录 Mapper 接口
 *
 * @author pengjian
 * @since 2024-11-06
 */
public interface IdentificationRecordMapper extends BaseMapper<IdentificationRecordEntity> {

    IPage<IdentificationRecordEntity> queryForPage(Page<IdentificationRecordEntity> objectPage, @Param("query") IdentificationRecordQuery query);
}
