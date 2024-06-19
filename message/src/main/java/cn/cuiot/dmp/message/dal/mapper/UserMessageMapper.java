package cn.cuiot.dmp.message.dal.mapper;//	模板

import cn.cuiot.dmp.base.infrastructure.dto.req.MsgExistDataIdReqDto;
import cn.cuiot.dmp.message.dal.entity.UserMessageEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/24 11:34
 */
public interface UserMessageMapper extends BaseMapper<UserMessageEntity> {

    List<Long> getAcceptDataIdList(@Param("param") MsgExistDataIdReqDto reqDto);
}
