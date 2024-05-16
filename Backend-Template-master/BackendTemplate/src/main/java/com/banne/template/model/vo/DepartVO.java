package com.banne.template.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class DepartVO {
    private Long id;

    private Long parentId;

    private String departmentName;

    private String manager;

    private String description;

    private String createTime;

    private String updateTime;

    private List<DepartVO> children;
}
