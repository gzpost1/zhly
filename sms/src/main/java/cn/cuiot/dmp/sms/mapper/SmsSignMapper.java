package cn.cuiot.dmp.sms.mapper;

import cn.cuiot.dmp.sms.entity.SmsSignEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 短信签名信息 mapper接口
 *
 * @Author: zc
 * @Date: 2024-09-20
 */
public interface SmsSignMapper extends BaseMapper<SmsSignEntity> {

    /**
     * 根据企业id查询数据
     *
     * @return SmsSignEntity
     * @Param companyId 企业id
     */
    SmsSignEntity getByCompanyId(Long companyId);

    /**
     * 获取平台最早审核通过并且启用的数据
     *
     * @return SmsSignEntity
     * @Param orgTypeId 账户类型
     * @Param thirdStatus 审核状态
     * @Param status 启停用状态
     */
    SmsSignEntity queryPlatformFirst();

}