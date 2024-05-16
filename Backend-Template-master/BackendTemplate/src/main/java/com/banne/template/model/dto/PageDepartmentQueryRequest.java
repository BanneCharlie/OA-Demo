package com.banne.template.model.dto;

import lombok.Data;

@Data
public class PageDepartmentQueryRequest {

        private  String departmentName;

        private  String manager;

        private  String createTimeBegin;

        private  String createTimeEnd;

        private  int page;

        private  int pageSize;
}
