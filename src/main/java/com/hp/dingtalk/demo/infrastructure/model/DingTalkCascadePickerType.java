package com.hp.dingtalk.demo.infrastructure.model;

import com.hp.common.base.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

/**
 * @author hp
 */
@Getter
@AllArgsConstructor
public enum DingTalkCascadePickerType implements BaseEnum<DingTalkCascadePickerType, String> {
    /**
     * For users, I recommend using the javascript to create the avatars on the fly.
     * See {@link genAvatar.js}
     * */
    USER("user","用户",""),
    DEPT("dept","部门","replaced with your customized avatar"),
    CORP("corp","企业","replaced with your customized avatar"),
    ;
    private final String code;
    private final String name;
    private final String avatar;

    public static Optional<DingTalkCascadePickerType> of(String code) {
        return Optional.ofNullable(BaseEnum.parseByCode(DingTalkCascadePickerType.class, code));
    }
}
