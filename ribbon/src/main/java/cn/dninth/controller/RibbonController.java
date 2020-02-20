package cn.dninth.controller;

import cn.dninth.service.RibbonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuanfang
 * @date 2020-02-20 13:00
 */
@RestController
@RequestMapping("/ribbon")
public class RibbonController {

    @Autowired
    private RibbonService ribbonService;

    @GetMapping("/hi")
    public String hi(String name) {
        return ribbonService.hi(name);
    }
}
