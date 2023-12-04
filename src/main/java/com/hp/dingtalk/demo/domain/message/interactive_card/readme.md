业务逻辑

1. 项目启动时通过`dingtalk-module`中的DingBooter注册互动卡片的回调地址;
2. [DummyApiController](..%2F..%2F..%2Fcontroller%2FDummyApiController.java).testSendInteractiveCardMessage 发送互动卡片
   1. 其中 IDingBot.class 为钉钉机器人应用抽象, 需要注册到容器; 见配置类: [DingConfig.java](..%2F..%2F..%2Finfrastructure%2Fconfig%2FDingConfig.java)
   2. IDingInteractiveMessageHandler.class 即发送卡片业务;
   3. IDingUserHandler.class 查询钉钉用户信息业务;
3. 发送卡片后获取自定义的卡片`实例id(outTrackId)`
4. 如果需要更新卡片数据, 可以通过上述的实例id更新;
   1. 见: [DummyApiController](..%2F..%2F..%2Fcontroller%2FDummyApiController.java).testUpdateInteractiveCardMessage
