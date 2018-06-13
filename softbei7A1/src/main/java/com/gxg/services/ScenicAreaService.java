package com.gxg.services;

import com.gxg.dao.*;
import com.gxg.entities.*;
import com.gxg.utils.FileProcess;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 郭欣光 on 2018/6/2.
 */

@Service
public class ScenicAreaService {

    @Autowired
    private CityDao cityDao;

    @Value("${scenic.area.information.base.dir}")
    private String scenicAreaInformationUrl;

    @Autowired
    private ScenicAreaDao scenicAreaDao;

    @Autowired
    private DayPlanDao dayPlanDao;

    @Autowired
    private TravelPlanService travelPlanService;

    @Autowired
    private TravelPlanDao travelPlanDao;

    @Autowired
    private UserDao userDao;

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

    public String getAll() {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        if (scenicAreaDao.getCount() == 0) {
            status = "error";
            String message = "没有景点信息！";
            jsonObject.accumulate("message", message);
        } else {
            List<ScenicArea> scenicAreaList = scenicAreaDao.getAllScenicArea();
            ArrayList<JSONObject> scenicAreaArrayList = new ArrayList<>();
            for (ScenicArea scenicArea : scenicAreaList) {
                scenicAreaArrayList.add(scenicArea.toJson());
            }
            jsonObject.accumulate("scenic_area_list", scenicAreaArrayList);
        }
        jsonObject.accumulate("status", status);
        return jsonObject.toString();
    }

    public String updateSaImg(String saId, MultipartFile saImg, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        String message;
        HttpSession session = request.getSession();
        if (session.getAttribute("admin_user") == null) {
            status = "error";
            message = "管理员未登录！";
        } else {
            if (scenicAreaDao.getCountBySaId(saId) == 0) {
                status = "error";
                message = "没有该景点！";
            } else {
                ScenicArea scenicArea = scenicAreaDao.getScenicAreaBySaId(saId);
                String cityId = scenicArea.getCityId();
                String fileName = saImg.getOriginalFilename();
                String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);//获取文件后缀名
                String saImgUploadDir = scenicAreaInformationUrl + "/sa_img/" + cityId + "/";
                String saImgName = saId + "." + fileType;
                String saImgSrc = "/scenic_area_information/sa_img/" + cityId + "/" + saImgName;
                String oldImg = scenicArea.getSaImg();
                String oldImgType = oldImg.substring(oldImg.lastIndexOf(".") + 1);
                String oldImgName = saId + "." + oldImgType;
                Boolean isDeleteSuccess = false;
                if (!fileType.equals(oldImgType)) {
                    JSONObject deleteImgResult = new JSONObject(FileProcess.deleteFile(saImgUploadDir + oldImgName));
                    if (deleteImgResult.get("status").equals("success")) {
                        isDeleteSuccess = true;
                    } else {
                        System.out.println("修改图片时删除原图片出错：" + deleteImgResult.toString());
                    }
                }
                JSONObject uploadImgResult = new JSONObject(FileProcess.uploadFile(saImg, saImgName, saImgUploadDir));
                if ("success".equals(uploadImgResult.get("status"))) {
                    if (fileType.equals(oldImgType)) {
                        message = "更新成功！";
                    } else {
                        scenicArea.setSaImg(saImgSrc);
                        try {
                            scenicAreaDao.updateScenicArea(scenicArea);
                            message = "更新成功！";
                        } catch (Exception e) {
                            System.out.println(e);
                            status = "error";
                            message = "更新失败！操作数据库出错！";
                            if (isDeleteSuccess) {
                                message += "原图片已经被成功删除！";
                            }
                        }
                    }
                } else {
                    status = "error";
                    message = "上传图片出错：" + uploadImgResult.get("message");
                    if (isDeleteSuccess) {
                        message += "原有图片已经被成功删除！";
                    }
                }
            }
        }
        jsonObject.accumulate("status", status);
        jsonObject.accumulate("message", message);
        return jsonObject.toString();
    }

    public String updateSaAr(String saId, MultipartFile saAr, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        String message;
        HttpSession session = request.getSession();
        if (session.getAttribute("admin_user") == null) {
            status = "error";
            message = "管理员未登录！";
        } else {
            if (scenicAreaDao.getCountBySaId(saId) == 0) {
                status = "error";
                message = "没有该景点！";
            } else {
                ScenicArea scenicArea = scenicAreaDao.getScenicAreaBySaId(saId);
                String cityId = scenicArea.getCityId();
                String saArFileName = saAr.getOriginalFilename();
                String saArFileType = saArFileName.substring(saArFileName.lastIndexOf(".") + 1);//获取文件后缀名
                String saArUploadDir = scenicAreaInformationUrl + "/sa_ar/" + cityId + "/";
                String saArName = saId + "." + saArFileType;
                String saArSrc = "/scenic_area_information/sa_ar/" + cityId + "/" + saArName;
                String oldSaAr = scenicArea.getSaAr();
                String oldSaArType = oldSaAr.substring(oldSaAr.lastIndexOf(".") + 1);
                String oldSaArName = saId + "." + oldSaArType;
                Boolean isDeleteSuccess = false;
                if (!oldSaArType.equals(saArFileType)) {
                    JSONObject deleteArResult = new JSONObject(FileProcess.deleteFile(saArUploadDir + oldSaArName));
                    if (deleteArResult.get("status").equals("success")) {
                        isDeleteSuccess = true;
                    } else {
                        System.out.println("更新AR文件是删除原AR文件出错：" + deleteArResult.toString());
                    }
                }
                JSONObject uploadArResult = new JSONObject(FileProcess.uploadFile(saAr, saArName, saArUploadDir));
                if (uploadArResult.get("status").equals("success")) {
                    if (oldSaArType.equals(saArFileName)) {
                        message = "更新成功！";
                    } else {
                        scenicArea.setSaAr(saArSrc);
                        try {
                            scenicAreaDao.updateScenicArea(scenicArea);
                            message = "更新成功！";
                        } catch (Exception e) {
                            System.out.println(e);
                            status = "error";
                            message = "更新失败，操作数据库出错！";
                            if (isDeleteSuccess) {
                                message += "原AR文件已经成功删除！";
                            }
                        }
                    }
                } else {
                    status = "error";
                    message = "上传AR文件出错：" + uploadArResult.get("message");
                    if (isDeleteSuccess) {
                        message += "原AR文件已经成功删除！";
                    }
                }
            }
        }
        jsonObject.accumulate("status", status);
        jsonObject.accumulate("message", message);
        return jsonObject.toString();
    }

    public String updateSaIntro(String saId, String saIntro, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        String message;
        HttpSession session = request.getSession();
        if (session.getAttribute("admin_user") == null) {
            status = "error";
            message = "管理员未登录！";
        } else {
            if (saIntro == null || "".equals(saId)) {
                status = "error";
                message = "景点简介不能为空！";
            } else {
                if (saIntro.length() > 1000) {
                    status = "error";
                    message = "景点简介长度不能超过1000字节！";
                } else {
                    if (scenicAreaDao.getCountBySaId(saId) == 0) {
                        status = "error";
                        message = "没有该景点！";
                    } else {
                        ScenicArea scenicArea = scenicAreaDao.getScenicAreaBySaId(saId);
                        scenicArea.setSaIntro(saIntro);
                        try {
                            scenicAreaDao.updateScenicArea(scenicArea);
                            message = "修改成功！";
                        } catch (Exception e) {
                            System.out.println(e);
                            status = "error";
                            message = "修改失败，操作数据库失败！";
                        }
                    }
                }
            }
        }
        jsonObject.accumulate("status", status);
        jsonObject.accumulate("message", message);
        return jsonObject.toString();
    }

    public String deleteScenicArea(String saId, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        String message;
        HttpSession session = request.getSession();
        if (session.getAttribute("admin_user") == null) {
            status = "error";
            message = "管理员未登录！";
        } else {
            if (scenicAreaDao.getCountBySaId(saId) == 0) {
                status = "error";
                message = "没有该景点！";
            } else {
                if (dayPlanDao.getCountByScenicAreaId(saId) != 0) {
                    List<DayPlan> dayPlanList = dayPlanDao.getDayPlanByScenicAreaId(saId);
                    for (DayPlan dayPlan : dayPlanList) {
                        if (travelPlanDao.getCountByTravelId(dayPlan.getTravelId()) != 0) {
                            TravelPlan travelPlan = travelPlanDao.getTravelPlanByTravelId(dayPlan.getTravelId());
                            if (userDao.getCountByUserId(travelPlan.getUserId()) != 0) {
                                User user = userDao.getUserByUserId(travelPlan.getUserId());
                                session.setAttribute("user", user);
                                JSONObject deleteDayPlanResult = new JSONObject(travelPlanService.deleteDayPlan(dayPlan.getDayId(), request));
                                System.out.println("因删除景点而删除日计划结果：" + deleteDayPlanResult.toString());
                            }
                        }
                    }
                }
                ScenicArea scenicArea = scenicAreaDao.getScenicAreaBySaId(saId);
                String saImg = scenicArea.getSaImg();
                String saAr = scenicArea.getSaAr();
                String cityId = scenicArea.getCityId();
                String saImgUploadDir = scenicAreaInformationUrl + "/sa_img/" + cityId + "/";
                String saImgType = saImg.substring(saImg.lastIndexOf(".") + 1);
                String saImgName = saId + "." + saImgType;
                JSONObject deleteSaImgResult = new JSONObject(FileProcess.deleteFile(saImgUploadDir + saImgName));
                System.out.println("删除景点图片结果：" + deleteSaImgResult.toString());
                String saArUploadDir = scenicAreaInformationUrl + "/sa_ar/" + cityId + "/";
                String saArType = saAr.substring(saAr.lastIndexOf(".") + 1);
                String saArName = saId + "." + saArType;
                JSONObject deleteArResult = new JSONObject(FileProcess.deleteFile(saArUploadDir + saArName));
                System.out.println("删除景点AR文件结果：" + deleteArResult);
                try {
                    scenicAreaDao.deeteScenicArea(scenicArea);
                    message = "删除成功！";
                } catch (Exception e) {
                    System.out.println(e);
                    status = "error";
                    message = "删除失败，操作数据库出错！景点照片及AR文件可能已经被删除，用户基于该景点创建的旅行日计划可能已经被删除！";
                }
            }
        }
        jsonObject.accumulate("status", status);
        jsonObject.accumulate("message", message);
        return jsonObject.toString();
    }
}
