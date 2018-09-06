package com.zyx.jshop.service;


import com.zyx.jshop.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName cn.saytime.service.UserService
 * @Description
 */
public interface UserService {

    User getUserById(Integer id);

    User getUserByMobileAndPwd(String mobile, String password);

    public List<User> getUserList();

    public int add(User user);

    public int update(Integer id, User user);

    public int delete(Integer id);

    public int updatePassword(Integer id, String password);
}