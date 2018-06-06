package com.gxg.controller;

import com.gxg.services.ScenicAreaServic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 郭欣光 on 2018/6/2.
 */

@RestController
@RequestMapping(value = "/scenic_area")
public class ScenicAreaController {

    @Autowired
    private ScenicAreaServic scenicAreaServic;

    @PostMapping(value = "/admin/add_scenic_area")
    public String addScenicArea(@RequestParam("city_id") String cityId, @RequestParam("sa_img") MultipartFile saImg, @RequestParam("sa_ar") MultipartFile saAr, @RequestParam("sa_intro") String saIntro, HttpServletRequest request) {
        return scenicAreaServic.addScenicArea(cityId, saImg, saAr, saIntro, request);
    }

    @GetMapping(value = "/city_id/{cityId}")
    public String getScenicAreaByCityId(@PathVariable String cityId) {
        return scenicAreaServic.getScenicAreaByCityId(cityId);
    }

    @GetMapping(value = "/city_name/{cityName}")
    public String getScenicAreaByCityName(@PathVariable String cityName) {
        return scenicAreaServic.getScenicAreaByCityName(cityName);
    }

    @GetMapping(value = "/sa_id/{saId}")
    public String getScenicAreaBySaId(@PathVariable String saId) {
        return scenicAreaServic.getScenicAreaBySaId(saId);
    }
}
