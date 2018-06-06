package com.gxg.services;

import com.gxg.dao.CityDao;
import com.gxg.entities.City;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 郭欣光 on 2018/6/2.
 */

@Service
public class CityService {

    @Autowired
    private CityDao cityDao;

    public synchronized String addCity(String cityName, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        String message;
        HttpSession session = request.getSession();
        if (session.getAttribute("admin_user") == null) {
            status = "error";
            message = "管理员未登录！";
        } else {
            if (cityName == null || "".equals(cityName)) {
                status = "error";
                message = "城市名称为空！";
            } else {
                if (cityName.length() > 100) {
                    status = "error";
                    message = "城市名字超过100字符！";
                } else {
                    if (cityDao.getCountByCityName(cityName) == 0) {
                        Timestamp time = new Timestamp(System.currentTimeMillis());
                        String timeString = time.toString();
                        String cityId = "city_" + timeString.split(" ")[0].split("-")[0] + timeString.split(" ")[0].split("-")[1] + timeString.split(" ")[0].split("-")[2] + timeString.split(" ")[1].split(":")[0] + timeString.split(" ")[1].split(":")[1] + timeString.split(" ")[1].split(":")[2].split("\\.")[0] + timeString.split("\\.")[1];//注意，split是按照正则表达式进行分割，.在正则表达式中为特殊字符，需要转义。
                        City city = new City();
                        city.setCityId(cityId);
                        city.setCityName(cityName);
                        try {
                            cityDao.addCity(city);
                            message = "添加成功！";
                        } catch (Exception e) {
                            System.out.println(e);
                            status = "error";
                            message = "添加失败，数据库操作出错！";
                        }
                    } else {
                        status = "error";
                        message = "该城市已存在！";
                    }
                }
            }
        }
        jsonObject.accumulate("status", status);
        jsonObject.accumulate("message", message);
        return jsonObject.toString();
    }

    public String getAll() {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        ArrayList<JSONObject> cityArrayList = new ArrayList<>();
        String message;
        if (cityDao.getCount() == 0) {
            status = "error";
            message = "没有城市信息！";
            jsonObject.accumulate("message", message);
        } else {
            List<City> cityList = cityDao.getAllCity();
            for (City city : cityList) {
                JSONObject cityObject = new JSONObject();
                cityObject.accumulate("city_id", city.getCityId());
                cityObject.accumulate("city_name", city.getCityName());
                cityArrayList.add(cityObject);
            }
            jsonObject.accumulate("city_list", cityArrayList);
        }
        jsonObject.accumulate("status", status);
        return jsonObject.toString();
    }

    public String getCityByCityName(String cityName) {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        String message;
        if (cityDao.getCountByCityName(cityName) == 0) {
            status = "error";
            message = "没有该城市！";
            jsonObject.accumulate("message", message);
        } else {
            City city = cityDao.getCityByCityName(cityName);
            JSONObject cityJsonObject = new JSONObject();
            cityJsonObject.accumulate("city_id", city.getCityId());
            cityJsonObject.accumulate("city_name", city.getCityName());
            jsonObject.accumulate("city", cityJsonObject);
        }
        jsonObject.accumulate("status", status);
        return jsonObject.toString();
    }
}
