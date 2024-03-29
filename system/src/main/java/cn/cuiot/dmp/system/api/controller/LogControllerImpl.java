package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.common.constant.RegexConst;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.enums.StatusCodeEnum;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.system.application.enums.LogTypeEnum;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author guoying
 * @className LogControllerImpl
 * @description 日志查询控制代码
 * @date 2020-10-23 10:00:07
 */
interface LogControllerImpl {
    /**
     * 正则表达式
     *
     * @param searchContent
     * @return
     */
    default boolean regex(String searchContent) {
        Pattern pattern = Pattern.compile(RegexConst.LOG_REGEX);
        Matcher matcher = pattern.matcher(searchContent);
        //包含A B这总类型
        return matcher.matches();
    }

    /**
     * 对searchContent进行正则表达式匹配
     *
     * @param searchContent
     */
    default void regexSearchContent(String searchContent) {
        if (!regex(searchContent)) {
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT);
        }
    }


    /**
     * 获取OperationUserId
     *
     * @param searchType
     * @param searchContent
     * @param type
     * @return
     */
    default String getOperationUserId(String searchType, String searchContent, String type) {
        return getTargetValue(searchType, searchContent,
                type);
    }

    /**
     * 获取OperationUserId
     *
     * @param searchType
     * @param searchContent
     * @param type
     * @return
     */
    default String getTargetValue(String searchType, String searchContent, String type) {
        return type.equals(searchType) ? searchContent : null;
    }

    /**
     * 获取action
     *
     * @param searchType
     * @param searchContent
     * @param type
     * @return
     */
    default String getAction(String searchType, String searchContent, String type) {
        return getTargetValue(searchType, searchContent, type);
    }

    /**
     * 获取querystatus
     *
     * @param status
     * @param successCode
     * @param failCode
     * @return
     */
    default String getQueryStatus(Integer status, String successCode, String failCode) {
        String queryStatus = null;
        if (StatusCodeEnum.SUCCESS.getCode().equals(status)) {
            queryStatus = successCode;
        } else if (StatusCodeEnum.FAILED.getCode().equals(status)) {
            queryStatus = failCode;
        }
        return queryStatus;
    }

    /**
     * 获取api搜索内容
     *
     *
     * @param code
     * @param searchType
     * @param searchContent
     * @param maxSize
     * @param type
     * @return
     */
    default String getContent(int code, String searchType, String searchContent, int maxSize, String type) {
        if (type.equals(searchType)) {
            String[] keywordsArray = getkeyWords(searchContent, maxSize);
            for (String keyWords : keywordsArray) {
                if (splitKeyWords(keyWords).length < 2) {
                    throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT);
                }
                if(code == LogTypeEnum.OPERATION_LOG.getCode()){
                    searchContent = replaceOperationLogSearchContent(searchContent, getKey(keyWords).trim(), getValue(keyWords).trim(), keyWords);
                }

            }
        }
        return searchContent;
    }

    /**
     * 判断是否存在相同的key值
     *
     * @param keywordsArray
     * @return
     */
    default boolean isKeywordsHasDuplicateKey(String[] keywordsArray) {
        Set<Object> keySet = new HashSet<>();
        for (String keywords : keywordsArray) {
            keySet.add(keywords.split("=")[0]);
        }
        return keySet.size() != keywordsArray.length;
    }

    /**
     * 根据 AND | OR 对searchContent进行分割
     *
     * @param searchContent
     * @param maxSize
     * @return
     */
    default String[] getkeyWords(String searchContent, Integer maxSize) {
        String[] keywords = searchContent.split(" AND | OR ");
        if (keywords.length > maxSize || isKeywordsHasDuplicateKey(keywords)) {
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT);
        }
        return keywords;
    }

    /**
     * 获取keywords的第一个元素
     *
     * @param keyWords
     * @return
     */
    default String getKey(String keyWords) {
        return splitKeyWords(keyWords)[0];
    }

    /**
     * 获取keywords的第二个元素
     *
     * @param keyWords
     * @return
     */
    default String getValue(String keyWords) {
        return splitKeyWords(keyWords)[1];
    }

    /**
     * 分割keywords
     *
     * @param keyWords
     * @return
     */
    default String[] splitKeyWords(String keyWords) {
        return keyWords.split("=");
    }

    /**
     * OperationLog搜索内容
     *
     * @param searchContent
     * @param key
     * @param value
     * @param source
     * @return
     */
    default String replaceOperationLogSearchContent(String searchContent, String key, String value, String source) {
        switch (key.trim()) {
            // OperationLog
            case "operation":
                // 操作编码
                searchContent = searchContent.replace(source, "operation_code = '" + value + "'");
                break;
            case "operator":
                searchContent = searchContent.replace(source, "operation_by_name = '" + value + "'");
                break;
            case "object":
                searchContent = searchContent.replace(source, "operation_target = '" + value + "'");
                break;
            case "code":
                searchContent = searchContent.replace(source, "status_code = '" + value + "'");
                break;
            case "IP":
                searchContent = searchContent.replace(source, "request_ip = '" + value + "'");
                break;
            default:
        }
        return searchContent;
    }

}
