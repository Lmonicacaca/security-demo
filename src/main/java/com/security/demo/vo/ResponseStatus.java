package com.security.demo.vo;

/**
 * @author coco
 * @date 2020-09-06 16:57
 **/
public enum ResponseStatus implements StatusEnum {

    SUCCESS("200", "服务器成功返回请求的数据"),
    DATA_CHANGE_SUCCESS("201","新建或修改数据成功"),
    SYNC_SUCCESS("202","一个请求已经进入后台排队（异步任务"),
    DELETE_SUCCESS("204","删除数据成功"),
    CHECK_ERROR("300", "校验错误"),
    ACL_REFUSE("401", "用户没有权限（令牌、用户名、密码错误）"),
    VISIT_REFUSE("403","用户得到授权，但是访问是被禁止的"),
    NOT_FOUND("404","发出的请求针对的是不存在的记录，服务器没有进行操作"),
    ERROR("500", "服务器发生错误，请检查服务器"),
    UNLOGIN("600", "账户未登录"),
    ACCOUNT_EXPIRED("601", "账号过期"),
    USER_CREDENTIALS_ERROR("602", "密码错误"),
    USER_CREDENTIALS_EXPIRED("603","密码过期"),
    USER_ACCOUNT_DISABLE("604","账号不可用"),
    USER_ACCOUNT_LOCKED("605","账户锁定"),
    USER_ACCOUNT_NOT_EXIST("607","账户不存在"),
    USER_ACCOUNT_USE_BY_OTHERS("608","账号下线"),
    PARAM_ERROR("700", "参数错误"),
    FILE_UPLOAD_ERROR("701", "文件上传失败");

    private String code;
    private String msg;

    ResponseStatus(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
