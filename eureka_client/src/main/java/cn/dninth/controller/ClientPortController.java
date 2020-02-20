package cn.dninth.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuanfang
 * @date 2020-02-20 11:40
 */
@RestController
@RequestMapping("/clientHi")
public class ClientPortController {

    @Value("${server.port}")
    private String port;

    @GetMapping("/getPort")
    public String getPort(String name) {
        return "我是" + name + "端口号是：" + port;
    }
}
