package cn.cuiot.dmp.sms.mapper;

import cn.cuiot.dmp.sms.entity.SmsTemplateEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 短信模版信息 mapper接口
 *
 * @Author: zc
 * @Date: 2024-09-20
 */
@Mapper
public interface SmsTemplateMapper extends BaseMapper<SmsTemplateEntity> {

    /**
     * 根据标准模板id查询
     *
     * @return SmsTemplateEntity
     * @Param stdTemplate 标准模板id
     */
    SmsTemplateEntity queryByStdTemplate(Integer stdTemplate);
}