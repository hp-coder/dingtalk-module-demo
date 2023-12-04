业务逻辑

1. 项目启动时通过`dingtalk-module`中的DingBooter注册互动卡片的回调地址;
   1. IDingInteractiveCardCallBack.class 为活动卡片回调地址抽象, 实例需注册到容器; 见配置类: [DingConfig.java](..%2F..%2F..%2Finfrastructure%2Fconfig%2FDingConfig.java)
   2. 项目启动时会根据spring事件执行注册回调地址业务;
2. [DummyApiController](..%2F..%2F..%2Fcontroller%2FDummyApiController.java).testSendInteractiveCardMessage 发送互动卡片
   1. 其中 IDingBot.class 为钉钉机器人应用抽象, 需要注册到容器; 见配置类: [DingConfig.java](..%2F..%2F..%2Finfrastructure%2Fconfig%2FDingConfig.java)
   2. IDingInteractiveMessageHandler.class 即发送卡片业务;
   3. IDingUserHandler.class 查询钉钉用户信息业务;
   4. AbstractDingInteractiveMsg.class 为通用的互动卡片抽象, 实现类只需关注自己卡片的属性即可;
3. 发送卡片后获取自定义的卡片`实例id(outTrackId)`
4. 如果需要更新卡片数据, 可以通过上述的实例id更新;
   1. 见: [DummyApiController](..%2F..%2F..%2Fcontroller%2FDummyApiController.java).testUpdateInteractiveCardMessage
