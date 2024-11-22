package cn.cuiot.dmp.archive.infrastructure.persistence.mapper;

import cn.cuiot.dmp.archive.infrastructure.vo.ArchiveOptionItemVo;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * @author: wuyongchong
 * @date: 2024/6/13 11:29
 */
public interface ArchiveOptionMapper {

    List<ArchiveOptionItemVo> selectArchiveOptionItems(@Param("systemOptionType") Byte systemOptionType, @Param("companyId") Long companyId);

}
