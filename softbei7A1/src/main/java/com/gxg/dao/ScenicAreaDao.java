package com.gxg.dao;

import com.gxg.entities.ScenicArea;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ScenicAreaDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addScenicArea(ScenicArea scenicArea) {
        String sql = "insert into scenic_area values(?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, scenicArea.getSaId(), scenicArea.getCityId(), scenicArea.getSaImg(), scenicArea.getSaAr(), scenicArea.getSaIntro());
    }

    public int getCountByCityId(String cityId) {
        String sql = "select count(*) from scenic_area where city_id=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, cityId);
        return rowCount;
    }

    public List<ScenicArea> getScenicAreaByCityId(String cityId) {
        String sql = "select * from scenic_area where city_id=?";
        List<ScenicArea> scenicAreaList = jdbcTemplate.query(sql, new RowMapper<ScenicArea>() {
            @Override
            public ScenicArea mapRow(ResultSet resultSet, int i) throws SQLException {
                ScenicArea scenicArea = new ScenicArea();
                scenicArea.setSaId(resultSet.getString("sa_id"));
                scenicArea.setCityId(resultSet.getString("city_id"));
                scenicArea.setSaImg(resultSet.getString("sa_img"));
                scenicArea.setSaAr(resultSet.getString("sa_ar"));
                scenicArea.setSaIntro(resultSet.getString("sa_intro"));
                return scenicArea;
            }
        }, cityId);
        return scenicAreaList;
    }

    public int getCountBySaId(String saId) {
        String sql = "select count(*) from scenic_area where sa_id=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, saId);
        return rowCount;
    }

    public ScenicArea getScenicAreaBySaId(String saId) {
        String sql = "select * from scenic_area where sa_id=?";
        ScenicArea scenicArea = jdbcTemplate.queryForObject(sql, new RowMapper<ScenicArea>() {
            @Override
            public ScenicArea mapRow(ResultSet resultSet, int i) throws SQLException {
                ScenicArea scenicArea = new ScenicArea();
                scenicArea.setSaId(resultSet.getString("sa_id"));
                scenicArea.setCityId(resultSet.getString("city_id"));
                scenicArea.setSaImg(resultSet.getString("sa_img"));
                scenicArea.setSaAr(resultSet.getString("sa_ar"));
                scenicArea.setSaIntro(resultSet.getString("sa_intro"));
                return scenicArea;
            }
        }, saId);
        return scenicArea;
    }

    public int getCount() {
        String sql = "select count(*) from scenic_area";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class);
        return rowCount;
    }

    public List<ScenicArea> getAllScenicArea() {
        String sql = "select * from scenic_area";
        List<ScenicArea> scenicAreaList = jdbcTemplate.query(sql, new RowMapper<ScenicArea>() {
            @Override
            public ScenicArea mapRow(ResultSet resultSet, int i) throws SQLException {
                ScenicArea scenicArea = new ScenicArea();
                scenicArea.setSaId(resultSet.getString("sa_id"));
                scenicArea.setCityId(resultSet.getString("city_id"));
                scenicArea.setSaImg(resultSet.getString("sa_img"));
                scenicArea.setSaAr(resultSet.getString("sa_ar"));
                scenicArea.setSaIntro(resultSet.getString("sa_intro"));
                return scenicArea;
            }
        });
        return scenicAreaList;
    }

    public void updateScenicArea(ScenicArea scenicArea) {
        String sql = "update scenic_area set city_id=?, sa_img=?, sa_ar=?, sa_intro=? where sa_id=?";
        jdbcTemplate.update(sql, scenicArea.getCityId(),scenicArea.getSaImg(), scenicArea.getSaAr(), scenicArea.getSaIntro(), scenicArea.getSaId());
    }

    public void deeteScenicArea(ScenicArea scenicArea) {
        String sql = "delete from scenic_area where sa_id=?";
        jdbcTemplate.update(sql, scenicArea.getSaId());
    }
}
