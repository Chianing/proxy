package com.chianing.demo.proxy.model.dto;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

import java.util.Map;

/**
 * @author chianing
 * @description TODO
 * @date 2024/03/16 08:22
 */
@Data
public class ProxyConfig {

    private String mockUrl;
    private JSONObject mockResult;
    private NextNode nextNode;

    @Data
    public static class NextNode {
        private String mockUrl;
        private String method;
        private Map<String, String> headers;
        private JSONObject params;
    }

}
