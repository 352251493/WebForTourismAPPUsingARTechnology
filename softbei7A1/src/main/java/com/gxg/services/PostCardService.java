package com.gxg.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.gxg.dao.CityDao;
import com.gxg.dao.PostCardDao;
import com.gxg.entities.PostCard;
import com.gxg.entities.User;
import com.gxg.utils.FileProcess;
import javafx.geometry.Pos;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 郭欣光 on 2018/6/4.
 */

@Service
public class PostCardService {

    @Autowired
    private CityDao cityDao;

    @Value("${post.card.base.dir}")
    private String postCardUrl;

    @Autowired
    private PostCardDao postCardDao;

    public String addPostCard(String  cityId, MultipartFile image, String sendWord, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        String message;
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            status = "error";
            message = "用户未登录！";
        } else {
            if (cityDao.getCountByCityId(cityId) == 0) {
                status = "error";
                message = "没有该城市！";
            } else {
                if (sendWord.length() > 1000) {
                    status = "error";
                    message = "寄语长度大于1000字节！";
                } else {
                    Timestamp time = new Timestamp(System.currentTimeMillis());
                    String timeString = time.toString();
                    String pcId = "post_" + timeString.split(" ")[0].split("-")[0] + timeString.split(" ")[0].split("-")[1] + timeString.split(" ")[0].split("-")[2] + timeString.split(" ")[1].split(":")[0] + timeString.split(" ")[1].split(":")[1] + timeString.split(" ")[1].split(":")[2].split("\\.")[0] + timeString.split("\\.")[1];//注意，split是按照正则表达式进行分割，.在正则表达式中为特殊字符，需要转义。
                    PostCard postCard = new PostCard();
                    postCard.setPcId(pcId);
                    postCard.setCityId(cityId);
                    User user = (User)session.getAttribute("user");
                    postCard.setUserId(user.getUserId());
                    String fileName = image.getOriginalFilename();
                    String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);//获取文件后缀名
                    String imageUploadDir = postCardUrl + "/" + user.getUserId() + "/";
                    String imageName = pcId + "." + fileType;
                    String imageSrc = "/post_card_information/" + user.getUserId() + "/" + imageName;
                    JSONObject uploadImageResult = new JSONObject(FileProcess.uploadFile(image, imageName, imageUploadDir));
                    if ("error".equals(uploadImageResult.getString("status"))) {
                        status = "error";
                        message = "上传图片出错：" + uploadImageResult.getString("message");
                    } else {
                        postCard.setImage(imageSrc);
                        postCard.setSendWord(sendWord);
                        try {
                            postCardDao.addPostCard(postCard);
                            message = "添加成功！";
                        } catch (Exception e) {
                            System.out.println(e);
                            System.out.println("添加明信片时数据库出错！删除图片结果：" + FileProcess.deleteFile(imageUploadDir + imageName));
                            status = "error";
                            message = "添加失败，操作数据库出错！";
                        }
                    }
                }
            }
        }
        jsonObject.accumulate("status", status);
        jsonObject.accumulate("message", message);
        return jsonObject.toString();
    }

    public String getAllPostCard(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            status = "error";
            String message = "用户未登录！";
            jsonObject.accumulate("message", message);
        } else {
            User user = (User)session.getAttribute("user");
            if (postCardDao.getCountByUserId(user.getUserId()) == 0) {
                status = "error";
                String message = "该用户没有明信片！";
                jsonObject.accumulate("message", message);
            } else {
                List<PostCard> postCardList = postCardDao.getPostCardByUserId(user.getUserId());
                ArrayList<JSONObject> postCardArrayList = new ArrayList<>();
                for (PostCard postCard : postCardList) {
                    postCardArrayList.add(postCard.toJson());
                }
                jsonObject.accumulate("post_card_list", postCardArrayList);
            }
        }
        jsonObject.accumulate("status", status);
        return jsonObject.toString();
    }

    public String getPostCard(String pcId, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            status = "error";
            String message = "用户未登录！";
            jsonObject.accumulate("message", message);
        } else {
            if (postCardDao.getCountByPcId(pcId) == 0) {
                status = "error";
                String message = "没有该明信片！";
                jsonObject.accumulate("message", message);
            } else {
                PostCard postCard = postCardDao.getPostCardByPcId(pcId);
                User user = (User)session.getAttribute("user");
                if (postCard.getUserId().equals(user.getUserId())) {
                    jsonObject.accumulate("post_card", postCard.toJson());
                } else {
                    status = "error";
                    String message = "该明信片不是该用户的，不能查看信息！";
                    jsonObject.accumulate("message", message);
                }
            }
        }
        jsonObject.accumulate("status", status);
        return jsonObject.toString();
    }

    public String updatePostCard(String pcId, String cityId, String sendWord, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        String message;
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            status = "error";
            message = "用户未登录！";
        } else {
            if (postCardDao.getCountByPcId(pcId) == 0) {
                status = "error";
                message = "该明信片不存在！";
            } else {
                PostCard postCard = postCardDao.getPostCardByPcId(pcId);
                User user = (User)session.getAttribute("user");
                if (postCard.getUserId().equals(user.getUserId())) {
                    if (cityDao.getCountByCityId(cityId) == 0) {
                        status = "error";
                        message = "该城市不存在！";
                    } else {
                        if (sendWord.length() > 1000) {
                            status = "error";
                            message = "寄语长度大于1000字节！";
                        } else {
                            postCard.setCityId(cityId);
                            postCard.setSendWord(sendWord);
                            try {
                                postCardDao.updatePostCard(postCard);
                                message = "修改成功！";
                            } catch (Exception e) {
                                System.out.println(e);
                                status = "error";
                                message = "修改失败，操作数据库出错！";
                            }
                        }
                    }
                } else {
                    status = "error";
                    message = "该明信片不属于该用户，不能修改！";
                }
            }
        }
        jsonObject.accumulate("status", status);
        jsonObject.accumulate("message", message);
        return jsonObject.toString();
    }

    public String updatePostCardImage(String pcId, MultipartFile image, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        String message;
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            status = "error";
            message = "用户未登录！";
        } else {
            if (postCardDao.getCountByPcId(pcId) == 0) {
                status = "error";
                message = "没有该明信片！";
            } else  {
                PostCard postCard = postCardDao.getPostCardByPcId(pcId);
                User user = (User)session.getAttribute("user");
                if (postCard.getUserId().equals(user.getUserId())) {
                    String fileName = image.getOriginalFilename();
                    String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);//获取文件后缀名
                    String imageUploadDir = postCardUrl + "/" + user.getUserId() + "/";
                    String imageName = pcId + "." + fileType;
                    String imageSrc = "/post_card_information/" + user.getUserId() + "/" + imageName;
                    String oldImage = postCard.getImage();
                    String oldImageType = oldImage.substring(oldImage.lastIndexOf(".") + 1);//获取文件后缀名
                    String oldImageDir = imageUploadDir + postCard.getPcId() + "." + oldImageType;
                    Boolean isDeleteSuccess = true;
                    if (!oldImageType.equals(fileType)) {
                        JSONObject deleteImageResult = new JSONObject(FileProcess.deleteFile(oldImageDir));
                        if ("error".equals(deleteImageResult.getString("status"))) {
                            isDeleteSuccess = false;
                            System.out.println("删除原有图片失败：" + deleteImageResult.getString("message"));
                        }
                    }
                    JSONObject uploadImageResult = new JSONObject(FileProcess.uploadFile(image, imageName, imageUploadDir));
                    if ("success".equals(uploadImageResult.getString("status"))) {
                        postCard.setImage(imageSrc);
                        try {
                            postCardDao.updatePostCard(postCard);
                            message = "修改成功！";
                        } catch (Exception e) {
                            System.out.println(e);
                            status = "error";
                            message = "修改失败，操作数据库出错！";
                            if (oldImageType.equals(fileType)) {
                                message += "由于图片类型相同，新上传图片可以正常显示！";
                            } else {
                                if (isDeleteSuccess) {
                                    message += "原图片已经成功删除，可能导致图片无法访问！";
                                }
                            }
                        }
                    } else {
                        status = "error";
                        message = "上传图片失败:" + uploadImageResult.getString("message");
                        if (!oldImageType.equals(fileType) && isDeleteSuccess) {
                            message += "但是原图片已经删除！";
                        }
                    }
                } else {
                    status = "error";
                    message = "该明信片不属于该用户，无法更改图片！";
                }
            }
        }
        jsonObject.accumulate("status", status);
        jsonObject.accumulate("message", message);
        return jsonObject.toString();
    }

    public String deletePostCard(String pcId, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        String message;
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            status = "error";
            message = "用户未登录！";
        } else {
            if (postCardDao.getCountByPcId(pcId) == 0) {
                status = "error";
                message = "没有该明信片！";
            } else {
                PostCard postCard = postCardDao.getPostCardByPcId(pcId);
                User user = (User)session.getAttribute("user");
                if (postCard.getUserId().equals(user.getUserId())) {
                    String image = postCard.getImage();
                    String imageType = image.substring(image.lastIndexOf(".") + 1);//获取文件后缀名
                    String imageUploadDir = postCardUrl + "/" + user.getUserId() + "/";
                    String imageDir = imageUploadDir + postCard.getPcId() + "." + imageType;
                    JSONObject deleteImageResult = new JSONObject(FileProcess.deleteFile(imageDir));
                    Boolean deleteImageSuccess = true;
                    if ("error".equals(deleteImageResult.getString("status"))) {
                        deleteImageSuccess = false;
                        System.out.println("删除明信片时删除图片出错：" + deleteImageResult.getString("message"));
                    }
                    try {
                        postCardDao.deletePostCard(postCard);
                        message = "删除成功！";
                    } catch (Exception e) {
                        System.out.println(e);
                        status = "error";
                        message = "删除失败，操作数据库失败！";
                        if (deleteImageSuccess) {
                            message += "但明信片中图片已经删除成功！";
                        }
                    }
                } else {
                    status = "error";
                    message = "该明信片不属于该用户！";
                }
            }
        }
        jsonObject.accumulate("status", status);
        jsonObject.accumulate("message", message);
        return jsonObject.toString();
    }
}
