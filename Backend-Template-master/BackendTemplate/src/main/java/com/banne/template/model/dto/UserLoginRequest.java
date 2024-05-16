package com.banne.template.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 密码
     */
    private String password;
}
