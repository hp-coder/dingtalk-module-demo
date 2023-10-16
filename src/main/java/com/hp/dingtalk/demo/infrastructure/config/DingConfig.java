package com.hp.dingtalk.demo.infrastructure.config;

import com.hp.dingtalk.component.application.IDingBot;
import com.hp.dingtalk.component.application.IDingMiniH5;
import com.hp.dingtalk.pojo.message.interactive.callback.IDingInteractiveCardCallBack;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

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
        return new IDingBot() {

            @Override
            public String getCorpName() {
                return "your corp name";
            }

            @Override
            public String getCorpId() {
                return "your corp id";
            }

            @Override
            public String getAppName() {
                return "custom bot name";
            }

            @Override
            public String getAppKey() {
                return "dingrabulzsjioosaz9a";
            }

            @Override
            public String getAppSecret() {
                return "Awfl6yODsUClv_OZSgcmxHrvke8XDe9OvNxQ0usKb7LQV0FRGx0kuBsZFLv3KLeZ";
            }

            @Override
            public Long getAppId() {
                return 16757015L;
            }
        };
    }

    @Bean
    public IDingMiniH5 dummyMiniH5App() {
        // TODO change the constructor parameters to your own DingTalk application info. app name is for readability.
        return new IDingMiniH5() {
            @Override
            public String getCorpName() {
                return "your corp name";
            }

            @Override
            public String getCorpId() {
                return "your corp id";
            }

            @Override
            public String getAppName() {
                return "custom bot name";
            }

            @Override
            public String getAppKey() {
                return "dingsqepyhyc4gjblfmk";
            }

            @Override
            public String getAppSecret() {
                return "fG2xCUCkU7CFjMnw4uZCJXEdSyxxf5Ny-yNatWnwfZmP-ky5h_trT5rZW00eicst";
            }

            @Override
            public Long getAppId() {
                return 2513620116L;
            }
        };
    }

    @Bean
    public IDingInteractiveCardCallBack dummyInteractiveCardCallback() {
        // TODO change the constructor parameters to your own custom configuration.
        // Note that the third parameter, robots on your DingTalk, indicates that those robots will be used to register callback url when the system is ready.
        return new IDingInteractiveCardCallBack() {
            @Override
            public String getCallbackUrl() {
                return "https://zwt2d.snunicom.com:9096/strPlatform-web/dingTalkCallBack/callBack";
            }

            @Override
            public String getCallbackRouteKey() {
                return "dummy_callback_key";
            }

            @Override
            public List<Class<? extends IDingBot>> getDingBots() {
                return Collections.singletonList(IDingBot.class);
            }
        };
    }
}
