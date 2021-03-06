package com.gxg.services;

import com.gxg.dao.PostCardDao;
import com.gxg.dao.TravelPlanDao;
import com.gxg.dao.UserDao;
import com.gxg.entities.PostCard;
import com.gxg.entities.TravelPlan;
import com.gxg.entities.User;
import com.gxg.utils.CheckTelephone;
import com.gxg.utils.FileProcess;
import com.gxg.utils.Md5;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.sql.SQLSyntaxErrorException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 郭欣光 on 2018/5/29.
 */

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Value("${user.information.base.dir}")
    private String userInformationBaseDir;

    @Autowired
    private TravelPlanDao travelPlanDao;

    @Autowired
    private TravelPlanService travelPlanService;

    @Autowired
    private PostCardDao postCardDao;

    @Autowired
    private PostCardService postCardService;

    public String login(String phoneNumber, String password, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        String message;
        if (!CheckTelephone.checkTelephone(phoneNumber)) {
            status = "error";
            message = "无效的电话号！";
        } else {
            if (userDao.getUserCountByPhoneNumber(phoneNumber) == 0) {
                status = "error";
                message = "没有该用户！";
            } else {
                User user = userDao.getUserByPhoneNumber(phoneNumber);
                if (user.getPassword().equals(Md5.md5(password))) {
                    HttpSession session = request.getSession();
                    session.setAttribute("user", user);
                    message = "登录成功！";
                } else {
                    status = "error";
                    message = "密码错误！";
                }
            }
        }
        jsonObject.accumulate("status", status);
        jsonObject.accumulate("message", message);
        return jsonObject.toString();
    }

    public synchronized String register(String phoneNumber, String password, String name, String gender, String birth, MultipartFile headPortrait, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        String message;
        if (!CheckTelephone.checkTelephone(phoneNumber)) {
            status = "error";
            message = "无效的手机号！";
        } else {
            if (userDao.getUserCountByPhoneNumber(phoneNumber) != 0) {
                status = "error";
                message = "该用户已存在！";
            } else {
                if (name.length() > 50) {
                    status = "error";
                    message = "姓名长度不能超过50字节";
                } else {
                    if (gender.length() > 10) {
                        status = "error";
                        message = "性别长度不能超过10字节";
                    } else {
                        if (birth.length() > 15) {
                            status = "error";
                            message = "生日长度不能超过15字节";
                        } else {
                            password = Md5.md5(password);
                            User user = new User();
                            user.setPhoneNumber(phoneNumber);
                            user.setPassword(password);
                            user.setGender(gender);
                            user.setBirth(birth);
                            user.setName(name);
                            Timestamp time = new Timestamp(System.currentTimeMillis());
                            String timeString = time.toString();
                            String userId = "user_" + timeString.split(" ")[0].split("-")[0] + timeString.split(" ")[0].split("-")[1] + timeString.split(" ")[0].split("-")[2] + timeString.split(" ")[1].split(":")[0] + timeString.split(" ")[1].split(":")[1] + timeString.split(" ")[1].split(":")[2].split("\\.")[0] + timeString.split("\\.")[1];//注意，split是按照正则表达式进行分割，.在正则表达式中为特殊字符，需要转义。
                            String fileName = headPortrait.getOriginalFilename();
                            String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);//获取文件后缀名
                            String uploadDir = userInformationBaseDir + "/head_portrait/";
                            String headPortraitName = userId + "." + fileType;
                            String headPortraitSrc = "/user_information/head_portrait/" + headPortraitName;
                            user.setHeadPortrait(headPortraitSrc);
                            user.setUserId(userId);
                            JSONObject result = new JSONObject(FileProcess.uploadFile(headPortrait, headPortraitName, uploadDir));
                            if ("error".equals(result.getString("status"))) {
                                status = "error";
                                message = result.getString("message");
                            } else {
                                try {
                                    userDao.insertUser(user);
                                    HttpSession session = request.getSession();
                                    session.setAttribute("user", user);
                                    message = "注册成功！";
                                } catch (Exception e) {
                                    System.out.println(e);
                                    System.out.println("注册用户添加数据库出错！删除头像图片结果：" + FileProcess.deleteFile(uploadDir + headPortraitName));
                                    status = "error";
                                    message = "注册失败，数据库出错！";
                                }
                            }
                        }
                    }
                }
            }
        }
        jsonObject.accumulate("status", status);
        jsonObject.accumulate("message", message);
        return jsonObject.toString();
    }

    public String updateUserInformation(String name, String gender, String birth, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        String message;
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            status = "error";
            message = "用户未登录！";
        } else {
            if (name.length() > 50) {
                status = "error";
                message = "姓名长度超过50字节！";
            } else {
                if (gender.length() > 10) {
                    status = "error";
                    message = "性别长度超过15字节！";
                } else {
                    if (birth.length() > 15) {
                        status = "error";
                        message = "生日长度大于15";
                    } else {
                        User user = (User)session.getAttribute("user");
                        user.setName(name);
                        user.setGender(gender);
                        user.setBirth(birth);
                        try {
                            userDao.updateUser(user);
                            message = "修改成功！";
                        } catch (Exception e) {
                            System.out.println(e);
                            status = "error";
                            message = "修改失败！操作数据库出错！";
                        }
                        if ("success".equals(status)) {
                            session.setAttribute("user", user);
                        }
                    }
                }
            }
        }
        jsonObject.accumulate("status", status);
        jsonObject.accumulate("message", message);
        return jsonObject.toString();
    }

    public String updateHeadPortrait(MultipartFile headPortrait, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        String message;
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            status = "error";
            message = "用户未登录！";
        } else {
            User user = (User)session.getAttribute("user");
            String uploadDir = userInformationBaseDir + "/head_portrait/";
            String fileName = headPortrait.getOriginalFilename();
            String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);//获取文件后缀名
            String userId = user.getUserId();
            String oldHeadPortraitSrc = user.getHeadPortrait();
            String oldFileType = oldHeadPortraitSrc.substring(oldHeadPortraitSrc.lastIndexOf(".") + 1);
            String headPortraitName = userId + "." + fileType;
            String headPortraitSrc = "/user_information/head_portrait/" + headPortraitName;
            JSONObject result = new JSONObject(FileProcess.uploadFile(headPortrait, headPortraitName, uploadDir));
            if ("error".equals(result.getString("status"))) {
                status = "error";
                message = result.getString("message");
            } else {
                if (!oldFileType.equals(fileType)) {
                    JSONObject deleteFileResult = new JSONObject(FileProcess.deleteFile(uploadDir + userId + "." + oldFileType));
                    if ("error".equals(deleteFileResult.getString("status"))) {
                        System.out.println("删除原头像文件失败：" + deleteFileResult.getString("message"));
                    }
                }
                user.setHeadPortrait(headPortraitSrc);
                try {
                    userDao.updateUser(user);
                    message = "修改头像成功！";
                } catch (Exception e) {
                    System.out.println(e);
                    System.out.println("修改头像失败！删除头像结果：" + FileProcess.deleteFile(uploadDir + headPortraitName));
                    status = "error";
                    message = "修改失败！操作数据库失败！";
                }
                if ("success".equals(status)) {
                    session.setAttribute("user", user);
                }
            }
        }
        jsonObject.accumulate("status", status);
        jsonObject.accumulate("message", message);
        return jsonObject.toString();
    }

    public String updateUserPhoneNumber(String phoneNumber, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        String message;
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            status = "error";
            message = "用户未登录！";
        } else {
            if (userDao.getUserCountByPhoneNumber(phoneNumber) == 0) {
                User user = (User)session.getAttribute("user");
                if (!CheckTelephone.checkTelephone(phoneNumber)) {
                    status = "error";
                    message = "无效的手机号！";
                } else {
                    user.setPhoneNumber(phoneNumber);
                    try {
                        userDao.updateUser(user);
                        message = "修改成功！";
                    } catch (Exception e) {
                        System.out.println(e);
                        status = "error";
                        message = "修改失败！操作数据库出错！";
                    }
                    if ("success".equals(status)) {
                        session.setAttribute("user", user);
                    }
                }
            } else {
                status = "error";
                message = "该手机号已被注册！";
            }
        }
        jsonObject.accumulate("status", status);
        jsonObject.accumulate("message", message);
        return jsonObject.toString();
    }

    public String getUserInformation(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        String message;
        JSONObject userInformation = new JSONObject();
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            status = "error";
            message = "用户未登录！";
            jsonObject.accumulate("message", message);
        } else {
            User user = (User)session.getAttribute("user");
            userInformation.accumulate("phone_number", user.getPhoneNumber());
            userInformation.accumulate("name", user.getName());
            userInformation.accumulate("gender", user.getGender());
            userInformation.accumulate("birth", user.getBirth());
            userInformation.accumulate("user_id", user.getUserId());
            userInformation.accumulate("head_portrait", user.getHeadPortrait());
            jsonObject.accumulate("user_information", userInformation);
        }
        jsonObject.accumulate("status", status);
        return jsonObject.toString();
    }

    public String resetPassword(String password, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        String message;
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            status = "error";
            message = "用户未登录！";
        } else {
            User user = (User)session.getAttribute("user");
            user.setPassword(Md5.md5(password));
            try {
                userDao.updateUser(user);
                message = "修改成功！";
            } catch (Exception e) {
                System.out.println(e);
                status = "error";
                message = "修改失败！数据库操作失败！";
            }
            if ("success".equals(status)) {
                session.setAttribute("user", user);
            }
        }
        jsonObject.accumulate("status", status);
        jsonObject.accumulate("message", message);
        return jsonObject.toString();
    }

    public String forgetPassword(String phoneNumber, String password) {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        String message;
        if (userDao.getUserCountByPhoneNumber(phoneNumber) == 0) {
            status = "error";
            message = "该手机号未注册！";
        } else {
            User user = userDao.getUserByPhoneNumber(phoneNumber);
            user.setPassword(Md5.md5(password));
            try {
                userDao.updateUser(user);
                message = "修改成功！";
            } catch (Exception e) {
                System.out.println(e);
                status = "error";
                message = "修改失败，操作数据库出错！";
            }
        }
        jsonObject.accumulate("status", status);
        jsonObject.accumulate("message", message);
        return jsonObject.toString();
    }

    public String signOut(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String status = "succes";
        String message = "退出成功！";
        HttpSession session = request.getSession();
        session.setAttribute("user", null);
        jsonObject.accumulate("status", status);
        jsonObject.accumulate("message", message);
        return jsonObject.toString();
    }

    public String getAllUser(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        HttpSession session = request.getSession();
        if (session.getAttribute("admin_user") == null) {
            status = "error";
            String message = "管理员未登录！";
            jsonObject.accumulate("message", message);
        } else {
            if (userDao.getCount() == 0) {
                status = "error";
                String message = "没有用户信息！";
                jsonObject.accumulate("message", message);
            } else {
                List<User> userList = userDao.getAllUser();
                ArrayList<JSONObject> userArrayList = new ArrayList<>();
                for (User user : userList) {
                    userArrayList.add(user.toJson());
                }
                jsonObject.accumulate("user_list", userArrayList);
            }
        }
        jsonObject.accumulate("status", status);
        return jsonObject.toString();
    }

    public synchronized String updateUserInformationByAdmin(String userId, String phoneNumber, String name, String gender, String birth, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        String message;
        HttpSession session = request.getSession();
        if (session.getAttribute("admin_user") == null) {
            status = "error";
            message = "管理员未登录！";
        } else {
            if (userDao.getCountByUserId(userId) == 0) {
                status = "error";
                message = "没有该用户！";
            } else {
                User user = userDao.getUserByUserId(userId);
                session.setAttribute("user", user);
                JSONObject updateUserBaseInformationResult = new JSONObject(this.updateUserInformation(name, gender, birth, request));
                if (updateUserBaseInformationResult.getString("status").equals("success")) {
                    if (user.getPhoneNumber().equals(phoneNumber)) {
                        message = "修改成功！";
                    } else {
                        User user1 = userDao.getUserByUserId(userId);
                        session.setAttribute("user", user1);
                        JSONObject updateUserPhoneNumberResult = new JSONObject(this.updateUserPhoneNumber(phoneNumber, request));
                        if (updateUserPhoneNumberResult.getString("status").equals("success")) {
                            message = "修改成功！";
                        } else {
                            status = "error";
                            message = "修改手机号失败：" + updateUserPhoneNumberResult.getString("message") + "但是除手机号之外的其他信息修改成功！";
                        }
                    }
                } else {
                    status = "error";
                    message = updateUserBaseInformationResult.getString("message");
                }
            }
        }
        jsonObject.accumulate("status", status);
        jsonObject.accumulate("message", message);
        return jsonObject.toString();
    }

    public String updateHeadPortraitByAdmin(String userId, MultipartFile headPortrait, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        String message;
        HttpSession session = request.getSession();
        if (session.getAttribute("admin_user") == null) {
            status = "error";
            message = "管理员未登录！";
        } else {
            if (userDao.getCountByUserId(userId) == 0) {
                status = "error";
                message = "没有该用户！";
            } else {
                User user = userDao.getUserByUserId(userId);
                session.setAttribute("user", user);
                JSONObject updateHeadPortraitResult = new JSONObject(this.updateHeadPortrait(headPortrait, request));
                if (updateHeadPortraitResult.getString("status").equals("success")) {
                    message = "修改成功！";
                } else {
                    status = "error";
                    message = updateHeadPortraitResult.getString("message");
                }
            }
        }
        jsonObject.accumulate("status", status);
        jsonObject.accumulate("message", message);
        return jsonObject.toString();
    }

    public String deleteUser(String userId, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        String message;
        HttpSession session = request.getSession();
        if (session.getAttribute("admin_user") == null) {
            status = "error";
            message = "管理员未登录！";
        } else {
            if (userDao.getCountByUserId(userId) == 0) {
                status = "error";
                message = "没有该用户！";
            } else {
                User user = userDao.getUserByUserId(userId);
                session.setAttribute("user", user);
                if (travelPlanDao.getCountByUserId(userId) != 0) {
                    List<TravelPlan> travelPlanList = travelPlanDao.getTravelPlanByUserId(userId);
                    for (TravelPlan travelPlan : travelPlanList) {
                        JSONObject deleteTravelPlanResult = new JSONObject(travelPlanService.deleteTravelPlan(travelPlan.getTravelId(), request));
                        System.out.println("删除用户时删除该用户的旅行计划结果：" + deleteTravelPlanResult.toString());
                    }
                }
                if (postCardDao.getCountByUserId(userId) != 0) {
                    List<PostCard> postCardList = postCardDao.getPostCardByUserId(userId);
                    for (PostCard postCard : postCardList) {
                        JSONObject deletePostCardResult = new JSONObject(postCardService.deletePostCard(postCard.getPcId(), request));
                        System.out.println("删除用户时删除该用户的明信片结果：" + deletePostCardResult);
                    }
                }
                String headPortrait = user.getHeadPortrait();
                String headPortraitType = headPortrait.substring(headPortrait.lastIndexOf(".") + 1);//获取文件后缀名
                String uploadDir = userInformationBaseDir + "/head_portrait/";
                String headPortraitName = userId + "." + headPortraitType;
                JSONObject deleteHeadPortraitResult = new JSONObject(FileProcess.deleteFile(uploadDir + headPortraitName));
                System.out.println("删除头像结果：" + deleteHeadPortraitResult.toString());
                try {
                    userDao.deleteUser(user);
                    message = "删除成功！";
                } catch (Exception e) {
                    status = "error";
                    message = "删除失败，操作数据库失败！该用户的旅行计划、明信片可能已经被删除！";
                }
            }
        }
        jsonObject.accumulate("status", status);
        jsonObject.accumulate("message", message);
        return jsonObject.toString();
    }
}
