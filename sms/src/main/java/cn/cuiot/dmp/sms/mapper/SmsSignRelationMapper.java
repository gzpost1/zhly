package cn.cuiot.dmp.sms.mapper;

import cn.cuiot.dmp.sms.entity.SmsSignEntity;
import cn.cuiot.dmp.sms.entity.SmsSignRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 短信-发送签名设置 mapper接口
 *
 * @Author: zc
 * @Date: 2024-09-20
 */
@Mapper
public interface SmsSignRelationMapper extends BaseMapper<SmsSignRelationEntity> {

    /**
     * 根据企业id查询企业管理的签名
     *
     * @return SmsSignEntity
     * @Param companyId 企业id
     */
    SmsSignEntity querySmsSignByCompanyId(Long companyId);

    /**
     * 根据id查询汇总
     *
     * @return Long
     * @Param companyId 企业id
     */
    Long countBySignId(Long signId);

}