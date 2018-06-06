package com.gxg.services;

import com.gxg.dao.CityDao;
import com.gxg.dao.DayPlanDao;
import com.gxg.dao.ScenicAreaDao;
import com.gxg.dao.TravelPlanDao;
import com.gxg.entities.DayPlan;
import com.gxg.entities.TravelPlan;
import com.gxg.entities.User;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 郭欣光 on 2018/6/3.
 */

@Service
public class TravelPlanService {

    @Autowired
    private CityDao cityDao;

    @Autowired
    private TravelPlanDao travelPlanDao;

    @Autowired
    private ScenicAreaDao scenicAreaDao;

    @Autowired
    private DayPlanDao dayPlanDao;

    public synchronized String addTravelPlan(String cityId, String vehicle, String beginDate, String endDate, HttpServletRequest request) {
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
                message = "该城市不存在！";
            } else {
                if (vehicle.length() > 100) {
                    status = "error";
                    message = "出行方式长度大于100字节！";
                } else {
                    if (beginDate.length() > 19) {
                        status = "error";
                        message = "开始日期长度大于19字节！";
                    } else {
                        if (endDate.length() > 19) {
                            status = "error";
                            message = "结束日期长度大于19字节！";
                        } else {
                            User user = (User)session.getAttribute("user");
                            TravelPlan travelPlan = new TravelPlan();
                            travelPlan.setCityId(cityId);
                            travelPlan.setVehicle(vehicle);
                            travelPlan.setBeginDate(beginDate);
                            travelPlan.setEndDate(endDate);
                            Timestamp time = new Timestamp(System.currentTimeMillis());
                            String timeString = time.toString();
                            String travelId = "plan_" + timeString.split(" ")[0].split("-")[0] + timeString.split(" ")[0].split("-")[1] + timeString.split(" ")[0].split("-")[2] + timeString.split(" ")[1].split(":")[0] + timeString.split(" ")[1].split(":")[1] + timeString.split(" ")[1].split(":")[2].split("\\.")[0] + timeString.split("\\.")[1];//注意，split是按照正则表达式进行分割，.在正则表达式中为特殊字符，需要转义。
                            travelPlan.setTravelId(travelId);
                            travelPlan.setUserId(user.getUserId());
                            try {
                                travelPlanDao.addTravelPlan(travelPlan);
                                message = "添加成功！";
                            } catch (Exception e) {
                                System.out.println(e);
                                status = "error";
                                message = "添加失败，操作数据库失出错！";
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

    public String getAllMineTravelPlan(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            status = "error";
            String message = "用户未登录！";
            jsonObject.accumulate("message", message);
        } else {
            User user = (User)session.getAttribute("user");
            if (travelPlanDao.getCountByUserId(user.getUserId()) == 0) {
                status = "error";
                String message = "没有旅行计划！";
                jsonObject.accumulate("message", message);
            } else {
                List<TravelPlan> travelPlanList = travelPlanDao.getTravelPlanByUserId(user.getUserId());
                ArrayList<JSONObject> travelPlanArrayList = new ArrayList<>();
                for (TravelPlan travelPlan : travelPlanList) {
                    travelPlanArrayList.add(travelPlan.toJson());
                }
                jsonObject.accumulate("travel_plan_list", travelPlanArrayList);
            }
        }
        jsonObject.accumulate("status", status);
        return jsonObject.toString();
    }

    public String updateTravelPlan(String cityId, String vehicle, String beginDate, String endDate, String travelId, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        String message;
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            status = "error";
            message = "用户未登录！";
        } else {
            if (travelPlanDao.getCountByTravelId(travelId) == 0) {
                status = "error";
                message = "该旅行计划不存在！";
            } else {
                User user = (User)session.getAttribute("user");
                TravelPlan travelPlan = travelPlanDao.getTravelPlanByTravelId(travelId);
                if (travelPlan.getUserId().equals(user.getUserId())) {
                    if (cityDao.getCountByCityId(cityId) == 0) {
                        status = "error";
                        message = "没有该城市！";
                    } else {
                        travelPlan.setCityId(cityId);
                        if (vehicle.length() > 100) {
                            status = "error";
                            message = "出行方式长度大于100字节！";
                        } else {
                            travelPlan.setVehicle(vehicle);
                            if (beginDate.length() > 19) {
                                status = "error";
                                message = "开始日期长度大于19字节！";
                            } else {
                                travelPlan.setBeginDate(beginDate);
                                if (endDate.length() > 19) {
                                    status = "error";
                                    message = "结束日期长度大于19字节！";
                                } else {
                                    travelPlan.setEndDate(endDate);
                                    try {
                                        travelPlanDao.updateTravelPlan(travelPlan);
                                        message = "修改成功！";
                                    } catch (Exception e) {
                                        System.out.println(e);
                                        status = "error";
                                        message = "修改失败，操作数据库出错！";
                                    }
                                }
                            }
                        }
                    }
                } else {
                    status = "error";
                    message = "该计划不是该用户发布的，不能修改！";
                }
            }
        }
        jsonObject.accumulate("status", status);
        jsonObject.accumulate("message", message);
        return jsonObject.toString();
    }

    public String getTravelPlan(String travelId, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            status = "error";
            String message = "用户未登录！";
            jsonObject.accumulate("message", message);
        } else {
            if (travelPlanDao.getCountByTravelId(travelId) == 0) {
                status = "error";
                String message = "没有该旅行计划！";
                jsonObject.accumulate("message", message);
            } else {
                TravelPlan travelPlan = travelPlanDao.getTravelPlanByTravelId(travelId);
                User user = (User)session.getAttribute("user");
                if (travelPlan.getUserId().equals(user.getUserId())) {
                    jsonObject.accumulate("travel_plan", travelPlan.toJson());
                } else {
                    status = "error";
                    String message = "该旅行计划不是该用户的不能获取该旅行计划信息！";
                    jsonObject.accumulate("message", message);
                }
            }
        }
        jsonObject.accumulate("status", status);
        return jsonObject.toString();
    }

    public synchronized String addDayPlan(String travelId, String scenicAreaId, String vehicle, String beginTime, String endTime, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        String message;
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            status = "error";
            message = "用户未登录！";
        } else {
            if (travelPlanDao.getCountByTravelId(travelId) == 0) {
                status = "error";
                message = "没有该旅行计划！";
            } else {
                TravelPlan travelPlan = travelPlanDao.getTravelPlanByTravelId(travelId);
                User user = (User)session.getAttribute("user");
                if (travelPlan.getUserId().equals(user.getUserId())) {
                    if (scenicAreaDao.getCountBySaId(scenicAreaId) == 0) {
                        status = "error";
                        message = "没有该景点！";
                    } else {
                        if (vehicle.length() > 100) {
                            status = "error";
                            message = "出行方式长度大于100字节！";
                        } else {
                            if (beginTime.length() > 19) {
                                status = "error";
                                message = "开始时间长度大于19字节！";
                            } else {
                                if (endTime.length() > 19) {
                                    status = "error";
                                    message = "结束时间长度大于19字节！";
                                } else {
                                    DayPlan dayPlan = new DayPlan();
                                    Timestamp time = new Timestamp(System.currentTimeMillis());
                                    String timeString = time.toString();
                                    String dayId = "day_plan_" + timeString.split(" ")[0].split("-")[0] + timeString.split(" ")[0].split("-")[1] + timeString.split(" ")[0].split("-")[2] + timeString.split(" ")[1].split(":")[0] + timeString.split(" ")[1].split(":")[1] + timeString.split(" ")[1].split(":")[2].split("\\.")[0] + timeString.split("\\.")[1];//注意，split是按照正则表达式进行分割，.在正则表达式中为特殊字符，需要转义。
                                    dayPlan.setDayId(dayId);
                                    dayPlan.setTravelId(travelId);
                                    dayPlan.setScenicAreaId(scenicAreaId);
                                    dayPlan.setVehicle(vehicle);
                                    dayPlan.setBeginTime(beginTime);
                                    dayPlan.setEndTime(endTime);
                                    try {
                                        dayPlanDao.addDayPlan(dayPlan);
                                        message = "添加成功！";
                                    } catch (Exception e) {
                                        System.out.println(e);
                                        status = "error";
                                        message = "添加失败，操作数据库出错！";
                                    }
                                }
                            }
                        }
                    }
                } else {
                    status = "error";
                    message = "该旅行计划不是该用户的无法添加旅行日计划！";
                }
            }
        }
        jsonObject.accumulate("status", status);
        jsonObject.accumulate("message", message);
        return jsonObject.toString();
    }

    public String deleteTravelPlan(String travelId, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        String message;
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            status = "error";
            message = "用户未登录！";
        } else {
            if (travelPlanDao.getCountByTravelId(travelId) == 0) {
                status = "error";
                message = "没有该旅行计划！";
            } else {
                TravelPlan travelPlan = travelPlanDao.getTravelPlanByTravelId(travelId);
                User user = (User) session.getAttribute("user");
                if (travelPlan.getUserId().equals(user.getUserId())) {
                    Boolean isDeleteDayPlanError = false;
                    if (dayPlanDao.getCountByTravelId(travelPlan.getTravelId()) != 0) {
                        List<DayPlan> dayPlanList = dayPlanDao.getDayPlanByTravelId(travelId);
                        for (DayPlan dayPlan : dayPlanList) {
                            try {
                                dayPlanDao.deleteDayPlan(dayPlan);
                            } catch (Exception e) {
                                isDeleteDayPlanError = true;
                                System.out.println(e);
                                System.out.println("删除旅行计划时删除日计划出错，日计划ID：" + dayPlan.getDayId());
                            }
                        }
                    }
                    try {
                        travelPlanDao.deleteTravelPlan(travelPlan);
                        message = "删除成功！";
                    } catch (Exception e) {
                        System.out.println(e);
                        status = "error";
                        message = "删除失败，操作数据库失败！";
                        if (!isDeleteDayPlanError) {
                            message += "但该旅行计划下的所有日计划已经被删除！";
                        } else {
                            message += "该旅行计划下的部分日计划可能已经被删除！";
                        }
                    }
                } else {
                    status = "error";
                    message = "该计划不是该用户的，不能删除！";
                }
            }
        }
        jsonObject.accumulate("status", status);
        jsonObject.accumulate("message", message);
        return jsonObject.toString();
    }

    public String getDayPlanByTravelId(String travelId, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            status = "error";
            String message = "用户未登录！";
            jsonObject.accumulate("message", message);
        } else {
            if (travelPlanDao.getCountByTravelId(travelId) == 0) {
                status = "error";
                String message = "没有该旅行计划！";
                jsonObject.accumulate("message", message);
            } else {
                TravelPlan travelPlan = travelPlanDao.getTravelPlanByTravelId(travelId);
                User user = (User)session.getAttribute("user");
                if (travelPlan.getUserId().equals(user.getUserId())) {
                    if (dayPlanDao.getCountByTravelId(travelId) == 0) {
                        status = "error";
                        String message = "该旅行计划没有日计划！";
                        jsonObject.accumulate("message", message);
                    } else {
                        List<DayPlan> dayPlanList = dayPlanDao.getDayPlanByTravelId(travelId);
                        ArrayList<JSONObject> dayPlanArrayList = new ArrayList<>();
                        for (DayPlan dayPlan : dayPlanList) {
                            dayPlanArrayList.add(dayPlan.toJson());
                        }
                        jsonObject.accumulate("day_plan_list", dayPlanArrayList);
                    }
                } else {
                    status = "error";
                    String message = "该计划不是该用户所建立的，不能获得信息！";
                    jsonObject.accumulate("message", message);
                }
            }
        }
        jsonObject.accumulate("status", status);
        return jsonObject.toString();
    }

    public String getDayPlanByDayId(String dayId, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            status = "error";
            String message = "用户未登录！";
            jsonObject.accumulate("message", message);
        } else {
            if (dayPlanDao.getCountByDayId(dayId) == 0) {
                status = "error";
                String message = "没有该旅行日计划！";
                jsonObject.accumulate("message", message);
            } else {
                DayPlan dayPlan = dayPlanDao.getDayPlanByDayId(dayId);
                TravelPlan travelPlan = travelPlanDao.getTravelPlanByTravelId(dayPlan.getTravelId());
                User user = (User)session.getAttribute("user");
                if (travelPlan.getUserId().equals(user.getUserId())) {
                    jsonObject.accumulate("day_plan", dayPlan.toJson());
                } else {
                    status = "error";
                    String message = "该旅行日计划不是该用户的，不能查看！";
                    jsonObject.accumulate("message", message);
                }
            }
        }
        jsonObject.accumulate("status", status);
        return jsonObject.toString();
    }

    public String updateDayPlan(String dayId, String scenicAreaId, String vehicle, String beginTime, String endTime, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        String message;
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            status = "error";
            message = "用户未登录！";
        } else {
            if (dayPlanDao.getCountByDayId(dayId) == 0) {
                status = "error";
                message = "没有该旅行日计划！";
            } else {
                DayPlan dayPlan = dayPlanDao.getDayPlanByDayId(dayId);
                TravelPlan travelPlan = travelPlanDao.getTravelPlanByTravelId(dayPlan.getTravelId());
                User user = (User)session.getAttribute("user");
                if (travelPlan.getUserId().equals(user.getUserId())) {
                    dayPlan.setScenicAreaId(scenicAreaId);
                    dayPlan.setVehicle(vehicle);
                    dayPlan.setBeginTime(beginTime);
                    dayPlan.setEndTime(endTime);
                    try {
                        dayPlanDao.updateDayPlan(dayPlan);
                        message = "修改成功！";
                    } catch (Exception e) {
                        System.out.println(e);
                        status = "error";
                        message = "修改失败，操作数据库出错！";
                    }
                } else {
                    status = "error";
                    message = "该旅行计划不属于该用户，无法修改！";
                }
            }
        }
        jsonObject.accumulate("status", status);
        jsonObject.accumulate("message", message);
        return jsonObject.toString();
    }

    public String deleteDayPlan(String dayId, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        String message;
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            status = "error";
            message = "用户未登录！";
        } else {
            if (dayPlanDao.getCountByDayId(dayId) == 0) {
                status = "error";
                message = "没有该旅行日计划！";
            } else {
                DayPlan dayPlan = dayPlanDao.getDayPlanByDayId(dayId);
                TravelPlan travelPlan = travelPlanDao.getTravelPlanByTravelId(dayPlan.getTravelId());
                User user = (User)session.getAttribute("user");
                if (travelPlan.getUserId().equals(user.getUserId())) {
                    try {
                        dayPlanDao.deleteDayPlan(dayPlan);
                        message = "删除成功！";
                    } catch (Exception e) {
                        System.out.println(e);
                        status = "error";
                        message = "删除失败，操作数据库出错！";
                    }
                } else {
                    status = "error";
                    message = "该旅行计划不是该用户的，不能删除！";
                }
            }
        }
        jsonObject.accumulate("status", status);
        jsonObject.accumulate("message", message);
        return jsonObject.toString();
    }
}
