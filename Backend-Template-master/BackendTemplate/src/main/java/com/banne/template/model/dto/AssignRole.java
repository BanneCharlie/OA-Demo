package com.banne.template.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AssignRole {

    private Long userId;

    private List<Long> roleIdList;
}
