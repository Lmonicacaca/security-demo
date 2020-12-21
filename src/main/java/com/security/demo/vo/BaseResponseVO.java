package com.security.demo.vo;


import lombok.Data;

import java.io.Serializable;

@Data
public class BaseResponseVO<T> implements Serializable {
    private static final long serialVersionUID = -2563911741093921552L;

    private boolean success = true;

    private String errorCode;

    private String errorMessage;

    private T data;


    public BaseResponseVO() {
    }
    public BaseResponseVO(StatusEnum e, String msg, T data) {
        this.errorCode = e.getCode();
        this.errorMessage = msg;
        this.data = data;
    }

    public BaseResponseVO(StatusEnum e, String msg) {
        this.errorCode = e.getCode();
        this.errorMessage = msg;
    }
    public static <T> BaseResponseVO<T> success() {
        return new BaseResponseVO(ResponseStatus.SUCCESS, ResponseStatus.SUCCESS.getMsg());
    }

    public static <T> BaseResponseVO<T> success(T data) {
        return new BaseResponseVO(ResponseStatus.SUCCESS, ResponseStatus.SUCCESS.getMsg(), data);
    }

    public static <T> BaseResponseVO<T> success(String msg, T data) {
        return new BaseResponseVO(ResponseStatus.SUCCESS, msg, data);
    }

    public BaseResponseVO status(StatusEnum e){
        this.errorCode = e.getCode();
        this.errorMessage = e.getMsg();
        this.success = false;
        return this;
    }


    public static BaseResponseVO build() {
        return new BaseResponseVO();
    }
}
