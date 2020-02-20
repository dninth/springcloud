package cn.dninth.controller;

import cn.dninth.service.FeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuanfang
 * @date 2020-02-20 14:56
 */
@RestController
public class FeignController {

    @Autowired
    private FeignService feignService;

    @GetMapping("/hi")
    public String hi(String name) {
        return feignService.hi(name);
    }
}
