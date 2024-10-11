package cn.cuiot.dmp.externalapi.service.mapper.hik;

import cn.cuiot.dmp.externalapi.service.entity.hik.HaikangAcsReaderEntity;
import cn.cuiot.dmp.externalapi.service.query.hik.HaikangAcsReaderQuery;
import cn.cuiot.dmp.externalapi.service.vo.hik.HaikangAcsReaderVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 门禁读卡器信息 Mapper 接口
 * </p>
 *
 * @author wuyongchong
 * @since 2024-10-09
 */
public interface HaikangAcsReaderMapper extends BaseMapper<HaikangAcsReaderEntity> {

    /**
     * 查询
     */
    Page<HaikangAcsReaderVo> searchList(Page<HaikangAcsReaderVo> page,
            @Param("param") HaikangAcsReaderQuery query);

    /**
     * 查询
     */
    List<HaikangAcsReaderVo> searchList(@Param("param") HaikangAcsReaderQuery query);

}
