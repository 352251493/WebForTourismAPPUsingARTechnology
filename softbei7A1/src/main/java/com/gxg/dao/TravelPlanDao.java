package com.gxg.dao;

import com.gxg.entities.TravelPlan;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;
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
public class TravelPlanDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addTravelPlan(TravelPlan travelPlan) {
        String sql = "insert into travel_plan values(?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, travelPlan.getCityId(), travelPlan.getVehicle(), travelPlan.getBeginDate(), travelPlan.getEndDate(), travelPlan.getTravelId(), travelPlan.getUserId());
    }

    public int getCountByUserId(String userId) {
        String sql = "select count(*) from travel_plan where user_id=?";
        int  rowCount = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return rowCount;
    }

    public List<TravelPlan> getTravelPlanByUserId(String userId) {
        String sql = "select * from travel_plan where user_id=?";
        List<TravelPlan> travelPlanList = jdbcTemplate.query(sql, new RowMapper<TravelPlan>() {
            @Override
            public TravelPlan mapRow(ResultSet resultSet, int i) throws SQLException {
                TravelPlan travelPlan = new TravelPlan();
                travelPlan.setCityId(resultSet.getString("city_id"));
                travelPlan.setVehicle(resultSet.getString("vehicle"));
                travelPlan.setBeginDate(resultSet.getString("begin_date"));
                travelPlan.setEndDate(resultSet.getString("end_date"));
                travelPlan.setTravelId(resultSet.getString("travel_id"));
                travelPlan.setUserId(resultSet.getString("user_id"));
                return travelPlan;
            }
        }, userId);
        return travelPlanList;
    }

    public int getCountByTravelId(String travelId) {
        String sql = "select count(*) from travel_plan where travel_id=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, travelId);
        return rowCount;
    }

    public TravelPlan getTravelPlanByTravelId(String travelId) {
        String sql = "select * from travel_plan where travel_id=?";
        TravelPlan travelPlan = jdbcTemplate.queryForObject(sql, new RowMapper<TravelPlan>() {
            @Override
            public TravelPlan mapRow(ResultSet resultSet, int i) throws SQLException {
                TravelPlan travelPlan = new TravelPlan();
                travelPlan.setCityId(resultSet.getString("city_id"));
                travelPlan.setVehicle(resultSet.getString("vehicle"));
                travelPlan.setBeginDate(resultSet.getString("begin_date"));
                travelPlan.setEndDate(resultSet.getString("end_date"));
                travelPlan.setTravelId(resultSet.getString("travel_id"));
                travelPlan.setUserId(resultSet.getString("user_id"));
                return travelPlan;
            }
        }, travelId);
        return travelPlan;
    }

    public void updateTravelPlan(TravelPlan travelPlan) {
        String sql = "update travel_plan set city_id=?, vehicle=?, begin_date=?, end_date=?, user_id=? where travel_id=?";
        jdbcTemplate.update(sql, travelPlan.getCityId(), travelPlan.getVehicle(), travelPlan.getBeginDate(), travelPlan.getEndDate(), travelPlan.getUserId(), travelPlan.getTravelId());
    }

    public void deleteTravelPlan(TravelPlan travelPlan) {
        String sql = "delete from travel_plan where travel_id=?";
        jdbcTemplate.update(sql, travelPlan.getTravelId());
    }

    public int getCountByCityId(String cityId) {
        String sql = "select count(*) from travel_plan where city_id=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, cityId);
        return rowCount;
    }

    public List<TravelPlan> getTravelPlanByCityId(String cityId) {
        String sql = "select * from travel_plan where city_id=?";
        List<TravelPlan> travelPlanList = jdbcTemplate.query(sql, new RowMapper<TravelPlan>() {
            @Override
            public TravelPlan mapRow(ResultSet resultSet, int i) throws SQLException {
                TravelPlan travelPlan = new TravelPlan();
                travelPlan.setCityId(resultSet.getString("city_id"));
                travelPlan.setVehicle(resultSet.getString("vehicle"));
                travelPlan.setBeginDate(resultSet.getString("begin_date"));
                travelPlan.setEndDate(resultSet.getString("end_date"));
                travelPlan.setTravelId(resultSet.getString("travel_id"));
                travelPlan.setUserId(resultSet.getString("user_id"));
                return travelPlan;
            }
        }, cityId);
        return travelPlanList;
    }
}
