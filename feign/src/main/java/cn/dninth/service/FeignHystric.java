package cn.dninth.service;

import org.springframework.stereotype.Component;

/**
 * @author yuanfang
 * @date 2020-02-20 15:00
 */
@Component
public class FeignHystric implements FeignService {
    @Override
    public String hi(String name) {
        return "feign ... name：" + name + " 出现了系统错误！";
    }
}
