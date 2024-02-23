package com.libsept24.limit.entity;

import lombok.Data;

@Data
public class User extends BaseEntity {

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 地址
     */
    private String address;

    /**
     * 手机号码
     */
    private String phoneno;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户类型
     */
    private Integer type;

    /**
     * 用户状态
     */
    private Integer status;

    /**
     * 激活码
     */
    private String activationCode;

    /**
     * 头像
     */
    private String photoUrl;
}
