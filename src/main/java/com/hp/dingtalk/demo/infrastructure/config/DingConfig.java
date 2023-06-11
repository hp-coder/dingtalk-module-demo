package com.hp.dingtalk.demo.infrastructure.config;

import com.hp.dingtalk.component.application.IDingBot;
import com.hp.dingtalk.component.application.IDingMiniH5;
import com.hp.dingtalk.pojo.message.interactive.callback.IDingInteractiveCardCallBack;
import com.hp.dingtalk.demo.domain.app.DummyMiniH5App;
import com.hp.dingtalk.demo.domain.message.interactive_card.DummyInteractiveCardCallback;
import com.hp.dingtalk.demo.domain.robot.TestBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

/**
 * 配置类
 * 或者更简化使用匿名内部类来注册bean，这里为了方便理解，还是通过完整实现类注册
 *
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
    public IDingBot testBot() {
        // TODO change the constructor parameters to your own DingTalk application info. app name is for readability.
        // Note that the app name is only for logging readability and is fully customizable by you
        return new TestBot("custom bot name", -1L, "your bot key", "your bot secret");
    }

    @Bean
    public IDingMiniH5 dummyMiniH5App() {
        // TODO change the constructor parameters to your own DingTalk application info. app name is for readability.
        return new DummyMiniH5App("custom app name", -1L, "your app key", "your app secret");
    }

    /**
     * 如果要做环境隔离，使用此方式配置
     * 否则硬编码+component或者通过configurationProperties自动配置也行
     */
    @Bean
    public IDingInteractiveCardCallBack dummyInteractiveCardCallback() {
        // TODO change the constructor parameters to your own custom configuration.
        // Note that the third parameter, robots on your DingTalk, indicates that those robots will be used to register callback url when the system is ready.
        return new DummyInteractiveCardCallback("your callback route key(no space)", "[a site that can be accessed on the internet]/dummy/test/callback", Collections.singletonList(TestBot.class));
    }
}
