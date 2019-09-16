package com.zpk.console.model.file;

public class RestResult<T> {
    private static final long serialVersionUID = 4840778423101210973L;
    private String code;
    private String message;
    private T data;

    public static final String RESULT_SUCCESS = "0";
    public static final String RESULT_FAILD = "1";
    public static final String RESULT_RETRY = "2";
    public static final String RESULT_WARN = "3";
    public static final String RESULT_ERROR = "999";

    public RestResult() {
        this.code = RestResult.RESULT_SUCCESS;
        this.message = "success";
    }

    public RestResult(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RestResult{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
