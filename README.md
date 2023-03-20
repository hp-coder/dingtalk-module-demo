针对 [hp-coder/dingtalk-module](https://github.com/hp-coder/dingtalk-module) 中机器人发送互动卡片的一次简单实现

**说明**

直接启动项目会失败，使用IDE查看该项目TODO以了解如何配置，完成配置后可以正常启动，much appreciate.

使用此demo时，请将上述模块的代码更新到最新版，有时候模块没发新版本容易出问题

以下配置值需要改为自己的

- DummyApiController中的接收消息的**钉钉用户的钉钉电话号码**
- DingConfig
  - testBot中的配置值
  - dummyInteractiveCardCallback中的配置值
- DummyInteractiveCard中的模板id应为自己创建的卡片模板id；[钉钉卡片后台](https://open-dev.dingtalk.com/fe/card?spm=ding_open_doc.document.0.0.5296640cuD3BXA#/)
