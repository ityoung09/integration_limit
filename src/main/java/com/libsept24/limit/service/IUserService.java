package com.libsept24.limit.service;

import com.libsept24.limit.entity.User;

import java.util.List;

public interface IUserService {

    int insertUserInfo(User user);

    List<User> queryUserInfos();
}
