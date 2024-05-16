package com.banne.template.model.entity;

import lombok.Data;

/**
 * @author 86188
 */
@Data
public class Depart {
    private Long id;

    private Long parentId;

    private String departmentName;

    private String manager;

    private String description;

    private String createTime;

    private String updateTime;
}
