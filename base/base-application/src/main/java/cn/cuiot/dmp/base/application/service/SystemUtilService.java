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
import org.apache.commons.lang3.StringUtils;
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
    public <T extends PageInfoBaseDto> void fillOrgNameAndBusinessName(IPage<T> page) {
        //填充每一行的业务名称和组织机构名称
        if (Objects.nonNull(page) && CollectionUtils.isNotEmpty(page.getRecords())) {
            //每一行转化为 BusinessAndOrgDto
            List<BusinessAndOrgDto> businessAndOrgDtoList = page.getRecords().stream().map(e -> {
                BusinessAndOrgDto businessAndOrgDto = new BusinessAndOrgDto();
                businessAndOrgDto.setDataId(e.getId());
                businessAndOrgDto.setOrgIds(e.getOrgIds());
                businessAndOrgDto.setCompanyId(e.getCompanyId());
                businessAndOrgDto.setBusinessTypeIdList(Lists.newArrayList(e.getBusinessTypeId()));
                businessAndOrgDto.setCreateUserId(e.getCreateUser());
                return businessAndOrgDto;
            }).collect(Collectors.toList());

            //获取业务类型和组织机构名称
            Map<Long, BusinessAndOrgNameDto> nameDtoMap = this.processBusinessAndOrgNameDto(businessAndOrgDtoList);

            //填充每一行的业务名称和组织机构名称
            page.getRecords().forEach(e -> {
                BusinessAndOrgNameDto businessAndOrgNameDto = nameDtoMap.get(e.getId());
                if (Objects.nonNull(businessAndOrgNameDto)) {
                    if(StringUtils.isNotEmpty(businessAndOrgNameDto.getBusinessTypeName())){
                        e.setBusinessTypeName(StringUtils.substringAfter(businessAndOrgNameDto.getBusinessTypeName(), ">"));
                    }

                    if(StringUtils.isNotEmpty(businessAndOrgNameDto.getOrgName())){
                        e.setOrgName(StringUtils.substringAfter(businessAndOrgNameDto.getOrgName(), ">"));
                    }

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
        // 获取业务类型名称列表
        List<BusinessTypeRspDTO> businessTypeList = getBusinessTypeList(businessAndOrgDtoList);

        //查询组织机构名称
        List<DepartmentDto> deptNameList = getDeptNameList(businessAndOrgDtoList);
        //获取用户名称
        Map<Long, String> userNameList = getUserNameList(businessAndOrgDtoList);

        //填充每一行的业务名称和组织机构名称
        Map<Long, BusinessAndOrgNameDto> collect = businessAndOrgDtoList.stream().map(e -> {
            BusinessAndOrgNameDto businessAndOrgNameDto = new BusinessAndOrgNameDto();
            businessAndOrgNameDto.setDataId(e.getDataId());
            if (CollectionUtils.isNotEmpty(businessTypeList) && CollectionUtils.isNotEmpty(e.getBusinessTypeIdList())) {
                String businessName = businessTypeList.stream().filter(businessTypeRspDTO -> e.getBusinessTypeIdList().contains(businessTypeRspDTO.getBusinessTypeId()))
                        .map(BusinessTypeRspDTO::getTreeName).collect(Collectors.joining(","));
                businessAndOrgNameDto.setBusinessTypeName(businessName);
            }

            //设置组织机构名称
            if (CollectionUtils.isNotEmpty(deptNameList) && CollectionUtils.isNotEmpty(e.getOrgIds())) {
                String orgName = deptNameList.stream().filter(businessTypeRspDTO -> e.getOrgIds().contains(businessTypeRspDTO.getId()))
                        .map(DepartmentDto::getPathName).collect(Collectors.joining(","));

                businessAndOrgNameDto.setOrgName(orgName);
            }

            //设置创建用户名称
            if (Objects.nonNull(userNameList) && Objects.nonNull(e.getCreateUserId())) {
                businessAndOrgNameDto.setCreateUserName(userNameList.get(e.getCreateUserId()));
            }

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
        Map<Long, String> userNameMap = new HashMap<>();

        List<Long> userIds = businessAndOrgDtoList.stream().filter(e -> Objects.nonNull(e.getCreateUserId()))
                .map(BusinessAndOrgDto::getCreateUserId).distinct().collect(Collectors.toList());

        //获取用户名称
        if (CollectionUtils.isNotEmpty(userIds)) {
            BaseUserReqDto baseUserReqDto = new BaseUserReqDto();
            baseUserReqDto.setUserIdList(userIds);
            log.info("获取用户名称参数：{}", baseUserReqDto);
            List<BaseUserDto> baseUserDtos = apiSystemService.lookUpUserList(baseUserReqDto);

            if (CollectionUtils.isNotEmpty(baseUserDtos)) {
                userNameMap = baseUserDtos.stream().collect(Collectors.toMap(BaseUserDto::getId, BaseUserDto::getName));
            }
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
        List<DepartmentDto> departmentDtos = new ArrayList<>();

        List<Long> deptIds = businessAndOrgDtoList.stream().filter(e -> CollectionUtils.isNotEmpty(e.getOrgIds()))
                .flatMap(businessAndOrgDto -> businessAndOrgDto.getOrgIds().stream()).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(deptIds)) {
            DepartmentReqDto departmentReqDto = new DepartmentReqDto();
            departmentReqDto.setDeptIdList(deptIds);

            log.info("获取组织机构名称参数：{}", departmentReqDto);
            departmentDtos = apiSystemService.lookUpDepartmentList(departmentReqDto);
        }

        return departmentDtos;
    }

    /**
     * 获取业务类型列表
     *
     * @param businessAndOrgDtoList
     * @return
     */
    public List<BusinessTypeRspDTO> getBusinessTypeList(List<BusinessAndOrgDto> businessAndOrgDtoList) {
        List<BusinessTypeRspDTO> businessTypeRspDTOS = new ArrayList<>();

        List<Long> businessTypeIds = businessAndOrgDtoList.stream().filter(e -> CollectionUtils.isNotEmpty(e.getBusinessTypeIdList()))
                .flatMap(businessAndOrgDto -> businessAndOrgDto.getBusinessTypeIdList().stream()).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(businessTypeIds)) {
            BusinessTypeReqDTO businessTypeReqDTO = new BusinessTypeReqDTO();
            businessTypeReqDTO.setBusinessTypeIdList(businessTypeIds);
            log.info("获取业务类型列表参数：{}", businessTypeReqDTO);
            businessTypeRspDTOS = apiSystemService.batchGetBusinessType(businessTypeReqDTO);
        }
        return businessTypeRspDTOS;
    }

}
