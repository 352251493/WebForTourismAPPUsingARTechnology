package com.gxg.controller;

import com.gxg.services.AdminUserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 郭欣光 on 2018/6/1.
 */

@RestController
@RequestMapping(value = "/admin_user")
public class AdminUserController {

    @Autowired
    private AdminUserService adminUserService;

    @PostMapping(value = "/login")
    public String login(@RequestParam("id") String id, @RequestParam("password") String password, HttpServletRequest request) {
        return adminUserService.login(id, password, request);
    }

    @PostMapping(value = "/reset_password")
    public String resetPassword(@RequestParam("password") String password, HttpServletRequest request) {
        return adminUserService.resetPassword(password, request);
    }
}
