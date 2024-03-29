package cn.cuiot.dmp.system.application.service.impl;

import cn.cuiot.dmp.common.constant.CacheConst;
import cn.cuiot.dmp.system.application.param.assembler.AreaConverter;
import cn.cuiot.dmp.system.application.service.AreaService;
import cn.cuiot.dmp.system.infrastructure.entity.AreaEntity;
import cn.cuiot.dmp.system.infrastructure.entity.dto.AreaDto;
import cn.cuiot.dmp.system.infrastructure.entity.vo.SelectCascadeVO;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.AreaDao;
import cn.cuiot.dmp.base.infrastructure.utils.RedisUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.util.StringUtil;
import com.google.common.collect.Lists;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * @author wensq
 * @classname AreaServiceImpl
 * @description
 * @date 2022/01/06
 */
@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    private AreaDao areaDao;

    /**
     * 自动注入stringRedisTemplate
     */
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private AreaConverter areaConverter;

    private static final String TO_DELETE_NODES = "市辖区";
    private static final Integer TO_DELETE_NODES_LEVEL = 3;

    /**
     * 组织对应的area缓存中保存时间
     */
    @Value("${community.area.cache.timeout:3600}")
    private Long timeout;

    @Override
    public List<AreaDto> listSon(String code) {
        List<AreaDto> areaDtos = Lists.newArrayList();
        List<AreaEntity> areaEntities = areaDao.selectByParentCode(code);
        if (!CollectionUtils.isEmpty(areaEntities)) {
            areaEntities.stream().forEach(item -> {
                if (TO_DELETE_NODES.equals(item.getName()) && TO_DELETE_NODES_LEVEL.equals(item.getLevel())) {
                    // 省份下面多了个“市辖区”，没有子节点了，所以需要删掉。 直辖市下面的“市辖区”下面是有子节点的。
                } else {
                    AreaDto areaDto = new AreaDto();
                    BeanUtils.copyProperties(item, areaDto);
                    areaDtos.add(areaDto);
                }
            });
        }
        return areaDtos;
    }

    @Override
    public String getAreaNamesByCodes(String areaCodes) {
        StringBuilder areaNames = new StringBuilder();
        String[] areaCodesArr = areaCodes.split(",");
        for (int i = 0; i < areaCodesArr.length; i++) {
            AreaEntity areaEntity = getAreaByCodeRedis(areaCodesArr[i].trim());
            areaNames.append(areaEntity.getName()).append(",");
        }
        return areaNames.substring(0, areaNames.length() - 1);
    }

    @Override
    public AreaEntity getAreaByCodeRedis(String areaCode) {
        String areaEntityStr = redisUtil.get(CacheConst.AREA_CODE_CONFIG + areaCode);
        if (StringUtil.isNotEmpty(areaEntityStr)) {
            return JSONObject.parseObject(areaEntityStr, AreaEntity.class);
        } else {
            AreaEntity areaEntity = areaDao.selectByCode(areaCode);
            redisUtil.set(CacheConst.AREA_CODE_CONFIG + areaCode, JSONObject.toJSONString(areaEntity), timeout);
            return areaEntity;
        }
    }




    @Override
    public List<SelectCascadeVO> listSonCascade(String code) {
        List<SelectCascadeVO> cascadeList = Lists.newArrayList();
        List<AreaEntity> areaEntities = areaDao.selectByParentCode(code);
        if (!CollectionUtils.isEmpty(areaEntities)) {
            areaEntities.stream().forEach(item -> {
                if (TO_DELETE_NODES.equals(item.getName()) && TO_DELETE_NODES_LEVEL.equals(item.getLevel())) {
                    // 省份下面多了个“市辖区”，没有子节点了，所以需要删掉。 直辖市下面的“市辖区”下面是有子节点的。
                } else {
                    SelectCascadeVO vo = new SelectCascadeVO();
                    vo.setKey(item.getCode());
                    vo.setValue(item.getName());
                    vo.setLevel(item.getLevel());
                    List<SelectCascadeVO> child = this.listSonCascade(item.getCode());
                    if (!CollectionUtils.isEmpty(child)) {
                        vo.setChildren(child);
                    }
                    cascadeList.add(vo);
                }
            });
        }
        return cascadeList;
    }

    @Override
    public List<AreaDto> listSonCascadeTwo(String code) {
        List<AreaDto> areaDtos = Lists.newArrayList();
        List<AreaEntity> areaEntities = areaDao.selectByParentCode(code);
        if (!CollectionUtils.isEmpty(areaEntities)) {
            areaEntities.stream().forEach(item -> {
                if (TO_DELETE_NODES.equals(item.getName())) {
                    // 省份下面多了个“市辖区”，没有子节点了，所以需要删掉。 直辖市下面的“市辖区”下面是有子节点的。
                } else {
                    AreaDto areaDto = new AreaDto();
                    BeanUtils.copyProperties(item, areaDto);
                    areaDtos.add(areaDto);
                }
            });
        }
        return areaDtos;
    }

    @Override
    public Integer checkProvinceCode(String provinceCode) {
        return areaDao.checkProvinceCode(provinceCode);
    }


    @Override
    public List<AreaDto> getAllOneProvince() {
        List<AreaEntity> areaEntityList = areaDao.getAllOneProvince();
        return  areaConverter.entityListToDtoList(areaEntityList);
    }


}
