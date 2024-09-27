package cn.cuiot.dmp.sms.service;

import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.base.infrastructure.utils.RedisUtil;
import cn.cuiot.dmp.common.bean.PageQuery;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.enums.OrgTypeEnum;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.sms.contant.SmsRedisKeyConstant;
import cn.cuiot.dmp.sms.enums.SmsThirdStatusEnum;
import cn.cuiot.dmp.sms.mapper.SmsSignRelationMapper;
import cn.cuiot.dmp.sms.query.SmsSignCreateDto;
import cn.cuiot.dmp.sms.query.SmsSignListQuery;
import cn.cuiot.dmp.sms.vendor.SmsApiFeignService;
import cn.cuiot.dmp.sms.vendor.req.SmsBindSignReq;
import cn.cuiot.dmp.sms.vendor.resp.SmsBaseResp;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.cuiot.dmp.sms.mapper.SmsSignMapper;
import cn.cuiot.dmp.sms.entity.SmsSignEntity;

import java.util.List;
import java.util.Objects;

/**
 * 短信签名信息 业务层
 *
 * @Author: zc
 * @Date: 2024-09-20
 */
@Service
public class SmsSignService extends ServiceImpl<SmsSignMapper, SmsSignEntity> {

    @Autowired
    private SmsApiFeignService smsApiFeignService;
    @Autowired
    private SmsSignRelationMapper smsSignRelationMapper;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 分页
     *
     * @return IPage
     * @Param query 参数
     */
    public IPage<SmsSignEntity> queryForPage(PageQuery query) {
        // 获取企业id
        Long companyId = getCompanyId();

        LambdaQueryWrapper<SmsSignEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SmsSignEntity::getCompanyId, companyId);
        wrapper.orderByDesc(SmsSignEntity::getCreateTime);
        return page(new Page<>(query.getPageNo(), query.getPageSize()), wrapper);
    }

    /**
     * 下拉选择列表
     *
     * @return List
     * @Param query 参数
     */
    public List<SmsSignEntity> signSelectionList(SmsSignListQuery query) {
        LambdaQueryWrapper<SmsSignEntity> wrapper = new LambdaQueryWrapper<>();
        if (Objects.nonNull(query.getType())) {
            if (Objects.equals(EntityConstants.NO, query.getType())) {
                wrapper.eq(SmsSignEntity::getOrgTypeId, OrgTypeEnum.PLATFORM.getValue());
            } else {
                wrapper.eq(SmsSignEntity::getCompanyId, getCompanyId());
            }
        }
        wrapper.eq(SmsSignEntity::getStatus, EntityConstants.ENABLED);
        wrapper.eq(SmsSignEntity::getThirdStatus, SmsThirdStatusEnum.SUCCESS_AUDIT.getCode());
        wrapper.orderByDesc(SmsSignEntity::getCreateTime);

        return list(wrapper);
    }

    /**
     * 创建
     */
    public void create(SmsSignCreateDto dto) {
        // 获取企业id
        Long companyId = getCompanyId();
        // 账户类型
        Long orgTypeId = getOrgTypeId();

        // 数据校验
        checkData(dto, companyId, orgTypeId);

        // 请求第三方创建签名
        SmsBindSignReq req = new SmsBindSignReq();
        req.setSignName(dto.getSign());
        SmsBaseResp<Integer> resp = smsApiFeignService.bindSign(req);

        SmsSignEntity sign = new SmsSignEntity();
        BeanUtils.copyProperties(dto, sign);
        sign.setCompanyId(companyId);
        sign.setOrgTypeId(orgTypeId);
        sign.setThirdCode(resp.getData());
        sign.setThirdStatus(SmsThirdStatusEnum.UNAUDITED.getCode());
        sign.setStatus(EntityConstants.ENABLED);
        save(sign);
    }

    /**
     * 启停用
     *
     * @Param param 参数
     */
    public void updateStatus(UpdateStatusParam param) {
        // 账户类型
        Long orgTypeId = getOrgTypeId();
        // 获取企业id
        Long companyId = getCompanyId();

        LambdaQueryWrapper<SmsSignEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SmsSignEntity::getCompanyId, companyId);
        wrapper.eq(SmsSignEntity::getId, param.getId());
        List<SmsSignEntity> list = list(wrapper);

        if (CollectionUtils.isEmpty(list)) {
            throw new BusinessException(ResultCode.ERROR, "数据不存在");
        }

        //修改状态
        SmsSignEntity sign = list.get(0);
        sign.setStatus(param.getStatus());
        updateById(sign);

        //刷新缓存
        if (Objects.equals(param.getStatus(), EntityConstants.ENABLED)) {
            if (Objects.equals(sign.getThirdStatus(), SmsThirdStatusEnum.SUCCESS_AUDIT.getCode())) {
                //设置缓存
                setRedis(orgTypeId, companyId, sign);
            }
        } else {
            //删除缓存
            deleteRedis(orgTypeId, companyId);
        }
    }

    /**
     * 删除
     *
     * @Param id 签名id
     */
    public void delete(Long id) {
        // 账户类型
        Long orgTypeId = getOrgTypeId();
        // 获取企业id
        Long companyId = getCompanyId();

        //判断id是否属于当前企业id
        SmsSignEntity sign = getById(id);
        if (Objects.isNull(sign)) {
            throw new BusinessException(ResultCode.ERROR, "数据不存在");
        }
        if (!Objects.equals(sign.getCompanyId(), companyId)) {
            throw new BusinessException(ResultCode.ERROR, "当前企业不存在该数据");
        }

        Long aLong = smsSignRelationMapper.countBySignId(id);
        //如果账户类型是企业并且已有关联，则不可删除
        if (!Objects.equals(orgTypeId, OrgTypeEnum.PLATFORM.getValue())) {
            if (aLong > 0) {
                throw new BusinessException(ResultCode.ERROR, "该签名已被设置为发送签名，不可删除！请先在 ”发送签名设置“ 中修改发送签名后再删除");
            }
        }
        removeById(id);

        //删除缓存
        deleteRedis(orgTypeId, companyId);
    }

    /**
     * 获取缓存签名
     *
     * @return SmsSignEntity
     * @Param companyId 企业id
     */
    public SmsSignEntity getRedisSign(Long companyId, Long platformCompanyId) {
        // 尝试从缓存中获取公司签名
        String jsonStr = redisUtil.get(SmsRedisKeyConstant.SIGN_COMPANY + companyId);
        SmsSignEntity sign = StringUtils.isNotBlank(jsonStr) ? JsonUtil.readValue(jsonStr, SmsSignEntity.class) : null;

        // 如果缓存存在，或者公司ID是平台ID，直接返回
        if (Objects.nonNull(sign) || Objects.equals(companyId, platformCompanyId)) {
            return Objects.nonNull(sign) ? sign : getPlatformSign(companyId);
        }

        // 查询数据库中公司签名并验证有效性
        sign = smsSignRelationMapper.querySmsSignByCompanyId(companyId);
        if (Objects.nonNull(sign) && SmsThirdStatusEnum.SUCCESS_AUDIT.getCode().equals(sign.getThirdStatus()) &&
                EntityConstants.ENABLED.equals(sign.getStatus())) {
            redisUtil.set(SmsRedisKeyConstant.SIGN_PLATFORM + companyId, JsonUtil.writeValueAsString(sign));
        } else {
            sign = getPlatformSign(companyId);
        }

        return sign;
    }

    private SmsSignEntity getPlatformSign(Long companyId) {
        // 尝试从缓存中获取平台签名
        String jsonStr = redisUtil.get(SmsRedisKeyConstant.SIGN_PLATFORM);
        SmsSignEntity sign = StringUtils.isNotBlank(jsonStr) ? JsonUtil.readValue(jsonStr, SmsSignEntity.class) : null;

        // 如果缓存不存在，查询数据库并缓存
        if (Objects.isNull(sign)) {
            sign = baseMapper.queryPlatformFirst();
            if (Objects.nonNull(sign)) {
                redisUtil.set(SmsRedisKeyConstant.SIGN_PLATFORM + companyId, JsonUtil.writeValueAsString(sign));
            }
        }

        return sign;
    }


    /**
     * 删除缓存
     *
     * @Param orgTypeId 账户类型
     * @Param companyId 企业id
     */
    private void deleteRedis(Long orgTypeId, Long companyId) {
        // 修改模版内容redis缓存
        if (Objects.equals(orgTypeId, OrgTypeEnum.PLATFORM.getValue())) {
            SmsSignEntity smsSignEntity = baseMapper.queryPlatformFirst();
            redisUtil.del(SmsRedisKeyConstant.SIGN_PLATFORM);
            if (Objects.nonNull(smsSignEntity)) {
                redisUtil.set(SmsRedisKeyConstant.SIGN_PLATFORM, JsonUtil.writeValueAsString(smsSignEntity));
            }
        } else {
            redisUtil.del(SmsRedisKeyConstant.SIGN_COMPANY + companyId);
        }
    }

    /**
     * 设置缓存
     *
     * @Param orgTypeId 账户类型
     * @Param companyId 企业id
     * @Param entity 签名
     */
    private void setRedis(Long orgTypeId, Long companyId, SmsSignEntity entity) {
        // 修改模版内容redis缓存
        if (Objects.equals(orgTypeId, OrgTypeEnum.PLATFORM.getValue())) {
            SmsSignEntity smsSignEntity = baseMapper.queryPlatformFirst();
            redisUtil.del(SmsRedisKeyConstant.SIGN_PLATFORM);
            if (Objects.nonNull(smsSignEntity)) {
                redisUtil.set(SmsRedisKeyConstant.SIGN_PLATFORM, JsonUtil.writeValueAsString(smsSignEntity));
            }
        } else {
            redisUtil.set(SmsRedisKeyConstant.SIGN_COMPANY + companyId, JsonUtil.writeValueAsString(entity));
        }
    }

    /**
     * 数据校验
     *
     * @Param dto 参数
     * @Param companyId 企业id
     * @Param currentOrgTypeId 账户类型
     */
    private void checkData(SmsSignCreateDto dto, Long companyId, Long orgTypeId) {
        String sign = dto.getSign();

        //判断签名是否重复
        long signCount = count(new LambdaQueryWrapper<SmsSignEntity>().eq(SmsSignEntity::getSign, sign));
        if (signCount > 0) {
            throw new BusinessException(ResultCode.ERROR, "签名已存在");
        }

        //数量校验
        LambdaQueryWrapper<SmsSignEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SmsSignEntity::getOrgTypeId, orgTypeId);
        if (!Objects.equals(orgTypeId, OrgTypeEnum.PLATFORM.getValue())) {
            wrapper.eq(SmsSignEntity::getCompanyId, companyId);
        }
        long sum = count(wrapper);

        int total;
        String errorMessage;
        if (Objects.equals(orgTypeId, OrgTypeEnum.PLATFORM.getValue())) {
            total = 20;
            errorMessage = "最多可添加" + total + "个默认签名";
        } else {
            total = 5;
            errorMessage = "最多可添加" + total + "个企业签名";
        }

        if (sum >= total) {
            throw new BusinessException(ResultCode.ERROR, errorMessage);
        }
    }

    /**
     * 获取企业id
     */
    private Long getCompanyId() {
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        if (Objects.isNull(companyId)) {
            throw new BusinessException(ResultCode.ERROR, "所属企业不存在");
        }
        return companyId;
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
