package cn.cuiot.dmp.archive.application.service.impl;

import cn.cuiot.dmp.archive.application.constant.ArchivesApiConstant;
import cn.cuiot.dmp.archive.application.param.dto.ArchivesApiQueryDTO;
import cn.cuiot.dmp.archive.application.param.vo.BaseArchivesVO;
import cn.cuiot.dmp.archive.application.service.ArchivesApiService;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author caorui
 * @date 2024/5/23
 */
@Slf4j
@Service
public class ArchivesApiServiceImpl implements ArchivesApiService {

    @Override
    public BaseArchivesVO<Object> queryForDetail(ArchivesApiQueryDTO queryDTO) {
        BaseArchivesVO<Object> baseArchivesVO = new BaseArchivesVO<>();
        baseArchivesVO.setArchiveId(queryDTO.getArchiveId());
        baseArchivesVO.setArchiveType(queryDTO.getArchiveType());
        Object archivesDetail = queryForDetail(queryDTO.getArchiveType(), queryDTO.getArchiveId());
        if (Objects.nonNull(archivesDetail)) {
            baseArchivesVO.setArchivesDetail(archivesDetail);
        }
        return baseArchivesVO;
    }

    /**
     * 根据档案id和档案类型查询档案详情
     *
     * @param archiveType 档案类型
     * @param id          请求参数
     */
    private Object queryForDetail(Byte archiveType, Long id) {
        Object bean = SpringContextUtil.getBean(ArchivesApiConstant.getArchivesName(archiveType));
        AssertUtil.isFalse(Objects.isNull(bean), "档案类型错误，archiveType:" + archiveType);
        Method method = ReflectionUtils.findMethod(bean.getClass(), "queryForDetail", id.getClass());
        AssertUtil.isFalse(Objects.isNull(method), "该方法不存在");
        return ReflectionUtils.invokeMethod(method, bean, id);
    }

}
