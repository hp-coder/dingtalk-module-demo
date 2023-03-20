package com.hp.dingtalk.demo.controller;

import com.google.gson.Gson;
import com.hp.dingding.component.factory.DingAppFactory;
import com.hp.dingding.pojo.bot.DingInteractiveCardCallBackPayload;
import com.hp.dingding.pojo.message.interactive.IDingInteractiveMsg;
import com.hp.dingding.pojo.message.interactive.callback.IDingInteractiveCardCallBack;
import com.hp.dingding.service.message.DingBotMessageHandler;
import com.hp.dingding.service.user.DingUserHandler;
import com.hp.dingtalk.demo.domain.message.interactive_card.DummyInteractiveCard;
import com.hp.dingtalk.demo.domain.robot.TestBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hp 2023/3/17
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/dummy")
public class DummyApiController {

    private final IDingInteractiveCardCallBack dummyInteractiveCardCallback;

    private static final Map<String, IDingInteractiveMsg> LOCAL_CACHE = new HashMap<>(16);

    @PostMapping("/test/interactive-card")
    public String testSendInteractiveCardMessage() {
        final DingBotMessageHandler dingBotMessageHandler = new DingBotMessageHandler();
        final DingUserHandler dingUserHandler = new DingUserHandler();
        final DummyInteractiveCard card = new DummyInteractiveCard(dummyInteractiveCardCallback, String.valueOf(System.currentTimeMillis()))
                .setDummyName(" Hello Human ")
                .setOtherInformation(" Surprise Mother发卡... ")
                .setButton(0);
        final TestBot app = DingAppFactory.app(TestBot.class);
        final String dingTalkUserId = dingUserHandler.findUserIdByMobile(app, "The phone number that equals the phone number on the DingTalk profile");
        final String outTrackId = dingBotMessageHandler.sendInteractiveMsgToIndividual(app, Collections.singletonList(dingTalkUserId), card);
        LOCAL_CACHE.putIfAbsent(outTrackId, card);
        return "successfully sent, and the outTrackId is: " + outTrackId;
    }

    @PostMapping("/test/callback")
    public void testUpdateInteractiveCardMessage(@RequestBody DingInteractiveCardCallBackPayload payload) {
        log.info("callback payload: {}", new Gson().toJson(payload));
        final String outTrackId = payload.getOutTrackId();
        final IDingInteractiveMsg msg = LOCAL_CACHE.get(outTrackId);
        if (!(msg instanceof DummyInteractiveCard)) {
            return;
        }
        ((DummyInteractiveCard) msg)
                .setDummyName("Oops! I got updated")
                .setOtherInformation(" Updated Time: " + LocalDateTime.now())
                .setButton(1);
        // openConversationId 为null 钉钉根据卡片实例id更新卡片
        new DingBotMessageHandler().updateInteractiveMsg(DingAppFactory.app(TestBot.class), null, msg);
    }
}
