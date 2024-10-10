package cn.cuiot.dmp.externalapi.service.mapper.hik;

import cn.cuiot.dmp.externalapi.service.entity.hik.HaikangAcsDoorEntity;
import cn.cuiot.dmp.externalapi.service.query.hik.HaikangAcsDoorQuery;
import cn.cuiot.dmp.externalapi.service.vo.hik.HaikangAcsDoorVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 门禁点信息 Mapper 接口
 * </p>
 *
 * @author wuyongchong
 * @since 2024-10-09
 */
public interface HaikangAcsDoorMapper extends BaseMapper<HaikangAcsDoorEntity> {

    /**
     * 查询
     */
    Page<HaikangAcsDoorVo> searchList(Page<HaikangAcsDoorVo> page,
            @Param("param") HaikangAcsDoorQuery query);

    /**
     * 查询
     */
    List<HaikangAcsDoorVo> searchList(@Param("param") HaikangAcsDoorQuery query);
}
