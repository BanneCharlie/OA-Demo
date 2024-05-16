package com.banne.template.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class MenuListVO {

    private Long id;
    // 父级列表
    private Long parentId;

    // 菜单标题
    private String title;

    // 组件名称
    private String component;

    private String createTime;

    // 下级列表  ToDo 注意这里的变化
    private List<MenuListVO> children;
}
