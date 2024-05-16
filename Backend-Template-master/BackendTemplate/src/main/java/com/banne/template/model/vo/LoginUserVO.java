package com.banne.template.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class LoginUserVO implements Serializable {

/*    *//**
     * 用户 id
     *//*
    private Long id;

    *//**
     * 用户昵称
     *//*
    private String userName;


    private long departmentId;*/

    private String token;

    private String refresh_token;
}
