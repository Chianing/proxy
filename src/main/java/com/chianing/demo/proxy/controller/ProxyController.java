package com.chianing.demo.proxy.controller;

import com.alibaba.fastjson2.JSONObject;
import com.chianing.demo.proxy.model.vo.ResultVO;
import com.chianing.demo.proxy.service.CoreService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chianing
 * @description TODO
 * @date 2024/03/16 09:53
 */
@Slf4j
@RestController
public class ProxyController {

    @Resource
    private CoreService coreService;

    @RequestMapping("/**")
    public JSONObject doProxy(HttpServletRequest request) {
        try {
            return coreService.doProxy(request);
        } catch (Exception e) {
            log.error("do proxy error", e);
            return JSONObject.from(ResultVO.failed(e.getMessage()));
        }
    }

}
