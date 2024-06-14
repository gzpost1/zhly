package cn.cuiot.dmp.system.infrastructure.persistence.mapper;

import cn.cuiot.dmp.system.infrastructure.entity.dto.ClientUserQuery;
import cn.cuiot.dmp.system.infrastructure.entity.vo.ClientUserVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * C端用户Mapper
 * @author: wuyongchong
 * @date: 2024/6/14 9:29
 */
public interface ClientUserMapper {

    /**
     * 分页查询
     */
    IPage<ClientUserVo> queryForList(Page<ClientUserVo> page,@Param("param") ClientUserQuery query);

}
