package com.gxg.dao;

import com.gxg.entities.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.Lifecycle;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by 郭欣光 on 2018/6/2.
 */

@Repository
public class CityDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addCity(City city) {
        String sql = "insert into city values(?, ?)";
        jdbcTemplate.update(sql, city.getCityId(), city.getCityName());
    }

    public int getCountByCityName(String cityName) {
        String sql = "select count(*) from city where city_name=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, cityName);
        return rowCount;
    }

    public int getCountByCityId(String cityId) {
        String sql = "select count(*) from city where city_id=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, cityId);
        return rowCount;
    }

    public int getCount() {
        String sql = "select count(*) from city";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class);
        return rowCount;
    }

    public List<City> getAllCity() {
        String sql = "select * from city";
        List<City> cityList = jdbcTemplate.query(sql, new RowMapper<City>() {
            @Override
            public City mapRow(ResultSet resultSet, int i) throws SQLException {
                City city = new City();
                city.setCityId(resultSet.getString("city_id"));
                city.setCityName(resultSet.getString("city_name"));
                return city;
            }
        });
        return cityList;
    }

    public City getCityByCityId(String cityId) {
        String sql = "select * from city where city_id=?";
        City city = jdbcTemplate.queryForObject(sql, new RowMapper<City>() {
            @Override
            public City mapRow(ResultSet resultSet, int i) throws SQLException {
                City city = new City();
                city.setCityId(resultSet.getString("city_id"));
                city.setCityName(resultSet.getString("city_name"));
                return city;
            }
        }, cityId);
        return city;
    }

    public City getCityByCityName(String cityName) {
        String sql = "select * from city where city_name=?";
        City city = jdbcTemplate.queryForObject(sql, new RowMapper<City>() {
            @Override
            public City mapRow(ResultSet resultSet, int i) throws SQLException {
                City city = new City();
                city.setCityId(resultSet.getString("city_id"));
                city.setCityName(resultSet.getString("city_name"));
                return city;
            }
        }, cityName);
        return city;
    }

    public void updateCity(City city) {
        String sql = "update city set city_name=? where city_id=?";
        jdbcTemplate.update(sql, city.getCityName(), city.getCityId());
    }

    public void deleteCity(City city) {
        String sql = "delete from city where city_id=?";
        jdbcTemplate.update(sql, city.getCityId());
    }
}
