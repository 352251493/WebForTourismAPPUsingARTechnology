package com.gxg.dao;

import com.gxg.entities.DayPlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by 郭欣光 on 2018/6/3.
 */

@Repository
public class DayPlanDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addDayPlan(DayPlan dayPlan) {
        String sql = "insert into day_plan values(?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, dayPlan.getDayId(), dayPlan.getTravelId(), dayPlan.getScenicAreaId(), dayPlan.getVehicle(), dayPlan.getBeginTime(), dayPlan.getEndTime());
    }

    public int getCountByTravelId(String travelId) {
        String sql = "select count(*) from day_plan where travel_id=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, travelId);
        return rowCount;
    }

    public List<DayPlan> getDayPlanByTravelId(String travelId) {
        String sql = "select * from day_plan where travel_id=?";
        List<DayPlan> dayPlanList = jdbcTemplate.query(sql, new RowMapper<DayPlan>() {
            @Override
            public DayPlan mapRow(ResultSet resultSet, int i) throws SQLException {
                DayPlan dayPlan = new DayPlan();
                dayPlan.setDayId(resultSet.getString("day_id"));
                dayPlan.setTravelId(resultSet.getString("travel_id"));
                dayPlan.setScenicAreaId(resultSet.getString("scenic_area_id"));
                dayPlan.setVehicle(resultSet.getString("vehicle"));
                dayPlan.setBeginTime(resultSet.getString("begin_time"));
                dayPlan.setEndTime(resultSet.getString("end_time"));
                return dayPlan;
            }
        }, travelId);
        return dayPlanList;
    }

    public void deleteDayPlan(DayPlan dayPlan) {
        String sql = "delete from day_plan where day_id=?";
        jdbcTemplate.update(sql, dayPlan.getDayId());
    }

    public int getCountByDayId(String dayId) {
        String sql = "select count(*) from day_plan where day_id=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, dayId);
        return rowCount;
    }

    public DayPlan getDayPlanByDayId(String dayId) {
        String sql = "select * from day_plan where day_id=?";
        DayPlan dayPlan = jdbcTemplate.queryForObject(sql, new RowMapper<DayPlan>() {
            @Override
            public DayPlan mapRow(ResultSet resultSet, int i) throws SQLException {
                DayPlan dayPlan = new DayPlan();
                dayPlan.setDayId(resultSet.getString("day_id"));
                dayPlan.setTravelId(resultSet.getString("travel_id"));
                dayPlan.setScenicAreaId(resultSet.getString("scenic_area_id"));
                dayPlan.setVehicle(resultSet.getString("vehicle"));
                dayPlan.setBeginTime(resultSet.getString("begin_time"));
                dayPlan.setEndTime(resultSet.getString("end_time"));
                return dayPlan;
            }
        }, dayId);
        return dayPlan;
    }

    public void updateDayPlan(DayPlan dayPlan) {
        String sql = "update day_plan set travel_id=?, scenic_area_id=?, vehicle=?, begin_time=?, end_time=? where day_id=?";
        jdbcTemplate.update(sql, dayPlan.getTravelId(), dayPlan.getScenicAreaId(), dayPlan.getVehicle(), dayPlan.getBeginTime(), dayPlan.getEndTime(), dayPlan.getDayId());
    }
}
