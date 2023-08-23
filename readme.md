# Prerequisite

## Dependencies
Please install these three projects to your local Maven repository before using this demo project.
```shell
# Install them in the given order
https://github.com/hp-coder/common-bom.git
https://github.com/hp-coder/common-base.git
https://github.com/hp-coder/dingtalk-module.git
```

# Demo

## 机器人发送高级互动卡片

针对 [hp-coder/dingtalk-module](https://github.com/hp-coder/dingtalk-module) 中机器人发送互动卡片的一次简单实现

**说明**

直接启动项目会失败，使用IDE查看该项目TODO以了解如何配置，完成配置后可以正常启动，much appreciate.

使用此demo时，请将上述模块的代码更新到最新版，有时候模块没发新版本容易出问题

以下配置值需要改为自己的

- DummyApiController中的接收消息的**钉钉用户的钉钉电话号码**
- DingConfig
    - testBot中的配置值
    - dummyInteractiveCardCallback中的配置值


DummyInteractiveCard中的模板id应为自己创建的卡片模板id；[钉钉卡片后台](https://open-dev.dingtalk.com/fe/card?spm=ding_open_doc.document.0.0.5296640cuD3BXA#/)

## 钉钉登录

### 钉钉后台配置

企业内应用-具体应用详情-基础信息-开发管理

- 应用首页地址配置为 `http://ip:port`
- PC端首页地址配置为 `http://ip:port`

企业内应用-具体应用详情-应用功能-登录与分享

- 微应用回调的URL配置 `http://ip:port`
    - 说明：文档中提到这里的会往这个url发送authcode，此前实现的项目基于ruoyi，以及另一个thymeleaf+vue2的项目都是可以按上述这么配置

### 前端

个人前端懂的不多，刚能实现，写前端的随便看下就知道怎么做了，[钉钉JSAPI文档](https://open.dingtalk.com/document/isvapp/read-before-development)
```shell
npm install dingtalk-jsapi --save
```
或者直接引入
```html
<script src="https://g.alicdn.com/dingding/dingtalk-jsapi/3.0.12/dingtalk.open.js"></script>
```

登录js方法
```js
export function ddlogin(code, authCode, inDingTalkAuthCode, state, uuid) {
  const data = {
    state,
    code,
    authCode,
    inDingTalkAuthCode,
    uuid
  }
  return request({
    url: '/dummy/test/login',
    headers: {
      isToken: false
    },
    method: 'post',
    data: data
  })
}
```
```js
const user = {
    actions: {
        DDLogin({commit}, userInfo) {
            return new Promise((resolve, reject) => {
                ddlogin(userInfo.code, userInfo.authCode, userInfo.inDingTalkAuthCode, userInfo.state, userInfo.uuid)
                    .then((res) => {
                        setToken(res.token);
                        commit("SET_TOKEN", res.token);
                        resolve();
                    })
                    .catch((error) => {
                        reject(error);
                    });
            });
        },
    }
}
```

登录页需要的依赖
```html
<!--旧版的钉钉扫码登录，只扫码-->
<script src="https://g.alicdn.com/dingding/dinglogin/0.0.5/ddLogin.js"></script>
<!--新版钉钉免登录，支持扫码和调起钉钉应用免登-->
<script src="https://g.alicdn.com/dingding/h5-dingtalk-login/0.21.0/ddlogin.js"></script>
```

登录页的实现
```vue
<template>
  <div>
    <!--  .... 省去其他业务表单-->

    <!--渲染钉钉登录组件的部分，新版会在这里插一个跨域的iframe，导致报错，没有显示调起已登录钉钉应用的组件，可能是这个原因，二维码能正确显示，扫码也可以执行-->
    <div id="login_container" class="login_container_class"></div>

    <!--  .... 省去其他业务表单-->
  </div>
</template>
<script>
import * as dd from "dingtalk-jsapi";

export default {
  name: "Login",
  data() {
    return {
      appKey:"your app key",
      dingRedirectUrl: encodeURIComponent("your login page uri"),
      corpId: "your corp id", 
      dingTalkLoginPage: true
    }
  },
  mounted() {
    //如果用账号密码 走其他流程z

    //钉钉登录
    const {code, authCode, state} = this.$route.query;
    //如果是钉钉登录重定向带参数
    if ((code || authCode) && state) {
      //登录接口
      this.handleCodeLogin(code, authCode, null, state);
    } else {
      //如果是钉钉App内工作台进入应用免登
      //这个方法在浏览器由于缺少钉钉环境，组件报错无法执行
      //如果纯pc浏览器开发测试巨麻烦
      if (window.navigator.userAgent.includes("DingTalk")) {
        this.initInDingTalkLogin();
      }
      //普通浏览器场景
      else {
        //如果为重定向方式
        if (this.dingTalkLoginPage) {
          this.redirectToDingTalkLoginPage()
        }
        // TODO 内嵌，iframe有限制，哪个牛b的能解决吗？内嵌的帅很多 :)
        else {
          console.log(" 内嵌，iframe有限制，")
          this.initDingLoginV2();
        }
      }
    }
  },
  methods: {
    //以下的 dd.xxxxx方法参考钉钉文档JSAPI开发部分
    handleCodeLogin(code, authCode, inDingTalkAuthCode, state) {
      const loading = this.$loading({
        lock: true,
        text: "正在登录",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
      });
      //参考ruoyi账号密码登录再写一个差不多的
      this.$store
          .dispatch("DDLogin", {code, authCode, inDingTalkAuthCode, state, uuid: state})
          .then((rsp) => {
            loading.close();
            this.$router.push({path: this.redirect || "/"}).catch(() => {
            });
          })
          .catch(() => {
            this.loading = false;
            if (this.captchaOnOff) {
              this.getCode();
            }
            this.$router.push({path: this.redirect || "/"}).catch(() => {
            });
          });
    },
    // 钉钉应用内免登
    initInDingTalkLogin() {
      var _that = this;
      dd.ready(function () {
        dd.runtime.permission.requestAuthCode({
          corpId: _that.corpId, // 企业id
          onSuccess: function (info) {
            let code = info.code // 通过该免登授权码可以获取用户身份
            let state = _that._getRandomString(10);
            _that.handleCodeLogin(null, null, code, state);
          },
          onFail: function (msg) {
            dd.device.notification.toast({
              icon: 'error', //icon样式，不同客户端参数不同，请参考参数说明
              text: `${msg}`, //提示信息
              duration: 3, //显示持续时间，单位秒，默认按系统规范[android只有两种(<=2s >2s)]
              delay: 0, //延迟显示，单位秒，默认0
              onSuccess: function (result) {
              }
            })
          }
        });
      });
    },
    // 使用直接跳转到钉钉提供的登录页的方式
    redirectToDingTalkLoginPage() {
      let url = this.dingRedirectUrl
      let state = this._getRandomString(10);
      window.location.href = "https://login.dingtalk.com/oauth2/auth?" +
          // 授权通过/拒绝后回调地址。
          "redirect_uri=" + url +
          // 固定值为code。授权通过后返回authCode。
          "&response_type=code" +
          // 企业内部应用：client_id为应用的AppKey。
          "&client_id=" + this.appKey +
          // 授权范围，授权页面显示的授权信息以应用注册时配置的为准。当前只支持两种输入：
          // openid：授权后可获得用户userid
          // openid corpid：授权后可获得用户id和登录过程中用户选择的组织id，空格分隔。注意url编码。
          "&scope=openid" +
          // 跟随authCode原样返回
          "&state=" + state +
          // 值为consent时，会进入授权确认页。
          "&prompt=consent";
      // 其他参数参考：https://open.dingtalk.com/document/isvapp/tutorial-enabling-login-to-third-party-websites
    },
    //新版方式，可以调起钉钉应用，内嵌
    initDingLoginV2() {
      let url = this.dingRedirectUrl
      window.DTFrameLogin(
          {
            id: 'login_container',
            width: 350,
            height: 350,
          },
          {
            redirect_uri: url,
            client_id: this.appKey,
            scope: 'openid',
            response_type: 'code',
            state: this._getRandomString(10),
            prompt: 'consent',
          },
          (loginResult) => {
            const {redirectUrl, authCode, state} = loginResult;
            // 这里可以直接进行重定向
            console.log("redirectUrl ", redirectUrl)
            window.location.href = redirectUrl;
            // 也可以在不跳转页面的情况下，使用code进行授权
            console.log("authCode ", authCode);
          },
          (errorMsg) => {
            // 这里一般需要展示登录失败的具体原因
            alert(`登录异常: ${errorMsg}`);
          },
      );
    },
    //旧版方式，钉钉不再维护，内嵌
    initDingLogin() {
      var url = this.dingRedirectUrl
      var state = this._getRandomString(10);
      var gotoUrl = encodeURIComponent(
          "https://oapi.dingtalk.com/connect/oauth2/sns_authorize?" +
          "appid=" + this.appKey +
          "&response_type=code" +
          "&scope=snsapi_login" +
          "&state=" + state +
          "&redirect_uri=" + url
      );
      var obj = DDLogin({
        id: "login_container",
        goto: gotoUrl,
        style: "border:none;background-color:#FFFFFF;",
        width: "350",
        height: "350",
      });
      var handleMessage = function (event) {
        var origin = event.origin;
        if (origin === "https://login.dingtalk.com") {
          var loginTmpCode = event.data;
          window.location.href =
              "https://oapi.dingtalk.com/connect/oauth2/sns_authorize?appid=dingphiifwpkj8476jgq&response_type=code&scope=snsapi_login&state=" +
              state +
              "&redirect_uri=" +
              url +
              "&loginTmpCode=" +
              loginTmpCode;
        }
      };
      if (typeof window.addEventListener != "undefined") {
        window.addEventListener("message", handleMessage, false);
      } else if (typeof window.attachEvent != "undefined") {
        window.attachEvent("onmessage", handleMessage);
      }
    },
    _getRandomString(len) {
      len = len || 10;
      let $chars = "ABCDEFGHIJKMNOPQRSTUVWXYZ"; // 默认去掉了容易混淆的字符oOLl,9gq,Vv,Uu,I1
      let maxPos = $chars.length;
      let pwd = "";
      for (var i = 0; i < len; i++) {
        pwd += $chars.charAt(Math.floor(Math.random() * maxPos));
      }
      return pwd;
    }
  }
}
</script>

```
