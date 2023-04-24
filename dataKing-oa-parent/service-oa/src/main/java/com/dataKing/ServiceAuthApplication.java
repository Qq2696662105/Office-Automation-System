package com.dataKing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * ClassName: ServiceAuthApplication
 * Package: com.dataKing.auth
 * Description:springboot启动类
 *
 * @Author dataKing
 * @Create 2023/3/25 0025 22:37
 * @Version 1.0
 */


@SpringBootApplication
public class ServiceAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceAuthApplication.class,args);
    }

}


