package com.hp.dingtalk.demo.test;

import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.response.OapiMediaUploadResponse;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.hp.dingtalk.demo.domain.message.interactive_card.DummyInteractiveCard;
import com.hp.dingtalk.component.application.IDingBot;
import com.hp.dingtalk.component.application.IDingMiniH5;
import com.hp.dingtalk.component.factory.app.DingAppFactory;
import com.hp.dingtalk.pojo.file.MediaRequest;
import com.hp.dingtalk.pojo.message.bot.DingTextMsg;
import com.hp.dingtalk.pojo.message.interactive.callback.IDingInteractiveCardCallBack;
import com.hp.dingtalk.pojo.message.worknotify.DingWorkNotifyMsg;
import com.hp.dingtalk.service.IDingBotMessageHandler;
import com.hp.dingtalk.service.IDingInteractiveMessageHandler;
import com.hp.dingtalk.service.IDingUserHandler;
import com.hp.dingtalk.service.IDingWorkNotifyMessageHandler;
import com.hp.dingtalk.service.file.media.DingMediaHandler;
import com.hp.dingtalk.service.file.media.DingMediaType;
import com.hp.dingtalk.service.message.DingBotMessageHandler;
import com.hp.dingtalk.service.message.DingWorkNotifyMessageHandler;
import com.hp.dingtalk.service.user.DingUserHandler;
import com.hp.dingtalk.utils.DingMarkdown;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author hp
 */
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DingTalkTest {
    private IDingMiniH5 app;
    private IDingBot bot;
    private IDingWorkNotifyMessageHandler messageHandler;
    private IDingBotMessageHandler botMessageHandler;
    private IDingInteractiveMessageHandler dingInteractiveMessageHandler;
    private IDingUserHandler userHandler;
    private DingMediaHandler mediaHandler;
    private OapiV2UserGetResponse.UserGetResponse user;
    private OapiMediaUploadResponse imageResponse;

    @Autowired
    private IDingInteractiveCardCallBack dummyInteractiveCardCallback;

    @BeforeAll
    public void init() throws IOException {
        app = DingAppFactory.app(IDingMiniH5.class);
        bot = DingAppFactory.app(IDingBot.class);
        messageHandler = new DingWorkNotifyMessageHandler(app);
        botMessageHandler = new DingBotMessageHandler(bot);
        dingInteractiveMessageHandler = new DingBotMessageHandler(bot);
        userHandler = new DingUserHandler(app);
        mediaHandler = new DingMediaHandler(app);
        user = userHandler.findUserByMobile("18652743467");
        assertThat(user).isNotNull();
        final MediaRequest request = new MediaRequest(DingMediaType.IMAGE, new File("/Users/programmer/Downloads/Logo_HuPeng-70-300x300px.png"));
        imageResponse = mediaHandler.upload(request);
        assertThat(imageResponse).isNotNull();
    }

    @Test
    public void test_send_link_work_notify_using_minih5_app() {
        final OapiMessageCorpconversationAsyncsendV2Request.Link link = new OapiMessageCorpconversationAsyncsendV2Request.Link();
        link.setTitle("测试");
        link.setMessageUrl("https://www.baidu.com/s?wd=1231231");
        link.setText("百度");
        link.setPicUrl(imageResponse.getMediaId());
        final DingWorkNotifyMsg linkMsg = new DingWorkNotifyMsg(link);
        messageHandler.sendWorkNotifyToUsers(Collections.singletonList(user.getUserid()), linkMsg);
    }

    @Test
    public void test_send_text_work_notify_using_minih5_app() {
        final OapiMessageCorpconversationAsyncsendV2Request.Text msg = new OapiMessageCorpconversationAsyncsendV2Request.Text();
        msg.setContent("https://www.baidu.com/s?wd=1231231");
        final DingWorkNotifyMsg linkMsg = new DingWorkNotifyMsg(msg);
        messageHandler.sendWorkNotifyToUsers(Collections.singletonList(user.getUserid()), linkMsg);
    }

    @Test
    public void test_send_image_work_notify_using_minih5_app() {
        final OapiMessageCorpconversationAsyncsendV2Request.Image msg = new OapiMessageCorpconversationAsyncsendV2Request.Image();
        msg.setMediaId(imageResponse.getMediaId());
        final DingWorkNotifyMsg linkMsg = new DingWorkNotifyMsg(msg);
        messageHandler.sendWorkNotifyToUsers(Collections.singletonList(user.getUserid()), linkMsg);
    }

    @Test
    public void test_send_file_work_notify_using_minih5_app() {
        final OapiMessageCorpconversationAsyncsendV2Request.File msg = new OapiMessageCorpconversationAsyncsendV2Request.File();
        msg.setMediaId(imageResponse.getMediaId());
        final DingWorkNotifyMsg linkMsg = new DingWorkNotifyMsg(msg);
        messageHandler.sendWorkNotifyToUsers(Collections.singletonList(user.getUserid()), linkMsg);
    }

    @Test
    public void test_send_oa_work_notify_using_minih5_app() {
        final OapiMessageCorpconversationAsyncsendV2Request.OA msg = new OapiMessageCorpconversationAsyncsendV2Request.OA();
        final DingWorkNotifyMsg linkMsg = new DingWorkNotifyMsg(msg);
        final OapiMessageCorpconversationAsyncsendV2Request.Head head = new OapiMessageCorpconversationAsyncsendV2Request.Head();
        head.setText("OA消息");
        msg.setHead(head);
        final OapiMessageCorpconversationAsyncsendV2Request.Body body = new OapiMessageCorpconversationAsyncsendV2Request.Body();
        body.setTitle("OA");
        body.setContent("发送OA类型消息");
        msg.setBody(body);
        messageHandler.sendWorkNotifyToUsers(Collections.singletonList(user.getUserid()), linkMsg);
    }

    @Test
    public void test_send_markdown_work_notify_using_minih5_app() {
        final OapiMessageCorpconversationAsyncsendV2Request.Markdown msg = new OapiMessageCorpconversationAsyncsendV2Request.Markdown();
        msg.setTitle("markdown消息");
        msg.setText(DingMarkdown.builder().level1Title("mk头").text("content").build());
        final DingWorkNotifyMsg linkMsg = new DingWorkNotifyMsg(msg);
        messageHandler.sendWorkNotifyToUsers(Collections.singletonList(user.getUserid()), linkMsg);
    }

    @Test
    public void test_send_test_message_using_robot_app() {
        final DingTextMsg.SampleText msg = new DingTextMsg.SampleText("https://www.baidu.com/s?wd=1231231");
        botMessageHandler.sendToUserByUserIds(Collections.singletonList(user.getUserid()), msg);
    }

    @Test
    public void test_send_interactive_message_using_robot_app() {
        final DummyInteractiveCard card = new DummyInteractiveCard(dummyInteractiveCardCallback, String.valueOf(System.currentTimeMillis()))
                .setDummyName(" Hello Human ")
                .setOtherInformation(" Some other info ")
                .setButton(1);
        // TODO change the second parameter to a phone number that exists in your organization.
        final String outTrackId = dingInteractiveMessageHandler.sendInteractiveMsgToIndividual(Collections.singletonList(user.getUserid()), card);
        assertThat(outTrackId).isNotEmpty();
    }
}
