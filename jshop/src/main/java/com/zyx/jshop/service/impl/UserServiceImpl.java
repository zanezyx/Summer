package com.zyx.jshop.service.impl;


import com.zyx.jshop.entity.User;
import com.zyx.jshop.mapper.UserMapper;
import com.zyx.jshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName cn.saytime.service.impl.UserServiceImpl
 * @Description
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUserById(Integer id) {
        return userMapper.getUserById(id);
    }

    @Override
    public User getUserByMobileAndPwd(String mobile, String password) {
        return userMapper.getUserByMobileAndPwd(mobile, password);
    }

    @Override
    public List<User> getUserList() {
        return userMapper.getUserList();
    }

    @Override
    public int add(User user) {
        return userMapper.add(user);
    }

    @Override
    public int update(Integer id, User user) {
        return userMapper.update(id, user);
    }

    @Override
    public int updatePassword(Integer id, String password) {
        return userMapper.updatePassword(id, password);
    }

    @Override
    public int delete(Integer id) {
        return userMapper.delete(id);
    }
}