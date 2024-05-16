package com.banne.template.model.vo;

import lombok.Data;

@Data
public class UserVO {
    /**
     * id
     */
    private Long id;

    // 部门的 名称
    private String  departmentName;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户手机
     */
    private String phone;

    /**
     * 用户描述
     */
    private String description;

    /**
     * 用户状态
     */
    private Integer status;

    /**
     * 用户创建时间
     */
    private String createTime;

    /**
     * 用户更新时间
     */
    private String updateTime;
}
