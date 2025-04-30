package com.test.lotterysystem.Comment.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public enum ServiceErrorCode implements ErrorCode{
    // --------- 登录时异常码 ---------
    REGISTER_INFO_IS_EMPTY(100,"注册信息为空"),
    MAIL_IS_ERROR(101,"邮箱格式错误"),
    PHONE_IS_ERROR(102,"手机格式错误"),
    IDENTITY_IS_ERROR(103, "用户身份错误"),
    PASSWORD_IS_EMPTY(104,"管理员密码为空"),
    PASSWORD_ERROR(105,"密码错误"),
    MAIL_IS_USE(106,"邮箱已经被使用"),
    PHONE_IS_USE(107,"手机号已经被使用"),
    LOGIN_INFO_NOT_EXIST(108,"登录类型不符合要求"),
    LOGIN_NOT_EXIST(109,"登录时邮箱或手机号格式错误"),
    USER_NOT_EXIST(110,"用户不存在"),
    LOGIN_INFO_IS_EMPTY(111,"登录信息为空"),
    VERIFICATION_CODE_ERROR(112,"短信验证码错误"),
    GET_USER_IDENTITY_ERROR(113,"查询用户身份异常"),



    // --------- 活动创建时异常码 ---------
    ACTIVITY_INFO_IS_EMPTY(300,"活动数据为空"),
    ACTIVITY_USERS_IS_ERROR(301,"活动用户选择异常"),
    ACTIVITY_PRIZES_IS_ERROR(301,"活动奖品选择异常"),
    ACTIVITY_PRIZE_NUMBER_ERROR(302,"活动奖品数目异常"),
    ACTIVITY_ID_IS_EMPTY(303,"活动id为空"),




    // --------- 抽奖时异常码 ---------
    DRAW_PRIZE_INFO_IS_EMPTY(400,"活动奖品数目异常"),
    DRAW_PRIZE_PARAM_IS_EMPTY(401,"发送消息为空"),
    DRAW_PRIZE_ACTIVITY_OR_PRIZE_IS_EMPTY(401,"活动或活动奖品为空"),
    ACTIVITY_STATUS_ERROR(402,"活动状态异常"),
    PRIZE_STATUS_ERROR(403,"奖品状态异常"),
    WINNER_PRIZE_AMOUNT_ERROR(404,"奖品数量与人员数量不同"),
    CONVERT_ERROR(405,"修改发生异常"),
    CACHE_ACTIVITY_ID_IS_EMPTY(406,"缓存的活动id为空");




    private Integer code;
    private String errMsg;
    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getErrMsg() {
        return errMsg;
    }
}
