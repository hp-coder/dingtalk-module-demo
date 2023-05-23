package com.hp.dingtalk.demo.domain.robot;

import com.hp.dingtalk.component.application.IDingBot;
import lombok.Getter;

/**
 * @author hp 2023/3/17
 */
@Getter
public class TestBot implements IDingBot {

    private final String appName;
    private final Long appId;
    private final String appKey;
    private final String appSecret;

    public TestBot(String appName, Long appId, String appKey, String appSecret) {
        this.appName = appName;
        this.appId = appId;
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.afterSingletonsInstantiated();
    }
}
