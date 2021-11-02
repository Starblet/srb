package com.starblet.srb.oss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author starblet
 * @create 2021-09-06 22:12
 */

@SpringBootApplication
@ComponentScan({"com.starblet.srb","com.starblet.common"})
public class ServiceOssApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceOssApplication.class);
    }
}
