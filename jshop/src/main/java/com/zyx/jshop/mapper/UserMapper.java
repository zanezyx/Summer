package com.zyx.jshop.mapper;


import com.zyx.jshop.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

// @Mapper 这里可以使用@Mapper注解，但是每个mapper都加注解比较麻烦，所以统一配置@MapperScan在扫描路径在application类中
public interface UserMapper {

    @Select("SELECT * FROM tb_user WHERE id = #{id}")
    User getUserById(Integer id);

    @Select("SELECT * FROM tb_user WHERE mobile = #{mobile} AND password = #{password}")
    User getUserByMobileAndPwd(@Param("mobile") String mobile, @Param("password") String password);

    @Select("SELECT * FROM tb_user")
    public List<User> getUserList();

    @Insert("insert into tb_user(mobile, password) values(#{mobile}, #{password})")
    public int add(User user);

    @Update("UPDATE tb_user SET username = #{user.username} , age = #{user.age} WHERE id = #{id}")
    public int update(@Param("id") Integer id, @Param("user") User user);


    @Update("UPDATE tb_user SET password = #{pwdnew}  WHERE id = #{id}")
    public int updatePassword(@Param("id") Integer id, @Param("pwdnew") String pwdnew);


    @Delete("DELETE from tb_user where id = #{id} ")
    public int delete(Integer id);
}