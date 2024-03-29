package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.common.constant.Constant;
import cn.cuiot.dmp.system.application.enums.LogTypeEnum;
import cn.cuiot.dmp.system.application.enums.SearchContentEnum;
import cn.cuiot.dmp.system.application.service.OperateLogQueryService;
import cn.cuiot.dmp.system.infrastructure.entity.bean.OperationLogBean;
import cn.cuiot.dmp.system.infrastructure.entity.dto.PlatformLogResDTO;
import java.sql.Timestamp;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @deprecated 操作日志查询接口-LogSearch
 * @Author 26432
 * @Date 2021/12/8   15:06
 */
@RestController
@RequestMapping("/log")
public class OperateLogController extends BaseController implements LogControllerImpl {

        @Autowired
        private OperateLogQueryService operateLogQueryService;

        /**
         * 所搜内容最大长度
         */
        private static final Integer MAX_LENGTH_OF_SEARCH_CONTENT = 5;

        /**
         * 查询平台日志
         * @param operationUserId
         * @param searchType
         * @param searchContent
         * @param serviceType
         * @param status
         * @param startTime
         * @param endTime
         * @param pageSize
         * @param pageNum
         * @param operationName
         * @return
         */
        @GetMapping(value = "/operation", produces = MediaType.APPLICATION_JSON_VALUE)
        public PlatformLogResDTO queryOperationLog(
                @RequestParam(value = "operationUserId", required = false) String operationUserId,
                @RequestParam(value = "searchType", required = false) String searchType,
                @RequestParam(value = "searchContent", required = false) String searchContent,
                @RequestParam(value = "serviceType", required = false, defaultValue = "1") Integer serviceType,
                @RequestParam(value = "status", required = false) Integer status,
                @RequestParam(value = "startTime") @NotNull @NotBlank Long startTime,
                @RequestParam(value = "endTime") @NotNull @NotBlank Long endTime,
                @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                @RequestParam(value = "pageNum", required = false, defaultValue = "0") Integer pageNum,
                @RequestParam(value = "operationName", required = false) String operationName) {
                String action = null;
                serviceType = Constant.SEARCH_ALL.equals(serviceType) ? null : serviceType;
                if (!StringUtils.isEmpty(searchContent)) {
                        regexSearchContent(searchContent.trim());
                        operationUserId = getOperationUserId(searchType, searchContent, SearchContentEnum.OPERATION_USER_ID.getType());
                        operationName = getOperationUserId(searchType, searchContent, SearchContentEnum.OPERATION_USER_ID.getType());
                        action = getAction(searchType, searchContent, SearchContentEnum.ACTION.getType());
                        searchContent = getContent(LogTypeEnum.OPERATION_LOG.getCode(), searchType, searchContent, MAX_LENGTH_OF_SEARCH_CONTENT, SearchContentEnum.CONTENT_KEYWORDS.getType());
                }
                String queryStatus = getQueryStatus(status, Constant.OPERATION_SUCCESS_CODE, Constant.OPERATION_FAILED_CODE);
                OperationLogBean operationLogBean = OperationLogBean.builder().operationUserId(operationUserId).action(action)
                        .serviceType(serviceType).status(queryStatus).startTime(new Timestamp(startTime))
                        .endTime(new Timestamp(endTime)).pageNum(pageNum).pageSize(pageSize).orgId(getOrgId())
                        .searchType(searchType).searchContent(searchContent).operationName(operationName).build();
                return operateLogQueryService.listLogs(operationLogBean);
        }

}
