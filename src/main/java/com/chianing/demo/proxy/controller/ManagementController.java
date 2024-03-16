package com.chianing.demo.proxy.controller;

import com.chianing.demo.proxy.model.dto.ProxyConfig;
import com.chianing.demo.proxy.model.vo.ResultVO;
import com.chianing.demo.proxy.service.CoreService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author chianing
 * @description TODO
 * @date 2024/03/16 08:00
 */
@Slf4j
@RestController
@RequestMapping("/management")
public class ManagementController {

    @Resource
    private CoreService coreService;

    @GetMapping("/listAll")
    public ResultVO<List<ProxyConfig>> listAll() {
        try {
            return ResultVO.success(coreService.listAll());
        } catch (Exception e) {
            log.error("list all error", e);
            return ResultVO.failed(e.getMessage());
        }
    }

    @PutMapping("/reload")
    public ResultVO<Void> reload() {
        return ResultVO.failed("not support yet");
        // try {
        //     coreService.reloadConfig();
        //     return ResultVO.success();
        // } catch (Exception e) {
        //     log.error("reload config error", e);
        //     return ResultVO.failed(e.getMessage());
        // }
    }

}
