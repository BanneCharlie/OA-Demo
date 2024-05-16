package com.banne.template.model.dto;

import lombok.Data;

@Data
public class PageMenuQueryRequest {

        private  String menuName;

        private  int page;

        private  int pageSize;
}
