package com.gxg.controller;

import com.gxg.services.TravelPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 郭欣光 on 2018/6/3.
 */

@RestController
@RequestMapping(value = "/travel/plan")
public class TravelPlanController {

    @Autowired
    private TravelPlanService travelPlanService;


    @PostMapping(value = "/general/add_plan")
    public String addTravelPlan(@RequestParam("city_id") String cityId, @RequestParam("vehicle") String vehicle, @RequestParam("begin_date") String beginDate, @RequestParam("end_date") String endDate, HttpServletRequest request) {
        return travelPlanService.addTravelPlan(cityId, vehicle, beginDate, endDate, request);
    }

    @GetMapping(value = "/general/all")
    public String getAllMineTravelPlan(HttpServletRequest request) {
        return travelPlanService.getAllMineTravelPlan(request);
    }

    @PostMapping(value = "/general/update")
    public String updateTravelPlan(@RequestParam("city_id") String cityId, @RequestParam("vehicle") String vehicle, @RequestParam("begin_date") String beginDate, @RequestParam("end_date") String endDate, @RequestParam("travel_id") String travelId, HttpServletRequest request) {
        return travelPlanService.updateTravelPlan(cityId, vehicle, beginDate, endDate, travelId, request);
    }

    @GetMapping(value = "/general/travel_id/{travelId}")
    public String getTravelPlan(@PathVariable String travelId, HttpServletRequest request) {
        return travelPlanService.getTravelPlan(travelId, request);
    }

    @PostMapping(value = "/day/add")
    public String addDayPlan(@RequestParam("travel_id") String travelId, @RequestParam("scenic_area_id") String scenicAreaId, @RequestParam("vehicle") String vehicle, @RequestParam("begin_time") String beginTime, @RequestParam("end_time") String endTime, HttpServletRequest request) {
        return travelPlanService.addDayPlan(travelId, scenicAreaId, vehicle, beginTime, endTime, request);
    }

    @PostMapping(value = "/general/delete/travel_id/{travelId}")
    public String deleteTravelPlan(@PathVariable String travelId, HttpServletRequest request) {
        return travelPlanService.deleteTravelPlan(travelId, request);
    }

    @GetMapping(value = "/day/travel_id/{travelId}")
    public String getDayPlanByTravelId(@PathVariable String travelId, HttpServletRequest request) {
        return travelPlanService.getDayPlanByTravelId(travelId, request);
    }

    @GetMapping(value = "/day/day_id/{dayId}")
    public String getDayPlanByDayId(@PathVariable String dayId, HttpServletRequest request) {
        return travelPlanService.getDayPlanByDayId(dayId, request);
    }

    @PostMapping(value = "/day/update")
    public String updateDayPlan(@RequestParam("day_id") String dayId, @RequestParam("scenic_area_id") String scenicAreaId, @RequestParam("vehicle") String vehicle, @RequestParam("begin_time") String beginTime, @RequestParam("end_time") String endTime, HttpServletRequest request) {
        return travelPlanService.updateDayPlan(dayId, scenicAreaId, vehicle, beginTime, endTime, request);
    }

    @PostMapping(value = "/day/delete/day_id/{dayId}")
    public String deleteDayPlan(@PathVariable String dayId, HttpServletRequest request) {
        return travelPlanService.deleteDayPlan(dayId, request);
    }
}
