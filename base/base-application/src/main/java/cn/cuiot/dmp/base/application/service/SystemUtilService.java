package cn.cuiot.dmp.base.application.service;

import cn.cuiot.dmp.base.application.dto.BusinessAndOrgDto;
import cn.cuiot.dmp.base.application.dto.BusinessAndOrgNameDto;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.BusinessTypeReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.req.DepartmentReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.BusinessTypeRspDTO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Description 系统工具服务
 * @Date 2024/5/9 15:39
 * @Created by libo
 */
@Service
public class SystemUtilService {
    @Autowired
    private ApiSystemService apiSystemService;


    /**
     * 处理业务类型和组织机构名称
     *
     * @param businessAndOrgDtoList
     * @return
     */
    public Map<Long, BusinessAndOrgNameDto> processBusinessAndOrgNameDto(List<BusinessAndOrgDto> businessAndOrgDtoList) {
        // 获取业务类型列表
        List<Long> businessTypeIds = businessAndOrgDtoList.stream()
                .flatMap(businessAndOrgDto -> businessAndOrgDto.getBusinessTypeIdList().stream()).collect(Collectors.toList());
        List<BusinessTypeRspDTO> businessTypeList = getBusinessTypeList(businessTypeIds, businessAndOrgDtoList.get(0).getOrgIds().get(0));

        List<DepartmentDto> deptNameList = getDeptNameList(businessAndOrgDtoList);

        //填充每一行的业务名称和组织机构名称
        Map<Long, BusinessAndOrgNameDto> collect = businessAndOrgDtoList.stream().map(e -> {
            BusinessAndOrgNameDto businessAndOrgNameDto = new BusinessAndOrgNameDto();
            businessAndOrgNameDto.setDataId(e.getDataId());
            if (CollectionUtils.isNotEmpty(businessTypeList)) {
                String businessName = businessTypeList.stream().filter(businessTypeRspDTO -> e.getBusinessTypeIdList().contains(businessTypeRspDTO.getBusinessTypeId()))
                        .map(BusinessTypeRspDTO::getTreeName).collect(Collectors.joining(","));
                businessAndOrgNameDto.setBusinessTypeName(businessName);
            }

            //设置组织机构名称
            if (CollectionUtils.isNotEmpty(deptNameList)) {
                String orgName = e.getOrgIds().stream().map(orgId -> deptNameList.stream().filter(departmentDto -> departmentDto.getId().equals(orgId))
                        .map(DepartmentDto::getName).findFirst().get()).collect(Collectors.joining(","));

                businessAndOrgNameDto.setOrgName(orgName);
            }
            return businessAndOrgNameDto;
        }).collect(Collectors.toMap(BusinessAndOrgNameDto::getDataId, Function.identity()));

        return collect;
    }

    /**
     * 获取组织机构名称
     * @param businessAndOrgDtoList
     * @return
     */
    private List<DepartmentDto> getDeptNameList(List<BusinessAndOrgDto> businessAndOrgDtoList) {
        List<Long> deptIds = businessAndOrgDtoList.stream()
                .flatMap(businessAndOrgDto -> businessAndOrgDto.getOrgIds().stream()).collect(Collectors.toList());
        DepartmentReqDto departmentReqDto = new DepartmentReqDto();
        departmentReqDto.setDeptIdList(deptIds);
        List<DepartmentDto> departmentDtos = apiSystemService.lookUpDepartmentList(departmentReqDto);
        return departmentDtos;
    }

    /**
     * 获取业务类型列表
     *
     * @param businessTypeIdList
     * @return
     */
    public List<BusinessTypeRspDTO> getBusinessTypeList(List<Long> businessTypeIdList, Long orgId) {
        BusinessTypeReqDTO businessTypeReqDTO = new BusinessTypeReqDTO();
        businessTypeReqDTO.setBusinessTypeIdList(businessTypeIdList);
        businessTypeReqDTO.setOrgId(orgId);
        return apiSystemService.batchGetBusinessType(businessTypeReqDTO);
    }

}
