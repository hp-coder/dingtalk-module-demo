package com.hp.dingtalk.demo.domain.login.service;

import com.aliyun.dingtalkcontact_1_0.models.GetUserResponseBody;
import com.aliyun.dingtalkoauth2_1_0.models.GetUserTokenResponseBody;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.dingtalk.api.response.OapiV2UserGetuserinfoResponse;
import com.hp.dingtalk.component.application.IDingMiniH5;
import com.hp.dingtalk.component.factory.app.DingAppFactory;
import com.hp.dingtalk.demo.domain.login.request.DingTalkLoginRequest;
import com.hp.dingtalk.service.IDingContactHandler;
import com.hp.dingtalk.service.IDingLoginHandler;
import com.hp.dingtalk.service.IDingUserHandler;
import com.hp.dingtalk.service.contact.DingContactHandler;
import com.hp.dingtalk.service.login.DingLoginHandler;
import com.hp.dingtalk.service.user.DingUserHandler;
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
     * 这里的示例是直接查询到用户的详情，如果不要钉钉用户的其他信息，在查询到userId后就可以和库的信息做认证和授权了
     *
     * @param request 扫码请求体
     * @return 用户详情
     */
    public OapiV2UserGetResponse.UserGetResponse queryDingTalk(DingTalkLoginRequest request) throws Exception {
        final IDingMiniH5 app = DingAppFactory.app(IDingMiniH5.class);
        //旧版扫码登录
        if (StringUtils.isNotEmpty(request.getCode())) {
            final IDingUserHandler handler = new DingUserHandler(app);
            return handler.findUserByCode(request.getCode());
        }
        //钉钉应用内，企业内微应用免登
        else if (StringUtils.isNotEmpty(request.getInDingTalkAuthCode())) {
            final IDingUserHandler handler = new DingUserHandler(app);
            final OapiV2UserGetuserinfoResponse.UserGetByCodeResponse response = handler.findUserByLoginAuthCode(request.getInDingTalkAuthCode());
            return handler.findUserByUserId(response.getUserid());
        }
        //新版免登录
        else {
            //获取用户个人信息，如需获取当前授权人的信息，unionId参数必须传me
            final String me = "me";
            final IDingLoginHandler dingLoginHandler = new DingLoginHandler(app);
            final GetUserTokenResponseBody userToken = dingLoginHandler.getUserToken(request.getAuthCode(), IDingLoginHandler.GrantType.authorization_code);
            final IDingContactHandler dingContactHandler = new DingContactHandler(app);
            final GetUserResponseBody contactUserResponse = dingContactHandler.personalContactInfo(userToken.getAccessToken(), me);
            final String unionId = contactUserResponse.getUnionId();
            final IDingUserHandler handler = new DingUserHandler(app);
            final String userId = handler.findUserIdByUnionId(unionId);
            return handler.findUserByUserId(userId);
        }
    }
}
