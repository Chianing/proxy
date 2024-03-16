package com.chianing.demo.proxy.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author chianing
 * @description TODO
 * @date 2024/03/16 08:06
 */
@Getter
@AllArgsConstructor
public enum ResultCode {
    SUCCESS(200),

    PARAMS_ERROR(400),

    FAILED(500);

    private final int code;

}
