package cn.cuiot.dmp.baseconfig.flow.enums;

/**
 * 审批人信息
 * @Author:LoveMyOrange
 * @Description:
 * @Date:Created in 2022/10/9 18:15
 */
public enum  AssigneeTypeEnums {
  ASSIGN_USER("ASSIGN_USER","指定人员"),
  SELF_SELECT("SELF_SELECT","发起人自己选择"),
  LEADER_TOP("LEADER_TOP","连续主管"),
  LEADER("LEADER","指定主管审批"),
  ROLE("role","系统角色"),
  DEPT("dept","系统部门"),
  SELF("SELF","发起人自己"),
  FORM_USER("FORM_USER","表单人员"),
  COMPLETE_SELECT("complete_select","完成人自己选择"),
  ;

  private String typeName;

  private String desc;

  AssigneeTypeEnums(String typeName, String desc) {
    this.typeName = typeName;
    this.desc = desc;
  }

  public String getTypeName() {
    return typeName;
  }

  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }
}
