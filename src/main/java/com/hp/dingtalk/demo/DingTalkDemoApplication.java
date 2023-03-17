package com.hp.dingtalk.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author hp 2023/3/17
 */
@ComponentScan("com.hp")
@SpringBootApplication
public class DingTalkDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DingTalkDemoApplication.class, args);
    }
}
