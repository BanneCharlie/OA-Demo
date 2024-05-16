package com.banne.template.model.vo;

import lombok.Data;

import java.util.List;

@Data

public class MenuVO {

    private Long id;

    // 菜单名称
    private String title;

    // 菜单名称 组件名称
    private String name;

    // 具有的子菜单
    private List<MenuVO> children;
}
