package com.chianing.demo.proxy.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.chianing.demo.proxy.model.dto.ProxyConfig;
import com.chianing.demo.proxy.model.vo.ResultVO;
import com.chianing.demo.proxy.service.CoreService;
import com.chianing.demo.proxy.utl.HttpUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.ByteStreams;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author chianing
 * @description TODO
 * @date 2024/03/16 08:18
 */
@Slf4j
@Service
public class CoreServiceImpl implements CoreService {

    @Resource
    private HttpUtil httpUtil;

    private static final String PROXY_JSON_PATH = "/proxy/proxy.json";

    private static final Map<String, ProxyConfig> PROXY_CONFIG_MAP = Maps.newConcurrentMap();

    @Override
    public List<ProxyConfig> listAll() {
        if (PROXY_CONFIG_MAP.isEmpty()) {
            init();
        }

        return Lists.newArrayList(PROXY_CONFIG_MAP.values());

    }

    @Override
    public void reloadConfig() {
        PROXY_CONFIG_MAP.clear();
        init();
    }

    @Override
    public JSONObject doProxy(HttpServletRequest request) {
        if (PROXY_CONFIG_MAP.isEmpty()) {
            init();
        }

        String currentUrl = request.getRequestURI();
        ProxyConfig proxyConfig = PROXY_CONFIG_MAP.get(currentUrl);

        if (proxyConfig == null) {
            ResultVO<Object> result = ResultVO.failed("proxy config is not exists, please reload config or check proxy.json. url: " + currentUrl);
            return JSONObject.from(result);
        }
        JSONObject mockResult = proxyConfig.getMockResult();

        // do proxy to next url
        ProxyConfig.NextNode nextNode = proxyConfig.getNextNode();
        if (nextNode == null || StringUtils.isBlank(nextNode.getMockUrl())) {
            return mockResult;
        }
        switch (Optional.ofNullable(nextNode.getMethod()).orElse("GET").toUpperCase()) {
            case "":
            case "GET":
                httpUtil.doGet(nextNode.getMockUrl());
                break;
            case "POST":
                JSONObject params = nextNode.getParams();
                httpUtil.doPost(nextNode.getMockUrl(), Optional.ofNullable(params).orElse(new JSONObject()).toString());
                break;
            default:
                throw new RuntimeException("not support method: " + nextNode.getMethod());
        }

        return mockResult;

    }


    private void init() {
        try (InputStream inputStream = this.getClass().getResourceAsStream(PROXY_JSON_PATH)) {
            if (inputStream == null) {
                log.warn("proxy.json is not exists");
                return;
            }

            String config = new String(ByteStreams.toByteArray(inputStream));
            List<ProxyConfig> proxyConfigList = JSONArray.parseArray(config, ProxyConfig.class);
            if (proxyConfigList == null || proxyConfigList.isEmpty()) {
                log.warn("proxy.json is empty");
                return;
            }

            for (ProxyConfig proxyConfig : proxyConfigList) {
                PROXY_CONFIG_MAP.put(proxyConfig.getMockUrl(), proxyConfig);
            }
            log.info("proxy config has been initialized, size: {}", PROXY_CONFIG_MAP.size());

        } catch (Exception e) {
            log.error("init proxy base map by proxy config error", e);
            throw new RuntimeException("init proxy base map error");
        }

    }

}
