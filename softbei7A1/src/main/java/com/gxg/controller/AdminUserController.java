package com.gxg.controller;

import com.gxg.services.AdminUserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(value = "/get_information")
    public String getInformation(HttpServletRequest request) {
        return adminUserService.getInformation(request);
    }

    @PostMapping(value = "/lay_out")
    public String layOut(HttpServletRequest request) {
        return adminUserService.layOut(request);
    }
}
