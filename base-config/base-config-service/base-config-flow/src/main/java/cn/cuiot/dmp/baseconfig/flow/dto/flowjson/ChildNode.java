package cn.cuiot.dmp.baseconfig.flow.dto.flowjson;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Author:LoveMyOrange
 * @Description:
 * @Date:Created in 2022/10/9 16:20
 * <p>
 * process: "
 * {\"id\":\"root\",\
 * "parentId\":null,\
 * "type\":\"ROOT\",\
 * "name\":\"发起人\",\
 * "desc\":\"任何人\",\
 * "props\":{\
 * "assignedUser\":
 * <p>
 * <p>
 * [{\"id\":1486186,\"name\":\"xx科技有限公司\",\"type\":\"dept\",\"sex\":null,\"selected\":false}],\
 * "formPerms\":[{\"id\":\"field6131501574832\",\"title\":\"单行文本输入\",\"required\":true,\"perm\":\"E\"}]},\
 * "children\":{\"id\":\"node_040730749764\",\"parentId\":\"root\",\"props\":{\"assignedType\":\"ASSIGN_USER\",\"mode\":\"AND\",\"sign\":true,\"nobody\":{\"handler\":\"TO_PASS\",\"assignedUser\":[]},\"timeLimit\":{\"timeout\":{\"unit\":\"H\",\"value\":\"1\"},\"handler\":{\"type\":\"REFUSE\",\"notify\":{\"once\":true,\"hour\":1}}},\"assignedUser\":[{\"id\":381496,\"name\":\"旅人\",\"type\":\"user\",\"sex\":false,\"selected\":false}],\"formPerms\":[{\"id\":\"field6131501574832\",\"title\":\"单行文本输入\",\"required\":true,\"perm\":\"R\"}],\"selfSelect\":{\"multiple\":false},\"leaderTop\":{\"endCondition\":\"TOP\",\"endLevel\":1},\"leader\":{\"level\":1},\"role\":[],\"refuse\":{\"type\":\"TO_END\",\"target\":\"\"},\"formUser\":\"\"},\"type\":\"APPROVAL\",\"name\":\"审批人\",\"children\":{}}}"
 */
@Data
public class ChildNode {
    /**
     * 节点ID
     */
    private String id;
    /**
     * 父节点ID
     */
    private String parentId;
    /**
     * 节点类型
     */
    private String type;
    /**
     * 节点名称
     */
    private String name;
    /**
     * 节点描述
     */
    private String desc;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 节点属性
     */
    private Properties props;
    /**
     * 子节点
     */
    private ChildNode children;
    /**
     * 分支节点
     */
    private List<ChildNode> branchs;
    /**
     * 并行节点
     */
    private String parallelStr;
    /**
     * 入参
     */
    private JSONObject incoming = new JSONObject();
    /**
     * 是否为else节点
     */
    private Boolean typeElse;


    /**
     * 通过
     */
    private Integer formTaskAccessRate;
}

