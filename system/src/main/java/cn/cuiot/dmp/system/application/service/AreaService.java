package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.system.infrastructure.entity.AreaEntity;
import cn.cuiot.dmp.system.infrastructure.entity.dto.AreaDto;
import cn.cuiot.dmp.system.infrastructure.entity.vo.SelectCascadeVO;
import java.util.List;

/**
 * @author shixh
 * @classname AreaService
 * @description
 * @date 2022/11/24
 */
public interface AreaService {

    /**
     * 查询行政区域列表
     * @param code String
     * @return
     * @Author wen
     * @Description 保存
     * @Date 2021/8/18 17:06
     **/
    List<AreaDto> listSon(String code);

    /**
     * 级联下拉框通用对象列表查询
     * @param code String
     * @return
     * @Author wen
     * @Description 保存
     * @Date 2021/8/18 17:06
     **/
    List<SelectCascadeVO> listSonCascade(String code);

    /**
     * 通过code查询行政区域名称
     * @param areaCodes
     * @return
     */
    String getAreaNamesByCodes(String areaCodes);

    /**
     * 查询缓存中的行政区域
     * @param areaCode
     * @return
     */
    AreaEntity getAreaByCodeRedis(String areaCode);


    /**
     * 级联下拉框通用对象列表查询
     * @param code String
     * @return
     * @Author wen
     * @Description 保存
     * @Date 2021/8/18 17:06
     **/
    List<AreaDto> listSonCascadeTwo(String code);

    /**
     * 验证省份code是否正确
     * @param provinceCode
     * @return
     */
    Integer checkProvinceCode(String provinceCode);

    /**
     * 获取所有一级省份信息
     */
    List<AreaDto> getAllOneProvince();
}
