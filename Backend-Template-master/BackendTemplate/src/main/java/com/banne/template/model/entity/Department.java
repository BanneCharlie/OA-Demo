package com.banne.template.model.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Department {

    private Long id;

    private Long parentId;

    private String departmentName;

    private String manager;

    private String createTime;

    private String updateTime;

    private String description;
}
