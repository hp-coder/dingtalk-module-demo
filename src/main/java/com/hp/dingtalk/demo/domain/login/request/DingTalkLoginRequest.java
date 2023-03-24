package com.hp.dingtalk.demo.domain.login.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @author hp 2023/3/24
 */
@Getter
@Setter
public class DingTalkLoginRequest {
    /**
     * 目前该值确定的是可以获取老版本的钉钉扫码登录临时授权码
     * 场景：
     * 钉钉扫码，确认后，重定向到开发指定的url并携带code和当初自定义的state，页面拿到这两个值后调登录业务
     * <p>
     * 通过code+普通应用accessToken获取unionId，之后根据unionId获取userId
     */
    private String code;

    /**
     * 新版扫码/免登录钉钉临时授权码
     * 场景1：
     * 页面内嵌iframe，没成功，报iframe cross-origin，nginx加请求头没搞定
     * 同上述code场景
     * 场景2：
     * 使用钉钉提供的页面登录，最简单
     * 同上述code场景
     * <p>
     * 通过单独的userToken接口再获取用户信息
     * 通过authCode获取userToken，之后根据userToken获取个人联系人信息，其中包含unionId，之后同上
     */
    private String authCode;

    /**
     * 场景：
     * 通过页面上的钉钉函数调用获取临时授权码，然后直接用这个码做登录
     * <p>
     * 钉钉应用内不需要登录直接点击微应用图标就直接登录，返回的临时授权码
     * 实际上给的也是code
     */
    private String inDingTalkAuthCode;

    /**
     * 曾经在业务里实现为长短码区分扫码或者绑定
     * 这个值在新旧版扫码免登中自定义，与钉钉应用内免登不相关
     */
    private String state;
}
