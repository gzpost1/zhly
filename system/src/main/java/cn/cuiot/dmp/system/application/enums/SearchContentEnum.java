package cn.cuiot.dmp.system.application.enums;

/**
 * @Author: guoying
 * @Description: 搜索类型枚举
 * @Date: 2020/11/27
 */
public enum SearchContentEnum {
    /**
     * 操作者
     */
    OPERATION_USER_ID("1", "operationUserId"),
    /**
     * 操作
     */
    ACTION("2", "action"),
    /**
     * 内容关键字搜索
     */
    CONTENT_KEYWORDS("3", "contentKeywords"),
    /**
     * transId
     */
    TRANS_ID("4","transId");

    private String type;
    private String desc;

    SearchContentEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public String getType() {
        return type;
    }
}
