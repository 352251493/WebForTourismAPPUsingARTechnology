package com.gxg.controller;

import com.gxg.services.ScenicAreaService;
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
    private ScenicAreaService scenicAreaService;

    @PostMapping(value = "/admin/add_scenic_area")
    public String addScenicArea(@RequestParam("city_id") String cityId, @RequestParam("sa_img") MultipartFile saImg, @RequestParam("sa_ar") MultipartFile saAr, @RequestParam("sa_intro") String saIntro, HttpServletRequest request) {
        return scenicAreaService.addScenicArea(cityId, saImg, saAr, saIntro, request);
    }

    @GetMapping(value = "/city_id/{cityId}")
    public String getScenicAreaByCityId(@PathVariable String cityId) {
        return scenicAreaService.getScenicAreaByCityId(cityId);
    }

    @GetMapping(value = "/city_name/{cityName}")
    public String getScenicAreaByCityName(@PathVariable String cityName) {
        return scenicAreaService.getScenicAreaByCityName(cityName);
    }

    @GetMapping(value = "/sa_id/{saId}")
    public String getScenicAreaBySaId(@PathVariable String saId) {
        return scenicAreaService.getScenicAreaBySaId(saId);
    }

    @GetMapping(value = "/get_all")
    public String getAll() {
        return scenicAreaService.getAll();
    }

    @PostMapping(value = "/admin/update/sa_img/sa_id/{saId}")
    public String updateSaImg(@PathVariable String saId, @RequestParam("sa_img") MultipartFile saImg, HttpServletRequest request) {
        return scenicAreaService.updateSaImg(saId, saImg, request);
    }

    @PostMapping(value = "/admin/update/sa_ar/sa_id/{saId}")
    public String updateSaAr(@PathVariable String saId, @RequestParam("sa_ar") MultipartFile saAr, HttpServletRequest request) {
        return scenicAreaService.updateSaAr(saId, saAr, request);
    }

    @PostMapping(value = "/admin/update/sa_intro/sa_id/{saId}")
    public String updateSaIntro(@PathVariable String saId, @RequestParam("sa_intro") String saIntro, HttpServletRequest request) {
        return scenicAreaService.updateSaIntro(saId, saIntro, request);
    }

    @PostMapping(value = "/admin/delete/sa_id/{saId}")
    public String deleteScenicArea(@PathVariable String saId, HttpServletRequest request) {
        return scenicAreaService.deleteScenicArea(saId, request);
    }
}
