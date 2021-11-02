package com.starblet.srb.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author starblet
 * @create 2021-07-28 22:16
 */

@SpringBootApplication
@ComponentScan({"com.starblet.srb","com.starblet.common"})
public class ServiceCoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceCoreApplication.class);
    }
}
