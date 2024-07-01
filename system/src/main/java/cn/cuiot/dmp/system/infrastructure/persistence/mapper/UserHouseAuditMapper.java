package cn.cuiot.dmp.system.infrastructure.persistence.mapper;

import cn.cuiot.dmp.system.application.param.dto.UserHouseAuditDTO;
import cn.cuiot.dmp.system.application.param.dto.UserHouseAuditPageQueryDTO;
import cn.cuiot.dmp.system.infrastructure.entity.UserHouseAuditEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * @author caorui
 * @date 2024/6/13
 */
public interface UserHouseAuditMapper extends BaseMapper<UserHouseAuditEntity> {

    List<UserHouseAuditDTO> queryForList(@Param("param") UserHouseAuditPageQueryDTO queryDTO);

    IPage<UserHouseAuditDTO> queryForList(Page<UserHouseAuditEntity> page,@Param("param") UserHouseAuditPageQueryDTO queryDTO);

    UserHouseAuditDTO queryForDetail(@Param("id") Long id);

}
