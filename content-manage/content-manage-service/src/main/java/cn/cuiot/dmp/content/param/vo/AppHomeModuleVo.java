package cn.cuiot.dmp.content.param.vo;//	模板

import cn.cuiot.dmp.content.dal.entity.ContentModule;
import cn.cuiot.dmp.content.dal.entity.ContentModuleApplication;
import cn.cuiot.dmp.content.dal.entity.ContentModuleBanner;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author hantingyao
 * @Description
 * @data 2024/6/7 14:10
 */

@Data
public class AppHomeModuleVo {

    private List<ContentModule> moduleList;

    /**
     * banner
     */
    private Map<Long, List<ContentModuleBanner>> bannerMap;

    /**
     * 应用
     */
    private Map<Long, List<ContentModuleApplication>> applicationMap;

}
