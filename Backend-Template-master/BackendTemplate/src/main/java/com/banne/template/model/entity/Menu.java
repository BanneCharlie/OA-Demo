package com.banne.template.model.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.util.List;

@Data
@Builder
public class Menu {
    // 菜单id
    private Long id;

    // 父级列表
    private Long parentId;

    // 菜单标题
    private String title;

    // 组件名称
    private String component;

    private String createTime;
}
