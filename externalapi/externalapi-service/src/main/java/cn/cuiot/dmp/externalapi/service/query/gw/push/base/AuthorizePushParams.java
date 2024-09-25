package cn.cuiot.dmp.externalapi.service.query.gw.push.base;

import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.externalapi.service.entity.gw.GwEntranceGuardPersonEntity;
import com.google.common.base.Objects;
import lombok.Data;

/**
 * 授权传参
 *
 * @Author: zc
 * @Date: 2024-09-12
 */
@Data
public class AuthorizePushParams {

    private AuthorizeBody body;

    @Data
    public static class AuthorizeBody{
        /**
         * 设备sn
         */
        private String sn;

        /**
         * 权限状态
         */
        private String state;

        /**
         * 人脸token
         */
        private String faceToken;

        /**
         * 人员id
         */
        private String personId;

        /**
         * 姓名
         */
        private String name;

        /**
         * IC卡号
         */
        private String icCard;

        /**
         * 身份证号码
         */
        private String idCard;

        /**
         * 手机号
         */
        private String mobile;

        /**
         * 密码
         */
        private String password;

        /**
         * 性别 (0: 男, 1: 女)
         */
        private Integer sex;

        /**
         * 图片方式 (0: 类型A, 1: 类型B)
         */
        private Integer imageType;

        /**
         * 图片
         */
        private String image;

        /**
         * 是否长期有效 (0: 否, 1: 是)
         */
        private Integer isLongtime;

        /**
         * 权限开始时间
         */
        private String startTime;

        /**
         * 权限结束时间
         */
        private String endTime;

        /**
         * 计划ID集合
         */
        private String scheduleIdString;
    }



    /**
     * 构造权授
     *
     * @return AuthorizePushParams
     * @Param entity 参数
     * @Param sn 设备sn
     */
    public static AuthorizePushParams buildAuthorizeAddPushParams(GwEntranceGuardPersonEntity entity, String sn) {
        AuthorizePushParams params = new AuthorizePushParams();
        AuthorizePushParams.AuthorizeBody body = new AuthorizePushParams.AuthorizeBody();
        body.setSn(sn);
        body.setState("ADD");
        body.setPersonId(entity.getId() + "");
        body.setName(entity.getName());
        body.setIcCard(entity.getIcCardNo());
        body.setIdCard(entity.getIdCardNo());
        body.setMobile(entity.getPhone());
        body.setPassword(entity.getPassword());
        body.setSex(entity.getSex() - 1);
        body.setImageType(1);
        body.setImage(entity.getImage());
        body.setIsLongtime(Math.abs(entity.getPrescriptionType().intValue() - 1));
        if (Objects.equal(body.getIsLongtime(), EntityConstants.NO.intValue())) {
            body.setStartTime(DateTimeUtil.dateToString(entity.getPrescriptionBeginDate()));
            body.setEndTime(DateTimeUtil.dateToString(entity.getPrescriptionEndDate()));
        }
        params.setBody(body);
        return params;
    }

    /**
     * 取消权授
     *
     * @return AuthorizePushParams
     * @Param personId 人员nid
     */
    public static AuthorizePushParams buildAuthorizeDeletePushParams(Long personId) {
        AuthorizePushParams params = new AuthorizePushParams();
        AuthorizePushParams.AuthorizeBody body = new AuthorizePushParams.AuthorizeBody();
        body.setState("DELETE");
        body.setPersonId(personId + "");
        params.setBody(body);
        return params;
    }
}
