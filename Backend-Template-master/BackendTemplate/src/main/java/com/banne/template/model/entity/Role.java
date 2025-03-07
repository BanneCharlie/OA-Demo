package com.banne.template.model.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Role {

    private Long id;

    private String roleName;

    private String createTime;
}
