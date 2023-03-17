package com.hp.dingtalk.demo.domain.message.interactive_card;

import com.hp.dingding.pojo.message.interactive.AbstractDingInteractiveMsg;
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
     * 模版id可以写死或者通过暂时自己实现动态配置
     */
    public DummyInteractiveCard(String callbackRouteKey, String outTrackId) {
        super(callbackRouteKey, outTrackId, "your interactive card template id");
    }
}
