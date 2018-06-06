package com.gxg.controller;

import com.gxg.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 郭欣光 on 2018/5/29.
 */

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/login")
    public String login(@RequestParam("phone_number") String phoneNumber, @RequestParam("password") String password, HttpServletRequest request) {
        return userService.login(phoneNumber, password, request);
    }

    @PostMapping(value = "/register")
    public String register(@RequestParam("phone_number") String phoneNumber, @RequestParam("password") String password, @RequestParam("name") String name, @RequestParam("gender") String gender, @RequestParam("birth") String birth, @RequestParam("head_portrait") MultipartFile headPortrait, HttpServletRequest request) {
        return userService.register(phoneNumber, password, name, gender, birth, headPortrait, request);
    }

    @PostMapping(value = "/update_user_information")
    public String updateUserInformation(@RequestParam("name") String name, @RequestParam("gender") String genter, @RequestParam("birth") String birth, HttpServletRequest request) {
        return userService.updateUserInformation(name, genter, birth, request);
    }

    @PostMapping(value = "/update_head_portrait")
    public String updateHeadPortrait(@RequestParam("head_portrait") MultipartFile headPortrait, HttpServletRequest request) {
        return  userService.updateHeadPortrait(headPortrait, request);
    }

    @PostMapping(value = "/update_user_phone_number")
    public String updateUserPhoneNumber(@RequestParam("phone_number") String phoneNumber, HttpServletRequest request) {
        return userService.updateUserPhoneNumber(phoneNumber, request);
    }

    @GetMapping("/get_user_information")
    public String getUserInformation(HttpServletRequest request) {
        return userService.getUserInformation(request);
    }

    @PostMapping(value = "/reset_password")
    public String resetPassword(@RequestParam("password") String password,  HttpServletRequest request) {
        return userService.resetPassword(password, request);
    }

    @PostMapping(value = "/forget_password")
    public String forgetPassword(@RequestParam("phone_number") String phoneNumber, @RequestParam("password") String password) {
        return userService.forgetPassword(phoneNumber, password);
    }

    @PostMapping(value = "/sign_out")
    public String signOut(HttpServletRequest request) {
        return userService.signOut(request);
    }
}
