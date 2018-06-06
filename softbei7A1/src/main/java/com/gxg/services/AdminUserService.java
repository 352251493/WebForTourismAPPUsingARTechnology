package com.gxg.services;

import com.gxg.dao.AdminUserDao;
import com.gxg.entities.AdminUser;
import com.gxg.utils.Md5;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by 郭欣光 on 2018/6/1.
 */

@Service
public class AdminUserService {

    @Autowired
    private AdminUserDao adminUserDao;

    public String login(String id, String password, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        String message;
        if (adminUserDao.getCountById(id) == 0) {
            status = "error";
            message = "没有该用户！";
        } else {
            AdminUser adminUser = adminUserDao.queryById(id);
            if (adminUser.getPassword().equals(Md5.md5(password))) {
                HttpSession session = request.getSession();
                session.setAttribute("admin_user", adminUser);
                message = "登录成功！";
            } else {
                status = "error";
                message = "密码错误！";
            }
        }
        jsonObject.accumulate("status", status);
        jsonObject.accumulate("message", message);
        return jsonObject.toString();
    }

    public String resetPassword(String password, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        String message;
        HttpSession session = request.getSession();
        if (session.getAttribute("admin_user") == null) {
            status = "error";
            message = "用户未登录！";
        } else {
            AdminUser adminUser = (AdminUser)session.getAttribute("admin_user");
            message = "修改成功！";
            adminUser.setPassword(Md5.md5(password));
            try {
                adminUserDao.updateAdminUser(adminUser);
            } catch (Exception e) {
                System.out.println(e);
                status  = "error";
                message = "修改密码失败！数据库出错！";
            }
            if ("success".equals(status)) {
                session.setAttribute("admin_user", adminUser);
            }
        }
        jsonObject.accumulate("status", status);
        jsonObject.accumulate("message", message);
        return jsonObject.toString();
    }
}
