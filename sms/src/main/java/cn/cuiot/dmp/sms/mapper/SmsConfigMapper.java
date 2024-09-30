package cn.cuiot.dmp.sms.mapper;

import cn.cuiot.dmp.sms.entity.SmsConfigEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 短信密钥 mapper接口
 *
 * @Author: zc
 * @Date: 2024-09-19
 */
@Mapper
public interface SmsConfigMapper extends BaseMapper<SmsConfigEntity> {
}