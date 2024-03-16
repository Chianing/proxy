package com.chianing.demo.proxy.controller;

import com.chianing.demo.proxy.model.vo.ResultVO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author chianing
 * @description TODO
 * @date 2024/03/16 10:31
 */
@Slf4j
@RestController
@RequestMapping("/self")
public class SelfTestController {

    @GetMapping("/test")
    public ResultVO<String> invokeGetEntrypoint() {
        return ResultVO.success("GET test entrypoint invoked successfully");
    }

    @PostMapping("/test")
    public ResultVO<String> invokePostEndpoint(HttpServletRequest request) {
        StringBuilder requestBody = new StringBuilder();
        String line;

        try {
            // 逐行读取请求体内容
            while ((line = request.getReader().readLine()) != null) {
                requestBody.append(line);
            }
        } catch (IOException e) {
            log.error("Failed to read request body");
        }

        log.info("POST test entrypoint invoked, params: {}", requestBody);
        return ResultVO.success("POST test entrypoint invoked successfully");
    }

}
