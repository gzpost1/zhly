package cn.cuiot.dmp.lease.mapper.charge;

import cn.cuiot.dmp.lease.dto.charge.SecuritydepositManagerPageDto;
import cn.cuiot.dmp.lease.dto.charge.SecuritydepositManagerQuery;
import cn.cuiot.dmp.lease.entity.charge.TbSecuritydepositManager;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

public interface TbSecuritydepositManagerMapper extends BaseMapper<TbSecuritydepositManager> {
    IPage<SecuritydepositManagerPageDto> queryForPage(Page page, @Param("query") SecuritydepositManagerQuery query);
}