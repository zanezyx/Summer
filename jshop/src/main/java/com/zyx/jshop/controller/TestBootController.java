package com.zyx.jshop.controller;


import com.zyx.jshop.entity.User;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
@RequestMapping("/testboot")
public class TestBootController {
    @RequestMapping("getuser")
    public String getUser() {
        return "hello";
    }
}