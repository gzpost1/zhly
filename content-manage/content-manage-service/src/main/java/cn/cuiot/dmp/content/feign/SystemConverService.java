package cn.cuiot.dmp.content.feign;//	模板

import cn.cuiot.dmp.base.infrastructure.dto.BaseUserDto;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.BaseUserReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.DepartmentReqDto;
import cn.cuiot.dmp.base.infrastructure.feign.SystemApiFeignService;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.hutool.core.collection.CollUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/30 17:18
 */
@Service
public class SystemConverService {

    @Autowired
    private SystemApiFeignService systemApiFeignService;

    public HashMap<Long, DepartmentDto> getDepartmentMapByIds(List<Long> departmentIds) {
        if (CollUtil.isEmpty(departmentIds)) {
            return new HashMap<>();
        }
        IdmResDTO<List<DepartmentDto>> listIdmResDTO = systemApiFeignService.lookUpDepartmentList(new DepartmentReqDto().setDeptIdList(departmentIds));
        List<DepartmentDto> data = listIdmResDTO.getData();
        if (CollUtil.isEmpty(data)) {
            return new HashMap<>();
        }
        return data.stream().collect(Collectors.toMap(DepartmentDto::getId, departmentDto -> departmentDto, (k1, k2) -> k1, HashMap::new));
    }

    public List<Long> getDeptIds(DepartmentReqDto query) {
        IdmResDTO<List<DepartmentDto>> listIdmResDTO = systemApiFeignService.lookUpDepartmentChildList2(query);
        AtomicReference<List<Long>> deptIds = new AtomicReference<>(new ArrayList<>());
        Optional.ofNullable(listIdmResDTO.getData()).ifPresent(departmentDtos -> {
            deptIds.set(departmentDtos.stream().map(DepartmentDto::getId).distinct().collect(Collectors.toList()));
        });
        return deptIds.get();
    }

    public HashMap<Long, BaseUserDto> getUserMapByIds(List<Long> creatUserIds) {
        IdmResDTO<List<BaseUserDto>> listIdmResDTO = systemApiFeignService.lookUpUserList(new BaseUserReqDto().setUserIdList(creatUserIds));
        List<BaseUserDto> data = listIdmResDTO.getData();
        if (CollUtil.isEmpty(data)) {
            return new HashMap<>();
        }
        return data.stream().collect(Collectors.toMap(BaseUserDto::getId, baseUserDto -> baseUserDto, (k1, k2) -> k1, HashMap::new));
    }

    public List<BaseUserDto> lookUpUserList(BaseUserReqDto reqDto) {
        return systemApiFeignService.lookUpUserList(reqDto).getData();
    }

    public List<Long> lookUpUserIds(BaseUserReqDto reqDto) {
        List<BaseUserDto> userDtoList = systemApiFeignService.lookUpUserList(reqDto).getData();
        if (CollUtil.isNotEmpty(userDtoList)) {
            return userDtoList.stream().map(BaseUserDto::getId).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
