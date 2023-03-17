package com.hp.dingtalk.demo.domain.message.interactive_card;

import com.hp.dingding.component.application.IDingBot;
import com.hp.dingding.pojo.message.interactive.callback.AbstractDingInteractiveCardCallback;

import java.util.List;

/**
 * @author hp 2023/3/17
 */
public class DummyInteractiveCardCallback extends AbstractDingInteractiveCardCallback {

    public DummyInteractiveCardCallback(String callbackRouteKey, String callbackUrl, List<Class<? extends IDingBot>> dingBots) {
        super(callbackRouteKey, callbackUrl, dingBots);
    }
}
