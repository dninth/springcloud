package cn.dninth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author yuanfang
 * @date 2020-02-20 12:54
 */
@Service
public class RibbonService {

    @Autowired
    private RestTemplate restTemplate;

    public String hi(String name) {
        return restTemplate.getForObject("http://client-hi/hi?name=" + name, String.class);
    }
}
