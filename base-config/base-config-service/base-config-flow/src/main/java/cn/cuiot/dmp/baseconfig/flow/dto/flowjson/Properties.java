package cn.cuiot.dmp.baseconfig.flow.dto.flowjson;

import cn.cuiot.dmp.baseconfig.custommenu.vo.FlowTaskConfigVo;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:LoveMyOrange
 * @Description:
 * @Date:Created in 2022/10/9 16:30

\"assignedType\":\"ASSIGN_USER\",\"mode\":\"AND\",\"sign\":true,\
"nobody\":{\"handler\":\"TO_PASS\",\"assignedUser\":[]},\
"timeLimit\":{\"timeout\":{\"unit\":\"H\",\"value\":\"1\"},\
"handler\":{\"type\":\"REFUSE\",\"notify\":{\"once\":true,\"hour\":1}}},\
"assignedUser\":[{\"id\":381496,\"name\":\"旅人\",\"type\":\"user\",\"sex\":false,\"selected\":false}],\
"formPerms\":[{\"id\":\"field6131501574832\",\"title\":\"单行文本输入\",\"required\":true,\"perm\":\"R\"}],\
"selfSelect\":{\"multiple\":false},\
"leaderTop\":{\"endCondition\":\"TOP\",\"endLevel\":1},\
"leader\":{\"level\":1},\"role\":[],\
"refuse\":{\"type\":\"TO_END\",\"target\":\"\"},\"formUser\":\"\"},\"type\":\"APPROVAL\",\"name\":\"审批人\",\"children\":{}}}"

 */
@Data
public class Properties {
  private String subprocessId;
  /**
   * ASSIGN_USER指定审批人 SELF发起人自己 SELF_SELECT发起人自己选择 LEADER_TOP连续主管 LEADER指定主管审批 ROLE系统角色 FORM_USER表单人员
   * role 系统角色 dept 系统部门 complete_select 完成人自己选择
   * @see cn.cuiot.dmp.baseconfig.flow.enums.AssigneeTypeEnums
   */
  private String assignedType;
  private List<UserInfo> assignedUser;
  //发起人自旋  multiple true false
  private Map<String,Object> selfSelect;
  //连续主管 endCondition TOP   LEAVE    endLevel  level
  private Map<String,Object> leaderTop=new HashMap<>();
  //指定主管审批
  private Map<String,Object> leader=new HashMap<>();
  //系统角色
  private List<Map<String,Object>> role;
  //表单人员
  private String formUser;


  //审批人为空的规则  hander 和 assignedUser
  private Map<String,Object> nobody;
  /**
   * 会签 （按选择顺序办理，每个人必须同意）->NEXT
   * 会签（可同时办理，每个人必须同意） -> AND
   * 或签（有一人同意即可） -> OR
   * 或签 按比例ton过 -> OR_RATE
   */
  private String mode;

  /**
   * 会签比例
   */
  private Integer modeRate;

  private Boolean sign;


  private Byte flag;
  //审批超时
  private TimeLimit timeLimit;

  private Map<String,Object> refuse;

  //------------------------------------表单通过配置
  //节点保存内容 0表单 1任务
  private Byte nodeProcessType;
  /**
   * 表单内容
   */
  private List<FormOperates> formPerms;

  /**
   * 任务配置，包含表单信息
   */
  private FlowTaskConfigVo taskConfig;

  /**
   * 表单需要处理的任务
   */
  private Long formTaskId;

  /**
   * 表单ID
   */
  private List<Long> formIds;

  /**
   * 表单需要处理的任务任务完成规则 0按顺序执行 1随机
   */
  private Byte formTaskType;

  /**
   * ○任务完成比例
   * ■任务对象完成比例大于等于设置的比例时，才算完成任务；
   * ■比例值可输入大于0，小于等于100的整数；
   */
  private Integer formTaskAccessRate;

  //-----------------抄送配置
  private CCInfo ccInfo;

  //------------------------------------->
  private String groupsType;
  private String expression;
  private List<GroupsInfo> groups;

  //
  private Boolean shouldAdd;
  //
  private String type;
  private Long time;
  private String unit;
  private String dateTime;

  //
  private HttpInfo http;
  private EmailInfo email;

  /**
   * 节点按钮配置
   */
  private List<NodeButton> buttons;

  /**
   * 撤销配置
   */
  private CancelInfo cancelInfo;

  /**
   * 处理节点类型
   */
  private String processNodeType;

  /**
   * 是否允许客户端发起工单 0否 1是
   */
  private Byte isSelectAppUser;
}
