package com.banne.template.model.dto;

import lombok.Data;

@Data
public class PageRoleQueryRequest {

        private  String roleName;

        private  int page;

        private  int pageSize;

}
