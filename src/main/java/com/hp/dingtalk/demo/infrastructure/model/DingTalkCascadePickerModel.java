package com.hp.dingtalk.demo.infrastructure.model;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.hp.common.base.annotations.FieldDesc;
import com.hp.common.base.annotations.MethodDesc;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

/**
 * 类似钉钉的企业组织架构节点对象, 已经实现过业务, 可以用
 * <p>
 * 请使用builder方式创建节点以避免数据问题
 * <p>
 * Note:
 * <p>
 * - 部分字段为json序列化做了妥协
 * <p>
 * - 拼音相关字段主要是用来做基于内存的拼音查询
 *
 * @author hp 2023/4/26
 */
@Getter
@Setter
public class DingTalkCascadePickerModel {

    @FieldDesc("用于区分来源(系统/钉钉)")
    private String source;
    @FieldDesc("钉钉用户id/部门id/系统用户id等")
    private String id;
    @FieldDesc("系统用户id,冗余保存方便后期直接获取")
    private String systemUserId;
    @FieldDesc("节点名称")
    private String name;
    @FieldDesc("头像")
    private String avatar;
    @FieldDesc("节点类型:用于区分企业/部门/用户")
    private String type;
    @FieldDesc("职称,用于用户节点展示(CEO/CTO)")
    private String title;
    @FieldDesc("子节点")
    private List<DingTalkCascadePickerModel> children;

    @FieldDesc("前端属性:节点是否选中")
    private boolean selected = false;
    @FieldDesc("前端属性:节点是否展示")
    private boolean show = true;

    @FieldDesc("name字段的拼音全拼")
    @JsonIgnore
    private String nameToPinyin;
    @FieldDesc("通过pinyin4j获取的每个字的拼音, 有多音字的情况(不100%准)")
    @JsonIgnore
    private Set<String> pinyin;
    @FieldDesc("通过pinyin4j获取的首字母")
    @JsonIgnore
    private Set<String> shortPinyin;

    public DingTalkCascadePickerModel() {
        this.children = Lists.newArrayList();
    }

    private DingTalkCascadePickerModel(String source, String id, String name, String avatar, DingTalkCascadePickerType type, String title) {
        this.source = source;
        this.id = id;
        this.name = name;
        if (StrUtil.isEmpty(avatar)) {
            this.avatar = type.getAvatar();
        } else {
            this.avatar = avatar;
        }
        this.type = type.getCode();
        this.title = title;
        this.children = Lists.newArrayList();
    }

    public static class UserBuilder {
        private String id;
        private String systemUserId;
        private String name;
        private String avatar;
        private String title;
        private String nameToPinyin;
        private Set<String> pinyin;
        private Set<String> shortPinyin;

        private DingTalkCascadePickerSource source;

        public UserBuilder id(String id) {
            this.id = id;
            return this;
        }

        public UserBuilder systemUserId(String systemUserId) {
            this.systemUserId = systemUserId;
            return this;
        }

        public UserBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserBuilder avatar(String avatar) {
            this.avatar = avatar;
            return this;
        }

        public UserBuilder title(String title) {
            this.title = title;
            return this;
        }

        public UserBuilder source(DingTalkCascadePickerSource source) {
            this.source = source;
            return this;
        }

        public UserBuilder nameToPinyin(String nameToPinyin) {
            this.nameToPinyin = nameToPinyin;
            return this;
        }

        public UserBuilder pinyin(Set<String> pinyin) {
            this.pinyin = pinyin;
            return this;
        }

        public UserBuilder shortPinyin(Set<String> shortPinyin) {
            this.shortPinyin = shortPinyin;
            return this;
        }

        public DingTalkCascadePickerModel build() {
            final DingTalkCascadePickerModel model = new DingTalkCascadePickerModel(source.getCode(), id, name, avatar, DingTalkCascadePickerType.USER, title);
            model.setNameToPinyin(nameToPinyin);
            model.setPinyin(pinyin);
            model.setShortPinyin(shortPinyin);
            model.setSystemUserId(systemUserId);
            return model;
        }
    }

    public static class DeptBuilder {
        private String id;
        private String name;
        private DingTalkCascadePickerSource source;

        public DeptBuilder id(String id) {
            this.id = id;
            return this;
        }

        public DeptBuilder name(String name) {
            this.name = name;
            return this;
        }

        public DeptBuilder source(DingTalkCascadePickerSource source) {
            this.source = source;
            return this;
        }

        public DingTalkCascadePickerModel build() {
            return new DingTalkCascadePickerModel(source.getCode(), id, name, null, DingTalkCascadePickerType.DEPT, null);
        }
    }

    public static class CorpBuilder {
        private String id;
        private String name;
        private String avatar;
        private DingTalkCascadePickerSource source;

        public CorpBuilder id(String id) {
            this.id = id;
            return this;
        }

        public CorpBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CorpBuilder avatar(String avatar) {
            this.avatar = avatar;
            return this;
        }

        public CorpBuilder source(DingTalkCascadePickerSource source) {
            this.source = source;
            return this;
        }

        public DingTalkCascadePickerModel build() {
            return new DingTalkCascadePickerModel(source.getCode(), id, name, avatar, DingTalkCascadePickerType.CORP, null);
        }
    }

    @MethodDesc("注意:使用的是已有的集合对象")
    public DingTalkCascadePickerModel setChildren(List<DingTalkCascadePickerModel> children) {
        if (CollUtil.isNotEmpty(children)) {
            this.children.addAll(children);
        }
        return this;
    }

    public DingTalkCascadePickerModel clearChildren() {
        this.children = Lists.newArrayList();
        return this;
    }

    public boolean hasChildren() {
        return CollUtil.isNotEmpty(children);
    }

    public boolean hasUsers() {
        return hasChildren() && children.stream().anyMatch(i -> i.getType().equals(DingTalkCascadePickerType.USER.getCode()));
    }

    public boolean hasDepts() {
        return hasChildren() && children.stream().anyMatch(i -> i.getType().equals(DingTalkCascadePickerType.DEPT.getCode()));

    }
}
