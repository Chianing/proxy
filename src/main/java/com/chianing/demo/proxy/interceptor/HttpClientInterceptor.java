package com.chianing.demo.proxy.interceptor;

import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okio.Buffer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author chianing
 * @description TODO
 * @date 2024/03/16 08:18
 */
@Slf4j
public class HttpClientInterceptor implements Interceptor {

    private final String serviceName;

    private final boolean truncateResponseOutput;

    private int outputStringMaxLen = 128;

    public HttpClientInterceptor(String serviceName, boolean truncateResponseOutput) {
        this.serviceName = serviceName;
        this.truncateResponseOutput = truncateResponseOutput;
    }

    public HttpClientInterceptor(String serviceName, int outputStringMaxLen) {
        this.serviceName = serviceName;
        this.truncateResponseOutput = true;
        this.outputStringMaxLen = outputStringMaxLen;
    }

    @Override
    public Response intercept(@NotNull Chain chain) {
        long startTime = System.currentTimeMillis();

        Request request = chain.request();
        Response response;
        String responseStr = "";
        String requestParamOutput = "";

        try {
            response = chain.proceed(chain.request());
            if (response.body() == null) {
                return response;
            }

            MediaType mediaType = response.body().contentType();
            responseStr = response.body().string();

            Response.Builder builder = response.newBuilder();
            builder.setBody$okhttp(ResponseBody.create(responseStr, mediaType));
            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            long endTime = System.currentTimeMillis();
            if (request.body() != null) {
                requestParamOutput = getRequestParamOutput(request.body(), serviceName);
            }

            log.info("\n-----Http Request Info-----" +
                     "\n[client name]: " + serviceName +
                     "\n[request url]: " + request.url() +
                     "\n[request method]: " + request.method() + "\t[time cost]: " + (endTime - startTime) + "ms" +
                     "\n[headers]: " +
                     "\n" + request.headers() +
                     "\n[request output]: " + requestParamOutput +
                     "\n[response output]: " + truncateOutputStr(responseStr) +
                     "\n-----Request Finished------");

        }
    }

    private String getRequestParamOutput(RequestBody body, String serviceName) {
        Buffer buffer = new Buffer();
        try {
            body.writeTo(buffer);
            Charset charset = StandardCharsets.UTF_8;
            MediaType contentType = body.contentType();
            if (contentType != null) {
                charset = contentType.charset(StandardCharsets.UTF_8);
            }
            assert charset != null;
            return buffer.readString(charset);
        } catch (IOException e) {
            log.error("print params error, serviceName: {}", serviceName, e);
            return "";
        }
    }

    private String truncateOutputStr(String output) {
        if (!truncateResponseOutput ||
            StringUtils.isBlank(output) ||
            output.length() <= outputStringMaxLen) {
            return output;
        }

        return output.substring(0, outputStringMaxLen) + "...";
    }
}
