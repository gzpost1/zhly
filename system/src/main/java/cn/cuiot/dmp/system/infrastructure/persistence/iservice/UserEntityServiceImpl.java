package cn.cuiot.dmp.system.infrastructure.persistence.iservice;

import cn.cuiot.dmp.system.infrastructure.persistence.mapper.UserEntity;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.UserEntityMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author yth
 * @since 2023-08-29
 */
@Service
public class UserEntityServiceImpl extends ServiceImpl<UserEntityMapper, UserEntity> implements IUserEntityService {

}
