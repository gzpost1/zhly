package cn.cuiot.dmp.externalapi.service.mapper.hik;

import cn.cuiot.dmp.externalapi.service.entity.hik.HaikangAcsDeviceEntity;
import cn.cuiot.dmp.externalapi.service.query.hik.HaikangAcsDeviceQuery;
import cn.cuiot.dmp.externalapi.service.vo.hik.HaikangAcsDeviceVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 门禁设备信息 Mapper 接口
 * </p>
 *
 * @author wuyongchong
 * @since 2024-10-09
 */
public interface HaikangAcsDeviceMapper extends BaseMapper<HaikangAcsDeviceEntity> {

    /**
     * 查询
     */
    Page<HaikangAcsDeviceVo> searchList(Page<HaikangAcsDeviceVo> page,
            @Param("param") HaikangAcsDeviceQuery query);

    /**
     * 查询
     */
    List<HaikangAcsDeviceVo> searchList(@Param("param") HaikangAcsDeviceQuery query);

}
