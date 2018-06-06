package com.gxg.dao;

import com.gxg.entities.PostCard;
import com.sun.corba.se.impl.logging.POASystemException;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by 郭欣光 on 2018/6/4.
 */

@Repository
public class PostCardDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addPostCard(PostCard postCard) {
        String sql = "insert into post_card values(?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, postCard.getPcId(), postCard.getCityId(), postCard.getUserId(), postCard.getImage(), postCard.getSendWord());
    }

    public int getCountByUserId(String userId) {
        String sql = "select count(*) from post_card where user_id=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return rowCount;
    }

    public List<PostCard> getPostCardByUserId(String userId) {
        String sql = "select * from post_card where user_id=?";
        List<PostCard> postCardList = jdbcTemplate.query(sql, new RowMapper<PostCard>() {
            @Override
            public PostCard mapRow(ResultSet resultSet, int i) throws SQLException {
                PostCard postCard = new PostCard();
                postCard.setPcId(resultSet.getString("pc_id"));
                postCard.setCityId(resultSet.getString("city_id"));
                postCard.setUserId(resultSet.getString("user_id"));
                postCard.setImage(resultSet.getString("image"));
                postCard.setSendWord(resultSet.getString("send_word"));
                return postCard;
            }
        }, userId);
        return postCardList;
    }

    public int getCountByPcId(String pcId) {
        String sql = "select count(*) from post_card where pc_id=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, pcId);
        return rowCount;
    }

    public PostCard getPostCardByPcId(String pcId) {
        String sql = "select * from post_card where pc_id=?";
        PostCard postCard = jdbcTemplate.queryForObject(sql, new RowMapper<PostCard>() {
            @Override
            public PostCard mapRow(ResultSet resultSet, int i) throws SQLException {
                PostCard postCard = new PostCard();
                postCard.setPcId(resultSet.getString("pc_id"));
                postCard.setCityId(resultSet.getString("city_id"));
                postCard.setUserId(resultSet.getString("user_id"));
                postCard.setImage(resultSet.getString("image"));
                postCard.setSendWord(resultSet.getString("send_word"));
                return postCard;
            }
        }, pcId);
        return postCard;
    }

    public void updatePostCard(PostCard postCard) {
        String sql = "update post_card set city_id=?, user_id=?, image=?, send_word=? where pc_id=?";
        jdbcTemplate.update(sql, postCard.getCityId(), postCard.getUserId(), postCard.getImage(), postCard.getSendWord(), postCard.getPcId());
    }

    public void deletePostCard(PostCard postCard) {
        String sql = "delete from post_card where pc_id=?";
        jdbcTemplate.update(sql, postCard.getPcId());
    }
}
