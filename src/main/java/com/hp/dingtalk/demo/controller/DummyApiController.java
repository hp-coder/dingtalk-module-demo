package com.hp.dingtalk.demo.controller;

import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.google.gson.Gson;
import com.hp.dingtalk.component.factory.app.DingAppFactory;
import com.hp.dingtalk.demo.domain.login.request.DingTalkLoginRequest;
import com.hp.dingtalk.demo.domain.login.service.DingTalkLoginService;
import com.hp.dingtalk.demo.domain.message.interactive_card.DummyInteractiveCard;
import com.hp.dingtalk.demo.domain.robot.TestBot;
import com.hp.dingtalk.pojo.callback.DingInteractiveCardCallBackRequest;
import com.hp.dingtalk.pojo.message.interactive.IDingInteractiveMsg;
import com.hp.dingtalk.pojo.message.interactive.callback.IDingInteractiveCardCallBack;
import com.hp.dingtalk.service.message.DingBotMessageHandler;
import com.hp.dingtalk.service.user.DingUserHandler;
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
    private final DingTalkLoginService dingTalkLoginService;

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
        // TODO change the second parameter to a phone number that exists in your organization.
        final String dingTalkUserId = dingUserHandler.findUserIdByMobile(app, "The phone number that equals the phone number on the DingTalk profile");
        final String outTrackId = dingBotMessageHandler.sendInteractiveMsgToIndividual(app, Collections.singletonList(dingTalkUserId), card);
        LOCAL_CACHE.putIfAbsent(outTrackId, card);
        return "successfully sent, and the outTrackId is: " + outTrackId;
    }

    @PostMapping("/test/callback")
    public void testUpdateInteractiveCardMessage(@RequestBody DingInteractiveCardCallBackRequest request) {
        log.info("callback payload: {}", new Gson().toJson(request));
        final String outTrackId = request.getOutTrackId();
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


    @PostMapping("/test/login")
    public String login(@RequestBody DingTalkLoginRequest request) {
        final OapiV2UserGetResponse.UserGetResponse userGetResponse = dingTalkLoginService.queryDingDing(request);
        final String userid = userGetResponse.getUserid();
        /*
            ...后续查询系统用户信息校验等等
         */
        return "token:" + userid;
    }
}
