package com.hp.dingtalk.demo.infrastructure.model;

import com.hp.common.base.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

/**
 * 针对如果系统内存在自建架构(部门-人员),以该状态做区分
 *
 * @author hp
 */
@Getter
@AllArgsConstructor
public enum DingTalkCascadePickerSource implements BaseEnum<DingTalkCascadePickerSource, String> {
    /***/
    DING_TALK("dingtalk", "钉钉组织架构"),
    SYSTEM("system", "系统组织架构"),
    ;
    private final String code;
    private final String name;

    public static Optional<DingTalkCascadePickerSource> of(String code) {
        return Optional.ofNullable(BaseEnum.parseByCode(DingTalkCascadePickerSource.class, code));
    }
}
