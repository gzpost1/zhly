package cn.cuiot.dmp.sms.service;

import cn.cuiot.dmp.base.infrastructure.utils.RedisUtil;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.enums.OrgTypeEnum;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.sms.contant.SmsRedisKeyConstant;
import cn.cuiot.dmp.sms.enums.SmsThirdStatusEnum;
import cn.cuiot.dmp.sms.query.SmsTemplateCreateDto;
import cn.cuiot.dmp.sms.query.SmsTemplatePageQuery;
import cn.cuiot.dmp.sms.vendor.SmsApiFeignService;
import cn.cuiot.dmp.sms.vendor.req.SmsBindTemplateReq;
import cn.cuiot.dmp.sms.vendor.resp.SmsBaseResp;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.cuiot.dmp.sms.mapper.SmsTemplateMapper;
import cn.cuiot.dmp.sms.entity.SmsTemplateEntity;

import java.util.Objects;
import java.util.Optional;

/**
 * 短信模版信息 业务层
 *
 * @Author: zc
 * @Date: 2024-09-20
 */
@Slf4j
@Service
public class SmsTemplateService extends ServiceImpl<SmsTemplateMapper, SmsTemplateEntity> {

    @Autowired
    private SmsApiFeignService smsApiFeignService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 分页
     *
     * @return IPage<SmsTemplateEntity>
     * @Param query 参数
     */
    public IPage<SmsTemplateEntity> queryForPage(SmsTemplatePageQuery query) {

        //获取当前账户类型
        Long orgTypeId = getOrgTypeId();
        if (!Objects.equals(orgTypeId, OrgTypeEnum.PLATFORM.getValue())) {
            return new Page<>();
        }

        LambdaQueryWrapper<SmsTemplateEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Objects.nonNull(query.getStdTemplate()), SmsTemplateEntity::getStdTemplate, query.getStdTemplate());
        wrapper.eq(Objects.nonNull(query.getThirdTemplate()), SmsTemplateEntity::getThirdTemplate, query.getThirdTemplate());
        wrapper.eq(Objects.nonNull(query.getThirdStatus()), SmsTemplateEntity::getThirdStatus, query.getThirdStatus());
        wrapper.eq(Objects.nonNull(query.getSmsType()), SmsTemplateEntity::getSmsType, query.getSmsType());
        wrapper.orderByDesc(SmsTemplateEntity::getCreateTime);

        return page(new Page<>(query.getPageNo(), query.getPageSize()), wrapper);
    }

    /**
     * 创建
     *
     * @Param dto 参数
     */
    public void create(SmsTemplateCreateDto dto) {
        //获取当前账户类型
        Long orgTypeId = getOrgTypeId();
        if (!Objects.equals(orgTypeId, OrgTypeEnum.PLATFORM.getValue())) {
            throw new BusinessException(ResultCode.ERROR, "该页面权限仅对平台开开放");
        }

        //最多添加模板数
        int maxCount = 500;

        long count = count();
        if (count >= maxCount) {
            throw new BusinessException(ResultCode.ERROR, "最多可添加500个短信模板");
        }

        //请求创建模板
        SmsBindTemplateReq req = new SmsBindTemplateReq();
        req.setTemplateContent(dto.getContent());
        SmsBaseResp<Integer> resp = smsApiFeignService.bindTemplate(req);

        // 保存数据库
        SmsTemplateEntity template = new SmsTemplateEntity();
        BeanUtils.copyProperties(dto, template);
        template.setStdTemplate(resp.getData());
        template.setThirdStatus(SmsThirdStatusEnum.UNAUDITED.getCode());
        template.setThirdTemplate(resp.getData());
        save(template);

        // 修改模版内容redis缓存
        redisUtil.set(SmsRedisKeyConstant.TEMPLATE + template.getStdTemplate(), JsonUtil.writeValueAsString(template));
    }

    /**
     * 删除
     *
     * @Param id 数据id
     */
    public void delete(Long id) {
        // 获取当前账户类型
        Long orgTypeId = getOrgTypeId();
        if (!Objects.equals(orgTypeId, OrgTypeEnum.PLATFORM.getValue())) {
            throw new BusinessException(ResultCode.ERROR, "该页面权限仅对平台开开放");
        }

        SmsTemplateEntity byId = Optional.ofNullable(getById(id))
                .orElseThrow(() -> new BusinessException(ResultCode.ERROR, "数据不存在"));

        // 删除数据
        removeById(id);

        // 修改模版内容redis缓存
        redisUtil.del(SmsRedisKeyConstant.TEMPLATE + byId.getStdTemplate());
    }

    /**
     * 根据标准模板id获取缓存数据
     *
     * @return SmsTemplateEntity
     * @Param stdTemplate 标准模板id
     */
    public SmsTemplateEntity getRedisTemplate(Integer stdTemplate) {
        String jsonStr = redisUtil.get(SmsRedisKeyConstant.TEMPLATE + stdTemplate);
        if (StringUtils.isNotBlank(jsonStr)) {
            return JsonUtil.readValue(jsonStr, SmsTemplateEntity.class);
        }
        // 如果为空则查询数据库
        SmsTemplateEntity template = baseMapper.queryByStdTemplate(stdTemplate);
        //如果数据存在并且审核通过则返回
        if (Objects.nonNull(template) && Objects.equals(template.getThirdStatus(), SmsThirdStatusEnum.SUCCESS_AUDIT.getCode())) {
            redisUtil.set(SmsRedisKeyConstant.TEMPLATE + stdTemplate, JsonUtil.writeValueAsString(template));
            return template;
        }
        return null;
    }

    /**
     * 获取账户类型
     */
    private Long getOrgTypeId() {
        Integer currentOrgTypeId = LoginInfoHolder.getCurrentOrgTypeId();
        if (Objects.isNull(currentOrgTypeId)) {
            throw new BusinessException(ResultCode.ERROR, "所属企业不存在");
        }
        return (long) currentOrgTypeId;
    }
}
