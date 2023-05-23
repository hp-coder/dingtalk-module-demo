package com.hp.dingtalk.demo.domain.message.interactive_card;

import com.hp.dingtalk.component.application.IDingBot;
import com.hp.dingtalk.pojo.message.interactive.callback.AbstractDingInteractiveCardCallback;

import java.util.List;

/**
 * @author hp 2023/3/17
 */
public class DummyInteractiveCardCallback extends AbstractDingInteractiveCardCallback {

    public DummyInteractiveCardCallback(String callbackRouteKey, String callbackUrl, List<Class<? extends IDingBot>> dingBots) {
        super(callbackRouteKey, callbackUrl, dingBots);
    }
}
