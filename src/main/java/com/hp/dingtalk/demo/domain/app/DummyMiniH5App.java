package com.hp.dingtalk.demo.domain.app;

import com.hp.dingding.component.application.IDingMiniH5;
import lombok.Getter;

/**
 * @author hp 2023/3/24
 */
@Getter
public class DummyMiniH5App implements IDingMiniH5 {
    private final String appName;
    private final Long appId;
    private final String appKey;
    private final String appSecret;

    public DummyMiniH5App(String appName, Long appId, String appKey, String appSecret) {
        this.appName = appName;
        this.appId = appId;
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.afterSingletonsInstantiated();
    }
}
