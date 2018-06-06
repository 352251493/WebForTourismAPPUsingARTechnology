package com.gxg.controller;

import com.gxg.services.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 郭欣光 on 2018/6/2.
 */

@RestController
@RequestMapping(value = "/city")
public class CityController {

    @Autowired
    private CityService cityService;

    @PostMapping(value = "/admin/add_city")
    public String addCity(@RequestParam("city_name") String cityName, HttpServletRequest request) {
        return cityService.addCity(cityName, request);
    }

    @GetMapping(value = "/get_all")
    public String getAll() {
        return cityService.getAll();
    }

    @GetMapping(value = "/name/{cityName}")
    public String getCityByCityName(@PathVariable String cityName) {
        return cityService.getCityByCityName(cityName);
    }
}
