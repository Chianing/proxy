package com.chianing.demo.proxy.configuration;

import com.chianing.demo.proxy.interceptor.HttpClientInterceptor;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author chianing
 * @description TODO
 * @date 2024/03/16 08:18
 */
@Configuration
public class OkHttpClientConfiguration {

    @Bean(name = "defaultOkHttpClient")
    public OkHttpClient okHttpClient() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .addInterceptor(new HttpClientInterceptor("defaultOkHttpClient", false))
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS);
        clientBuilder.setConnectionPool$okhttp(new ConnectionPool(8, 5, TimeUnit.MINUTES));
        return clientBuilder.build();
    }

}
