package com.libsept24.limit.service.impl;


import com.libsept24.limit.entity.User;
import com.libsept24.limit.mapper.UserMapper;
import com.libsept24.limit.service.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private UserMapper userMapper;

    public int insertUserInfo(User user){
        return userMapper.insert(user);
    }

    public List<User> queryUserInfos(){
        return userMapper.selectList(null);
    }


}
