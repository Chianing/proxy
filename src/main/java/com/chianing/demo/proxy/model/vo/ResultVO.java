package com.chianing.demo.proxy.model.vo;

import com.chianing.demo.proxy.enums.ResultCode;
import lombok.Builder;
import lombok.Data;

/**
 * @author chianing
 * @description TODO
 * @date 2024/03/16 08:02
 */
@Data
@Builder
public class ResultVO<T> {
    private int code;
    private String message;
    private T data;

    public static <T> ResultVO<T> success() {
        return ResultVO.<T>builder()
                .code(ResultCode.SUCCESS.getCode())
                .build();
    }

    public static <T> ResultVO<T> success(T data) {
        return ResultVO.<T>builder()
                .code(ResultCode.SUCCESS.getCode())
                .data(data)
                .build();
    }

    public static <T> ResultVO<T> failed(String message) {
        return ResultVO.<T>builder()
                .code(ResultCode.FAILED.getCode())
                .message(message)
                .build();
    }
}