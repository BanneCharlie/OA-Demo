package com.banne.template.model.dto;

import lombok.Data;

@Data
public class PageQueryRequest {

    private  String userName;

    private  String userAccount;

    private  String phone;

    private String createTimeBegin;

    private String createTimeEnd;

    private Integer status;

    private String  departmentName;

    private  int page;

    private  int pageSize;

}
