package com.hp.dingtalk.demo.infrastructure.config;

import com.hp.dingding.component.application.IDingBot;
import com.hp.dingding.pojo.message.interactive.callback.IDingInteractiveCardCallBack;
import com.hp.dingtalk.demo.domain.message.interactive_card.DummyInteractiveCardCallback;
import com.hp.dingtalk.demo.domain.robot.TestBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

/**
 * 配置类
 * @author hp 2023/3/17
 */
@Configuration
public class DingConfig {

    /**
     * 如果要做环境隔离，使用此方式配置
     * 否则硬编码+component或者通过configurationProperties自动配置也行
     * 最终目的是将应用注册到工厂中，后续accessToken和接口调用
     */
    @Bean
    public IDingBot testBot(){
        return new TestBot("custom app name",16757015L,"your app key","your app secret");
    }

    /**
     * 如果要做环境隔离，使用此方式配置
     * 否则硬编码+component或者通过configurationProperties自动配置也行
     */
    @Bean
    public IDingInteractiveCardCallBack dummyInteractiveCardCallback(){
        return new DummyInteractiveCardCallback("your callback route key(no space)", "http://sdavw8.natappfree.cc/dummy/test/callback", Collections.singletonList(TestBot.class));
    }
}
