package com.gxg.services;

import com.gxg.dao.CityDao;
import com.gxg.dao.ScenicAreaDao;
import com.gxg.entities.City;
import com.gxg.entities.ScenicArea;
import com.gxg.utils.FileProcess;
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
 * Created by 郭欣光 on 2018/6/2.
 */

@Service
public class ScenicAreaServic {

    @Autowired
    private CityDao cityDao;

    @Value("${scenic.area.information.base.dir}")
    private String scenicAreaInformationUrl;

    @Autowired
    private ScenicAreaDao scenicAreaDao;

    public synchronized String addScenicArea(String cityId, MultipartFile saImg, MultipartFile saAr, String saIntro, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        String message;
        HttpSession session = request.getSession();
        if (session.getAttribute("admin_user") == null) {
            status = "error";
            message = "管理员未登录！";
        } else {
            if (cityDao.getCountByCityId(cityId) == 0) {
                status = "error";
                message = "没有该城市！";
            } else {
                if (saIntro.length() > 1000) {
                    status = "error";
                    message = "景区介绍超过1000字符！";
                } else {
                    Timestamp time = new Timestamp(System.currentTimeMillis());
                    String timeString = time.toString();
                    String saId = "sa_" + timeString.split(" ")[0].split("-")[0] + timeString.split(" ")[0].split("-")[1] + timeString.split(" ")[0].split("-")[2] + timeString.split(" ")[1].split(":")[0] + timeString.split(" ")[1].split(":")[1] + timeString.split(" ")[1].split(":")[2].split("\\.")[0] + timeString.split("\\.")[1];//注意，split是按照正则表达式进行分割，.在正则表达式中为特殊字符，需要转义。
                    String fileName = saImg.getOriginalFilename();
                    String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);//获取文件后缀名
                    String saImgUploadDir = scenicAreaInformationUrl + "/sa_img/" + cityId + "/";
                    String saImgName = saId + "." + fileType;
                    String saImgSrc = "/scenic_area_information/sa_img/" + cityId + "/" + saImgName;
                    JSONObject uploadSaImgResult = new JSONObject(FileProcess.uploadFile(saImg, saImgName, saImgUploadDir));
                    if ("error".equals(uploadSaImgResult.getString("status"))) {
                        status = "error";
                        message = "上传图片出错：" + uploadSaImgResult.getString("message");
                    } else {
                        String saArFileName = saAr.getOriginalFilename();
                        String saArFileType = saArFileName.substring(saArFileName.lastIndexOf(".") + 1);//获取文件后缀名
                        String saArUploadDir = scenicAreaInformationUrl + "/sa_ar/" + cityId + "/";
                        String saArName = saId + "." + saArFileType;
                        String saArSrc = "/scenic_area_information/sa_ar/" + cityId + "/" + saArName;
                        JSONObject uploadSaArResult = new JSONObject(FileProcess.uploadFile(saAr, saArName, saArUploadDir));
                        if ("error".equals(uploadSaArResult.getString("status"))) {
                            status = "error";
                            message = "上传AR文件出错：" + uploadSaArResult.getString("message");
                            System.out.println("上传AR文件出错，删除IMG结果：" + FileProcess.deleteFile(saImgUploadDir + saImgName));
                        } else {
                            ScenicArea scenicArea = new ScenicArea();
                            scenicArea.setSaId(saId);
                            scenicArea.setCityId(cityId);
                            scenicArea.setSaImg(saImgSrc);
                            scenicArea.setSaAr(saArSrc);
                            scenicArea.setSaIntro(saIntro);
                            try {
                                scenicAreaDao.addScenicArea(scenicArea);
                                message = "添加成功！";
                            } catch (Exception e) {
                                System.out.println(e);
                                System.out.println("添加景点操作数据库失败，删除IMG结果：" + FileProcess.deleteFile(saImgUploadDir + saImgName) + "删除AR文件结果：" + FileProcess.deleteFile(saArUploadDir + saArName));
                                status = "error";
                                message = "添加失败，操作数据库出错！";
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

    public String getScenicAreaByCityId(String cityId) {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        if (cityDao.getCountByCityId(cityId) == 0) {
            status = "error";
            String message = "该城市不存在";
            jsonObject.accumulate("message", message);
        } else {
            if (scenicAreaDao.getCountByCityId(cityId) == 0) {
                status = "error";
                String message = "该城市没有任何景点！";
            } else {
                List<ScenicArea> scenicAreaList = scenicAreaDao.getScenicAreaByCityId(cityId);
                ArrayList<JSONObject> scenicAreaArrayList = new ArrayList<>();
                for (ScenicArea scenicArea : scenicAreaList) {
                    JSONObject scenicAreaObject = new JSONObject();
                    scenicAreaObject.accumulate("sa_id", scenicArea.getSaId());
                    scenicAreaObject.accumulate("city_id", scenicArea.getCityId());
                    scenicAreaObject.accumulate("sa_img", scenicArea.getSaImg());
                    scenicAreaObject.accumulate("sa_ar", scenicArea.getSaAr());
                    scenicAreaObject.accumulate("sa_intro", scenicArea.getSaIntro());
                    scenicAreaArrayList.add(scenicAreaObject);
                }
                jsonObject.accumulate("scenic_area_list", scenicAreaArrayList);
            }
        }
        jsonObject.accumulate("status", status);
        return jsonObject.toString();
    }

    public String getScenicAreaByCityName(String cityName) {
        if (cityDao.getCountByCityName(cityName) == 0) {
            JSONObject jsonObject = new JSONObject();
            String status = "error";
            String message = "该城市不存在！";
            jsonObject.accumulate("status", status);
            jsonObject.accumulate("message", message);
            return jsonObject.toString();
        } else {
            City city = cityDao.getCityByCityName(cityName);
            return this.getScenicAreaByCityId(city.getCityId());
        }
    }

    public String getScenicAreaBySaId(String saId) {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        if (scenicAreaDao.getCountBySaId(saId) == 0) {
            status = "error";
            String message = "没有该景点！";
        } else {
            ScenicArea scenicArea = scenicAreaDao.getScenicAreaBySaId(saId);
            JSONObject scenicAreaObject = new JSONObject();
            scenicAreaObject.accumulate("sa_id", scenicArea.getSaId());
            scenicAreaObject.accumulate("city_id", scenicArea.getCityId());
            scenicAreaObject.accumulate("sa_img", scenicArea.getSaImg());
            scenicAreaObject.accumulate("sa_ar", scenicArea.getSaAr());
            scenicAreaObject.accumulate("sa_intro", scenicArea.getSaIntro());
            jsonObject.accumulate("scenic_area", scenicAreaObject);
        }
        jsonObject.accumulate("status", status);
        return jsonObject.toString();
    }
}
