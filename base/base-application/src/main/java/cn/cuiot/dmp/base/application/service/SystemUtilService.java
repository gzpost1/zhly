package cn.cuiot.dmp.base.application.service;

import cn.cuiot.dmp.base.application.dto.BusinessAndOrgDto;
import cn.cuiot.dmp.base.application.dto.BusinessAndOrgNameDto;
import cn.cuiot.dmp.base.infrastructure.dto.BaseUserDto;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.dto.PageInfoBaseDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.BaseUserReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.BusinessTypeReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.req.DepartmentReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.BusinessTypeRspDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Description 系统工具服务
 * @Date 2024/5/9 15:39
 * @Created by libo
 */
@Slf4j
@Service
public class SystemUtilService {
    @Autowired
    private ApiSystemService apiSystemService;

    /**
     * 填充每一行的业务名称和组织机构名称
     *
     * @param page
     */
    public  <T extends PageInfoBaseDto> void fillOrgNameAndBusinessName(IPage<T> page) {
        //填充每一行的业务名称和组织机构名称
        if (Objects.nonNull(page) && CollectionUtils.isNotEmpty(page.getRecords())) {
            //每一行转化为 BusinessAndOrgDto
            List<BusinessAndOrgDto> businessAndOrgDtoList = page.getRecords().stream().map(e -> {
                BusinessAndOrgDto businessAndOrgDto = new BusinessAndOrgDto();
                businessAndOrgDto.setDataId(e.getId());
                businessAndOrgDto.setOrgIds(e.getOrgIds());
                businessAndOrgDto.setCompanyId(e.getCompanyId());
                businessAndOrgDto.setBusinessTypeIdList(Lists.newArrayList(e.getBusinessTypeId()));
                return businessAndOrgDto;
            }).collect(Collectors.toList());

            //获取业务类型和组织机构名称
            Map<Long, BusinessAndOrgNameDto> nameDtoMap = this.processBusinessAndOrgNameDto(businessAndOrgDtoList);

            //填充每一行的业务名称和组织机构名称
            page.getRecords().forEach(e -> {
                BusinessAndOrgNameDto businessAndOrgNameDto = nameDtoMap.get(e.getId());
                if (Objects.nonNull(businessAndOrgNameDto)) {
                    e.setBusinessTypeName(businessAndOrgNameDto.getBusinessTypeName());
                    e.setOrgName(businessAndOrgNameDto.getOrgName());
                    e.setCreateUserName(businessAndOrgNameDto.getCreateUserName());
                }
            });
        }
    }


    /**
     * 处理业务类型和组织机构名称
     *
     * @param businessAndOrgDtoList
     * @return
     */
    public Map<Long, BusinessAndOrgNameDto> processBusinessAndOrgNameDto(List<BusinessAndOrgDto> businessAndOrgDtoList) {
        //获取业务类型名称
        Map<Long, List<BusinessAndOrgDto>> companyBusinessMap = businessAndOrgDtoList.stream().collect(Collectors.groupingBy(BusinessAndOrgDto::getCompanyId));
        List<BusinessTypeRspDTO> businessTypeList = new ArrayList<>();
        companyBusinessMap.forEach((k, v) -> {
            // 获取业务类型列表
            List<Long> businessTypeIds = v.stream()
                    .flatMap(businessAndOrgDto -> businessAndOrgDto.getBusinessTypeIdList().stream()).collect(Collectors.toList());
            List<BusinessTypeRspDTO> businessResultList = getBusinessTypeList(businessTypeIds, k);
            businessTypeList.addAll(businessResultList);
        });

        //查询组织机构名称
        List<DepartmentDto> deptNameList = getDeptNameList(businessAndOrgDtoList);
        //获取用户名称
        Map<Long, String> userNameList = getUserNameList(businessAndOrgDtoList);

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

            //设置创建用户名称
            businessAndOrgNameDto.setCreateUserName(userNameList.get(e.getCreateUserId()));

            return businessAndOrgNameDto;
        }).collect(Collectors.toMap(BusinessAndOrgNameDto::getDataId, Function.identity()));

        return collect;
    }

    /**
     * 获取每一行的创建用户名称
     *
     * @param businessAndOrgDtoList
     */
    private Map<Long, String> getUserNameList(List<BusinessAndOrgDto> businessAndOrgDtoList) {
        List<Long> userIds = businessAndOrgDtoList.stream().map(BusinessAndOrgDto::getCreateUserId).distinct().collect(Collectors.toList());

        //获取用户名称
        BaseUserReqDto baseUserReqDto = new BaseUserReqDto();
        baseUserReqDto.setUserIdList(userIds);
        log.info("获取用户名称参数：{}", baseUserReqDto);
        List<BaseUserDto> baseUserDtos = apiSystemService.lookUpUserList(baseUserReqDto);

        Map<Long, String> userNameMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(baseUserDtos)) {
            userNameMap = baseUserDtos.stream().collect(Collectors.toMap(BaseUserDto::getId, BaseUserDto::getName));
        }

        return userNameMap;
    }

    /**
     * 获取组织机构名称
     *
     * @param businessAndOrgDtoList
     * @return
     */
    private List<DepartmentDto> getDeptNameList(List<BusinessAndOrgDto> businessAndOrgDtoList) {
        List<Long> deptIds = businessAndOrgDtoList.stream()
                .flatMap(businessAndOrgDto -> businessAndOrgDto.getOrgIds().stream()).collect(Collectors.toList());
        DepartmentReqDto departmentReqDto = new DepartmentReqDto();
        departmentReqDto.setDeptIdList(deptIds);

        log.info("获取组织机构名称参数：{}", departmentReqDto);
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
        log.info("获取业务类型列表参数：{}", businessTypeReqDTO);
        return apiSystemService.batchGetBusinessType(businessTypeReqDTO);
    }

}
