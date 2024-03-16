package com.chianing.demo.proxy.utl;

import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author chianing
 * @description TODO
 * @date 2024/03/16 09:17
 */
@Slf4j
@Component
public class HttpUtil {

    @Resource
    private OkHttpClient defaultOkHttpClient;


    public String doGet(String url) {
        Request request = new Request.Builder().url(url).build();

        return doRequest(request);
    }

    public String doPost(String url, String params) {
        MediaType mediaType = MediaType.parse("application/json; charset=UTF-8");
        RequestBody requestBody = RequestBody.Companion.create(params, mediaType);
        Request request = new Request.Builder().url(url).post(requestBody).build();

        return doRequest(request);
    }

    private String doRequest(Request request) {
        try (Response response = defaultOkHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Unexpected http response code: " + response.code());
            }
            if (response.body() == null) {
                throw new RuntimeException("Unexpected empty response body");
            }
            return response.body().string();
        } catch (IOException e) {
            log.error("Failed to execute http request, url: {}, params: {}", request.url(), JSONObject.toJSONString(request.body()), e);
            throw new RuntimeException("Failed to execute http request", e);
        }
    }

}
