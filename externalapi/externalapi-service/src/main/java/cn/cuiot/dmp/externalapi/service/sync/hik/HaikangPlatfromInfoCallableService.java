package cn.cuiot.dmp.externalapi.service.sync.hik;

import cn.cuiot.dmp.base.infrastructure.dto.req.PlatfromInfoReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.PlatfromInfoRespDTO;
import cn.cuiot.dmp.common.enums.FootPlateInfoEnum;
import cn.cuiot.dmp.common.utils.ListPagingUtil;
import cn.cuiot.dmp.externalapi.service.service.park.PlatfromInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 海康企业配置信息服务
 *
 * @author: wuyongchong
 * @date: 2024/10/11 10:12
 */
@Slf4j
@Component
public class HaikangPlatfromInfoCallableService {

    private final static Integer DEFAULT_LIMIT = 10;

    @Autowired
    private PlatfromInfoService platfromInfoService;

    /**
     * 获取企业海康配置信息并处理
     */
    public void resolvePlatfromInfos(Integer limit, HaikangPlatfromInfoCallable callable) {
        if (Objects.isNull(limit)) {
            limit = DEFAULT_LIMIT;
        }
        if (limit < 1) {
            limit = DEFAULT_LIMIT;
        }
        AtomicLong pageNo = new AtomicLong(1);
        long pages = 0;
        do {
            PlatfromInfoReqDTO reqDTO = new PlatfromInfoReqDTO();
            reqDTO.setPageNo(pageNo.getAndAdd(1));
            reqDTO.setPageSize(200L);
            reqDTO.setPlatformId(FootPlateInfoEnum.HIK_ENTRANCE_GUARD.getId());
            IPage<PlatfromInfoRespDTO> pageResult = platfromInfoService.queryForPage(reqDTO);
            pages = pageResult.getPages();
            List<PlatfromInfoRespDTO> records = pageResult.getRecords();
            if (CollectionUtils.isNotEmpty(records)) {
                int iterIndex = 0;
                List<PlatfromInfoRespDTO> iterList = Lists.newArrayList();
                do {
                    iterList = ListPagingUtil
                            .pageBySubList(records, iterIndex + 1, limit);
                    iterIndex = iterIndex + 1;
                    if (CollectionUtils.isNotEmpty(iterList)) {
                        //每次处理limit个
                        callable.process(iterList);
                    }
                } while (iterList.size() > 0);
            }
        } while (pageNo.get() <= pages);
    }


}
