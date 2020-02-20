package cn.dninth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

/**
 * @author yuanfang
 * @date 2020-02-20 12:44
 */
@EnableAutoConfiguration
@ComponentScan(basePackages = {"cn.dninth.controller", "cn.dninth.service"})
@EnableDiscoveryClient
public class RibbonApplication {
    public static void main(String[] args) {
        SpringApplication.run(RibbonApplication.class, args);
    }

    @Bean  // 创建一个对象到 spring 容器
    @LoadBalanced  //负载均衡
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
