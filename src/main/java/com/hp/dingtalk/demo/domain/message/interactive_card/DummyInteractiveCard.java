package com.hp.dingtalk.demo.domain.message.interactive_card;

import com.hp.dingtalk.pojo.message.interactive.AbstractDingInteractiveMsg;
import com.hp.dingtalk.pojo.message.interactive.callback.IDingInteractiveCardCallBack;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author hp 2023/3/17
 */
@Accessors(chain = true)
@Getter
@Setter
public class DummyInteractiveCard extends AbstractDingInteractiveMsg {

    /**
     * 模版属性
     */
    private String dummyName;
    private String otherInformation;
    private int button;


    /**
     * 模版id可以写死或者暂时自己实现动态配置
     */
    public DummyInteractiveCard(IDingInteractiveCardCallBack callBack, String outTrackId) {
        // TODO change the templateId parameter to your own template id that you previously created on the DingTalk interactive card platform.
        super(callBack, "fd0c7a4b-b41c-4ece-9798-bfc0c2693731.schema", outTrackId);
    }
}
