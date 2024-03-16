package com.chianing.demo.proxy.service;

import com.alibaba.fastjson2.JSONObject;
import com.chianing.demo.proxy.model.dto.ProxyConfig;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * @author chianing
 * @description TODO
 * @date 2024/03/16 08:18
 */
public interface CoreService {

    List<ProxyConfig> listAll();

    void reloadConfig();

    JSONObject doProxy(HttpServletRequest request);

}
