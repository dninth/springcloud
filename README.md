### SpringCloudDemo

- 创建一个空的 project（maven） 项目 **springcloud**，不需要任何依赖。

#### EurekaServer

EurekaServer：注册中心

- 选中 **springcloud**  右键 model  选择 maven 创建注册中心 **eureka_server** 

  - **pom.xml** 
  
    ```xml
    <dependencies>
    	<!-- 配置 eureka 服务中心 -->
    	<dependency>
    		<groupId>org.springframework.cloud</groupId>
    		<artifactId>
           spring-cloud-starter-netflix-eureka-server
        </artifactId>
    		<version>2.2.1.RELEASE</version>
    	</dependency>
  </dependencies>
    ```
  
  - **application.yml** 
  
    ```yaml
    server:
      port: 8761
    eureka:
      instance:
        hostname: localhost
      client:
        register-with-eureka: false
        fetch-registry: false
        service-url:
      		defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
    ```
  
  - **EurekaApplication** 
  
    ```java
    package cn.dninth;
    
    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;
    import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
    
    
    @SpringBootApplication
    @EnableEurekaServer
    public class EurekaApplication {
        public static void main(String[] args) {
            SpringApplication.run(EurekaApplication.class, args);
        }
    }
    ```
  
  启动 **EurekaApplication** 在浏览器地址栏输入 http://localhost:8761/ 即可进入 Eureka 管理服界面。
  

#### EurekaClient

EurekaClient：服务提供者，当client向server注册时，它会提供一些元数据，例如主机和端口，URL，主⻚等。Eureka server 从每个client实例接收心跳消息。如果心跳超时，则通常将该实例从注册server中删除。

- 选中 **springcloud**  右键 model  选择 maven 创建服务提供者 **eureka_client**

  - **pom.xml**

    ```xml
    <dependencies>
      <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        <version>2.2.1.RELEASE</version>
        </dependency>
    
      <dependency>
         <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-starter-web</artifactId>
      </dependency>
    </dependencies>
    ```

  - **application.yml**

    ```yaml
    server:
      port: 8010
    eureka:
      client:
        service-url:
          defaultZone: http://localhost:8761/eureka/  # 去该地址注册自己
    spring:
      application:
        name: client-hi   # 注册时的名字
    ```

  - **ClientApplication**

    ```java
    package cn.dninth;
    
    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;
    import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
    
    @SpringBootApplication
    @EnableEurekaClient
    public class ClientApplication {
        public static void main(String[] args) {
            SpringApplication.run(ClientApplication.class, args);
        }
    }
    ```

  - **controller**

    ```java
    package cn.dninth.controller;
    
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RestController;
    
    @RestController
    public class ClientPortController {
        @Value("${server.port}")
        private String port;
    
        @GetMapping("/hi")
        public String hi(String name) {
            return "我是" + name + "端口号是：" + port;
        }
    }
    ```

  先启动 注册中心（**EurekaApplication** ） 接着启动 服务提供者（**ClientApplication**），启动完后 

  进入 http://localhost:8761/ 即可看到服务提供者注册的名字及端口号。

  浏览器网址输入 http://localhost:8010/clientHi/getPort 即可看到 controller 返回的结果。

#### Ribbon

Ribbon：Ribbon 是一个客户端的负载均衡器，它提供对大量的 HTTP 和 TCP 客户端的访问控制。Ribbon的一个核心概念是命名的客户端。每个负载均衡器都是这个组件的全体的一部分，它们一起工作来连接到服务器，并且它们全体都有一个给定的名字。

- 选中 **springcloud**  右键 model  选择 maven 创建服务消费者 **ribbon**

  - **pom.xml**

    ```xml
    <dependencies>
      <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
      </dependency>
    
      <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        <version>2.2.1.RELEASE</version>
      </dependency>
    
      <dependency>
         <groupId>org.springframework.cloud</groupId>
         <artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
         <version>2.2.1.RELEASE</version>
      </dependency>
    </dependencies>
    ```

  - **application.yml**

    ```yaml
    server:
      port: 8020
    eureka:
      client:
        service-url:
          default-zone: http://localhost:8761/eureka/  # 去该地址注册自己
    spring:
      application:
        name: ribbon   # 注册时的名字
    ```

  - **RibbonApplication**

    ```java
    package cn.dninth;
    
    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
    import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
    import org.springframework.cloud.client.loadbalancer.LoadBalanced;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.ComponentScan;
    import org.springframework.web.client.RestTemplate;
    
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
    ```

  - **RibbonService**

    ```java
    package cn.dninth.service;
    
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;
    import org.springframework.web.client.RestTemplate;
    
    @Service
    public class RibbonService {
    
        @Autowired
        private RestTemplate restTemplate;
    
        public String hi(String name) {
            return restTemplate.getForObject("http://client-hi/hi?name=" + name, String.class);
        }
    }
    ```

  - **RibbonController**

    ```java
    package cn.dninth.controller;
    
    import cn.dninth.service.RibbonService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RestController;
    
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
    ```

  先启动 注册中心（**EurekaApplication** ） 接着启动 服务提供者（**ClientApplication**）在把 服务提供者 的

  端口改为 8011，使用 **run maven（spring-boot:run）** 的方式再次启动，不用停掉前面启动的！这样就会

  形成一个小的集群，用来测试负载均衡效果。最后把 服务消费者（**RibbonApplication**）启动。

  输入 http://localhost:8020/hi?name=tom 就可以看到 ribbon 调用了 服务提供者的方法。

  ```java
  public String hi(String name) {
     			return restTemplate.getForObject("http://client-hi/clientHi/getPort?name=" + name, String.class);
  }
  ```

  这个方法就是 **调用远程服务** 并返回一个对象，类型就是后面指定的（String.class）。

  这个时候多请求（刷新）几次浏览器就会看到返回的端口号是不同的，这样就简单的实现了负载均衡效果。

#### Feign

Feign：Feign是一个声明式的伪 Http 客户端，它使得写 Http 客户端变得更简单。使用 Feign，只需要创建一个接
口并注解。它具有可插拔的注解特性，可使用 Feign 注解和 JAX-RS 注解。Feign 支持可插拔的编码器和解码器。Feign 默认集成了 Ribbon，并和 Eureka 结合，默认实现了负载均衡的效果。Feign 采用的是基于接口的注解并且Feign 整合了了ribbon

- 选中 **springcloud**  右键 model  选择 maven 创建服务消费者 **feign**

  - **pom.xml**

    ```yaml
    <dependencies>
      <dependency>
          <groupId>org.springframework.cloud</groupId>
          <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
          <version>2.2.1.RELEASE</version>
      </dependency>
    
      <dependency>
         <groupId>org.springframework.cloud</groupId>
         <artifactId>spring-cloud-starter-openfeign</artifactId>
         <version>2.2.1.RELEASE</version>
      </dependency>
    
    </dependencies>
    ```

  - **application.yml**

    ```yaml
    server:
      port: 8030
    eureka:
      client:
        service-url:
          default-zone: http://localhost:8761/eureka/  # 去该地址注册自己
    spring:
      application:
        name: feign   # 注册时的名字
    feign:
      hystrix:
        enabled: true
    
    ```

  - **FeignApplication**

    ```java
    package cn.dninth;
    
    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;
    import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
    import org.springframework.cloud.openfeign.EnableFeignClients;
    
    @SpringBootApplication
    @EnableEurekaClient
    @EnableFeignClients
    public class FeignApplication {
        public static void main(String[] args) {
            SpringApplication.run(FeignApplication.class, args);
        }
    }
    ```

  - **FeignService**

    ```java
    package cn.dninth.service;
    
    import org.springframework.cloud.openfeign.FeignClient;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.RequestParam;
    
    @FeignClient(value = "client-hi", fallback = FeignHystric.class)
    public interface FeignService {
        @GetMapping("/hi")
        public String hi(@RequestParam("name") String name);
    }
    ```

  - **FeignHystric**

    ```java
    package cn.dninth.service;
    
    import org.springframework.stereotype.Component;
    
    @Component
    public class FeignHystric implements FeignService {
        @Override
        public String hi(String name) {
            return "feign ... name：" + name + " 出现了系统错误！";
        }
    }
    ```

  - **FeignController**

    ```java
    package cn.dninth.controller;
    
    import cn.dninth.service.FeignService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.RestController;
    
    @RestController
    public class FeignController {
    
        @Autowired
        private FeignService feignService;
    
        @GetMapping("/hi")
        public String hi(String name) {
            return feignService.hi(name);
        }
    }
    ```

  启动方式与 Ribbon 一样，浏览器地址输入 http://localhost:8020/hi?name=tom 就可以看到 Feign 调用了 服务提供者的方法。

  ```java
  @FeignClient(value = "client-hi", fallback = FeignHystric.class)
  public interface FeignService {
      @GetMapping("/hi")
      public String hi(@RequestParam("name") String name);
  }
  ```

  通过注解 **@FeignClient** 里面的 **value** 指定服务的名称，与相同的请求方式，例：**@GetMapping("/hi")** 来调用对应的方法。因为默认实现了负载均衡的效果，也可以多次刷新看到返回不同的结果。

#### Hystrix

Hystrix：在微服务架构中，根据业务来拆分成⼀一个个的服务，服务与服务之间可以相互调⽤用（RPC），在SpringCloud 可以⽤ RestTemplate+Ribbon 和 Feign 来调⽤。为了保证其高可⽤，单个服务通常会集群部署。由于网络原因或者自身的原因，服务并不能保证100%可⽤，如果单个服务出现问题，调用这个服务就会出现线程阻塞，此时若有大量的请求涌入，Servlet容器的线程资源会被消耗完毕，导致服务瘫痪。服务与服务之间的依赖性，故障会传播，会对整个微服务系统造成灾难性的严重后果，这就是服务故障的“雪崩”效应。

上面 **feign** 已经使用了断路器，注解 **@FeignClient** 里面的 ” fallback “ 指向另外一个类，响应此处出现问题。

```java
@FeignClient(value = "client-hi", fallback = FeignHystric.class)
```



#### Zuul

Zuul：netflix开源的⼀个API Gateway 服务器, 本质上是⼀个web servlet应⽤。Zuul 在云平台上提供动态路由，监控，弹性，安全等边缘服务的框架。Zuul 相当于是设备和 Netflix 流应用的 Web 网站后端所有请求的前门。

- 选中 **springcloud**  右键 model  选择 maven 创建网关 **zuul**

  - **pom.xml**

    ```xml
    <dependencies>
       <dependency>
         <groupId>org.springframework.cloud</groupId>
         <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
         <version>2.2.1.RELEASE</version>
      </dependency>
      <dependency>
         <groupId>org.springframework.cloud</groupId>
         <artifactId>spring-cloud-starter-netflix-zuul</artifactId>
         <version>2.2.1.RELEASE</version>
      </dependency>
    </dependencies>
    ```

  - **application.yml**

    ```yaml
    server:
      port: 8040
    eureka:
      client:
        service-url:
          default-zone: http://localhost:8761/eureka/  # 去该地址注册自己
    spring:
      application:
        name: zuul   # 注册时的名字
    zuul:
      routes:
        api-a:
          path: /api-a/**
          service-id: ribbon
        api-b:
          path: /api-b/**
          service-id: feign
    ```

  - **ZuulApplication**

    ```java
    package cn.dninth;
    
    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;
    import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
    import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
    
    @SpringBootApplication
    @EnableEurekaClient
    @EnableZuulProxy
    public class ZuulApplication {
        public static void main(String[] args) {
            SpringApplication.run(ZuulApplication.class,args);
        }
    }
    ```

  依次启动所有的模块，启动完成后，在浏览器地址栏输入  http://localhost:8020/api-a/hi?name=tom，调用 ribbon ，输入  http://localhost:8020/api-b/hi?name=tom，调用 Feign ，并且会调用对应的方法。