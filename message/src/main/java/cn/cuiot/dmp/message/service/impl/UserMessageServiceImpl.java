package cn.cuiot.dmp.message.service.impl;//	模板

import cn.cuiot.dmp.message.dal.entity.UserMessageEntity;
import cn.cuiot.dmp.message.dal.mapper.UserMessageMapper;
import cn.cuiot.dmp.message.service.UserMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/24 11:40
 */
@Service
public class UserMessageServiceImpl extends ServiceImpl<UserMessageMapper, UserMessageEntity> implements UserMessageService {

    @Autowired
    private UserMessageMapper userMessageMapper;


}
