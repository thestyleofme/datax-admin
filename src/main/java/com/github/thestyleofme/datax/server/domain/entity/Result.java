package com.github.thestyleofme.datax.server.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * <p>
 * description
 * </p>
 *
 * @author thestyleofme 2020/12/11 11:55
 * @since 1.0.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> {

    private static final String TYPE_ERROR = "error";

    private Boolean failed;
    private Integer code;
    private String message;
    /**
     * "type": "warn"
     */
    private String type;
    /**
     * 数据
     */
    private T data;

    private Result() {
    }

    private Result(Boolean failed, Integer code, String message, T data, String type) {
        this.failed = failed;
        this.code = code;
        this.message = message;
        this.data = data;
        this.type = type;
    }

    public static <T> Result<T> ok(T data) {
        return new Result<>(false, 200, null, data, null);
    }


    public static Result<Void> fail(String message) {
        return new Result<>(true, 500, message, null, TYPE_ERROR);
    }

    public static <T> Result<T> fail(T data, String message) {
        return new Result<>(true, 500, message, data, TYPE_ERROR);
    }

    public static <T> Result<T> fail(T data, Integer code, String message) {
        return new Result<>(true, code, message, data, TYPE_ERROR);
    }

    public Boolean getFailed() {
        return failed;
    }

    public void setFailed(Boolean failed) {
        this.failed = failed;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
