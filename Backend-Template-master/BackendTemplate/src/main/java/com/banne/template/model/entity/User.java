package com.banne.template.model.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 附件id
     */
    private Long documentId;

    // 部门的 id
    private Long  departmentId;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

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