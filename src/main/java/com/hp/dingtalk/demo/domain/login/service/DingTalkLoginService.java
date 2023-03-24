package com.hp.dingtalk.demo.domain.login.service;

import com.aliyun.dingtalkcontact_1_0.models.GetUserResponseBody;
import com.aliyun.dingtalkoauth2_1_0.models.GetUserTokenResponseBody;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.dingtalk.api.response.OapiV2UserGetuserinfoResponse;
import com.hp.dingding.component.application.IDingMiniH5;
import com.hp.dingding.component.factory.DingAppFactory;
import com.hp.dingding.service.IDingLoginHandler;
import com.hp.dingding.service.IDingUserHandler;
import com.hp.dingding.service.contact.DingContactHandler;
import com.hp.dingding.service.login.DingLoginHandler;
import com.hp.dingding.service.user.DingUserHandler;
import com.hp.dingtalk.demo.domain.app.DummyMiniH5App;
import com.hp.dingtalk.demo.domain.login.request.DingTalkLoginRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author hp 2023/3/24
 */
@Slf4j
@Service
public class DingTalkLoginService {

    /**
     * 查询用户详情
     * 这里的示例时直接查询到底，即查询到用户的详情，如果不要钉钉用户的其他信息，在查询到userId后就可以和库的信息做认证和授权了
     *
     * @param request 扫码请求体
     * @return 用户详情
     */
    public OapiV2UserGetResponse.UserGetResponse queryDingDing(DingTalkLoginRequest request) {
        final IDingMiniH5 app = DingAppFactory.app(DummyMiniH5App.class);
        //旧版扫码登录
        if (StringUtils.isNotEmpty(request.getCode())) {
            final IDingUserHandler handler = new DingUserHandler();
            return handler.userByCode(app, request.getCode());
        }
        //钉钉应用内，企业内微应用免登
        else if (StringUtils.isNotEmpty(request.getInDingTalkAuthCode())) {
            final DingUserHandler handler = new DingUserHandler();
            final OapiV2UserGetuserinfoResponse.UserGetByCodeResponse response = handler.userByLoginAuthCode(app, request.getInDingTalkAuthCode());
            return handler.userByUserId(app, response.getUserid());
        }
        //新版免登录
        else {
            //获取用户个人信息，如需获取当前授权人的信息，unionId参数必须传me
            final String me = "me";
            final GetUserTokenResponseBody userToken = new DingLoginHandler().getUserToken(app, request.getAuthCode(), IDingLoginHandler.GrantType.authorization_code);
            final GetUserResponseBody contactUserResponse = new DingContactHandler().personalContactInfo(userToken.getAccessToken(), me);
            final String unionId = contactUserResponse.getUnionId();
            final IDingUserHandler handler = new DingUserHandler();
            final String userId = handler.userIdByUnionId(app, unionId);
            return handler.userByUserId(app, userId);
        }
    }
}
