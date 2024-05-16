package com.banne.template.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class AssignMenu {

    private Long id;

    private Long roleId;

    private List<Map<String , Number>> menuIdList;
}
