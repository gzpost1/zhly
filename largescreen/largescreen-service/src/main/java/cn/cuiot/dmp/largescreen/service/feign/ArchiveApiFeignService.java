package cn.cuiot.dmp.largescreen.service.feign;//	模板

import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.largescreen.service.dto.archive.BuildingArchivesPageQuery;
import cn.cuiot.dmp.largescreen.service.vo.ArchivesStatisticVO;
import cn.cuiot.dmp.largescreen.service.vo.BuildingArchivesVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

/**
 * @author xiaotao
 * @Description
 * @data 2024/6/19 14:12
 */
@Component
@FeignClient(value = "community-archive" , fallbackFactory = ArchiveApiFeignServiceFallback.class)
public interface ArchiveApiFeignService {

    /**
     * 根据条件查询楼栋信息
     */
    @PostMapping("/api/queryArchiveInfoList")
    IdmResDTO<List<BuildingArchivesVO>> queryArchiveInfoList(@RequestBody @Valid BuildingArchivesPageQuery pageQuery);


    /**
     * 查询基础档案统计
     * @param pageQuery 楼盘信息
     * @return ArchivesStatisticVO
     */
    @PostMapping("/api/queryArchiveBaseStatisticInfo")
    IdmResDTO<ArchivesStatisticVO> queryArchiveBaseStatisticInfo(@RequestBody @Valid BuildingArchivesPageQuery pageQuery);

}
