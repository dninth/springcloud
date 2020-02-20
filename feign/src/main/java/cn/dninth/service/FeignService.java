package cn.dninth.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author yuanfang
 * @date 2020-02-20 14:54
 */
@FeignClient(value = "client-hi", fallback = FeignHystric.class)
public interface FeignService {
    @GetMapping("/hi")
    public String hi(@RequestParam("name") String name);

}
