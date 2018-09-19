package com.zyx.jshop.controller;



import com.zyx.jshop.entity.JsonResult;
import com.zyx.jshop.entity.User;
import com.zyx.jshop.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName cn.saytime.web.UserController
 * @Description
 * @date 2017-07-04 22:46:14
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 根据ID查询用户
     * @param id
     * @return
     */
    @RequestMapping(value = "user/{id}", method = RequestMethod.GET)
    public ResponseEntity<JsonResult> getUserById (@PathVariable(value = "id") Integer id){
        JsonResult r = new JsonResult();
        try {
            User user = userService.getUserById(id);
            r.setResult(user);
            r.setStatus("ok");
        } catch (Exception e) {
            r.setResult(e.getClass().getName() + ":" + e.getMessage());
            r.setStatus("error");
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    /**
     * 查询用户列表
     * @return
     */
    @RequestMapping(value = "users", method = RequestMethod.GET)
    public ResponseEntity<JsonResult> getUserList (){
        JsonResult r = new JsonResult();
        try {
            List<User> users = userService.getUserList();
            r.setResult(users);
            r.setStatus("ok");
        } catch (Exception e) {
            r.setResult(e.getClass().getName() + ":" + e.getMessage());
            r.setStatus("error");
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    /**
     * 添加用户
     * @param user
     * @return
     */
    @RequestMapping(value = "user", method = RequestMethod.POST)
    public ResponseEntity<JsonResult> add (@RequestBody User user){
        JsonResult r = new JsonResult();
        try {
            int orderId = userService.add(user);
            if (orderId < 0) {
                r.setResult(orderId);
                r.setStatus("fail");
            } else {
                r.setResult(orderId);
                r.setStatus("ok");
            }
        } catch (Exception e) {
            r.setResult(e.getClass().getName() + ":" + e.getMessage());
            r.setStatus("error");

            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    /**
     * 根据id删除用户
     * @param id
     * @return
     */
    @RequestMapping(value = "user/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<JsonResult> delete (@PathVariable(value = "id") Integer id){
        JsonResult r = new JsonResult();
        try {
            int ret = userService.delete(id);
            if (ret < 0) {
                r.setResult(ret);
                r.setStatus("fail");
            } else {
                r.setResult(ret);
                r.setStatus("ok");
            }
        } catch (Exception e) {
            r.setResult(e.getClass().getName() + ":" + e.getMessage());
            r.setStatus("error");

            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    /**
     * 根据id修改用户信息
     * @param user
     * @return
     */
    @RequestMapping(value = "user/{id}", method = RequestMethod.PUT)
    public ResponseEntity<JsonResult> update (@PathVariable("id") Integer id, @RequestBody User user){
        JsonResult r = new JsonResult();
        try {
            int ret = userService.update(id, user);
            if (ret < 0) {
                r.setResult(ret);
                r.setStatus("fail");
            } else {
                r.setResult(ret);
                r.setStatus("ok");
            }
        } catch (Exception e) {
            r.setResult(e.getClass().getName() + ":" + e.getMessage());
            r.setStatus("error");

            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    /**
     * user login
     * @param  /{mobile}/{password}
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
//    public ResponseEntity<JsonResult> login (@PathVariable(value = "mobile") String mobile,
//                                             @PathVariable(value = "password") String password){
    public ResponseEntity<JsonResult> login (@RequestParam("mobile") String mobile, @RequestParam("password") String password){
        JsonResult r = new JsonResult();
        try {
            User user = userService.getUserByMobileAndPwd(mobile, password);
            if(user==null){
                r.setResult(null);
                r.setStatus("error");
            }else{
                r.setResult(user);
                r.setStatus("ok");
            }
        } catch (Exception e) {
            r.setResult(e.getClass().getName() + ":" + e.getMessage());
            r.setStatus("error");
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    /**
     * user register
     * @param  /{mobile}/{password}
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ResponseEntity<JsonResult> register (@RequestParam("mobile") String mobile, @RequestParam("password") String password){
        JsonResult r = new JsonResult();
        User existUser  = userService.getUserByMobileAndPwd(mobile, password);
        if(existUser!=null){
            r.setStatus("exist");
            r.setResult(null);
        }else{
            try {
                User user = new User();
                user.setMobile(mobile);
                user.setPassword(password);
                userService.add(user);
                r.setStatus("ok");
                r.setResult(null);
            } catch (Exception e) {
                r.setResult(e.getClass().getName() + ":" + e.getMessage());
                r.setStatus("error");
                e.printStackTrace();
            }
        }
        return ResponseEntity.ok(r);
    }


    /**
     * update password
     * @param
     * @return
     */
    @RequestMapping(value = "/updatepwd", method = RequestMethod.GET)
    public ResponseEntity<JsonResult> updatePassword (@RequestParam("id") Integer id, @RequestParam("pwdnew") String pwdNew){
        JsonResult r = new JsonResult();
        int res = userService.updatePassword(id, pwdNew);
        if(res>0){
            r.setStatus("ok");
            r.setResult(null);
        }
        return ResponseEntity.ok(r);
    }

}