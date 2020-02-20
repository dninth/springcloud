### SpringCloudDemo

- 创建一个空的 project（maven） 项目 **springcloud**，不需要任何依赖。

#### EurekaServer（注册中心）

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
  

#### EurekaClient（服务提供者）

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
    @RequestMapping("/clientHi")
    public class ClientPortController {
    
        @Value("${server.port}")
        private String port;
    
        @Value("${spring.application.name}")
        private String name;
    
        @GetMapping("/getPort")
        public String getPort() {
            return "我是" + name + "端口号是：" + port;
        }
    }
    ```

  先启动 注册中心（**EurekaApplication** ） 接着启动 服务提供者（**ClientApplication**），启动完后 

  进入 http://localhost:8761/ 即可看到服务提供者注册的名字及端口号。

  浏览器网址输入 http://localhost:8010/clientHi/getPort 即可看到 controller 返回的结果。

