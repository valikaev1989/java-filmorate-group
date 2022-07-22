package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class ReviewDbStorage implements ReviewStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ReviewDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long addReview(Review review) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reviews")
                .usingGeneratedKeyColumns("review_id");
        return insert.executeAndReturnKey(review.toMap()).longValue();
    }

    @Override
    public void changeReview(Review review) {
        String sql = "update reviews set content = ?, is_positive = ? " +
                "where review_id = ?";
        jdbcTemplate.update(sql, review.getContent(),
                review.getIsPositive(),
                review.getReviewId());
    }

    @Override
    public void deleteReview(long id) {
        String sql = "delete from reviews where review_id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Review getReviewById(long id) {
        String sql = "select * from reviews where review_id = ?";
        return jdbcTemplate.queryForObject(sql, ReviewDbStorage::mapRowToReview, id);
    }

    @Override
    public List<Review> getReviewByFilmId(long filmId, int count) {
        String sql = "select * from reviews where film_id = ? limit ?";
        return jdbcTemplate.query(sql, ReviewDbStorage::mapRowToReview, filmId, count);
    }

    @Override
    public List<Review> getCountReview(int count) {
        String sql = "select * from reviews limit ?";
        return jdbcTemplate.query(sql, ReviewDbStorage::mapRowToReview, count);
    }

    @Override
    public List<Review> getAllReview() {
        String sql = "select * from reviews";
        return jdbcTemplate.query(sql, ReviewDbStorage::mapRowToReview);
    }

    @Override
    public void addLike(long id, long userId, boolean isLike) {
        String sql = "merge into REVIEW_LIKES (review_id, user_id, is_like) key (REVIEW_ID, USER_ID) values ( ?, ?, ? )";
        jdbcTemplate.update(sql, id, userId, isLike);
        updateUseful(id);
    }

    @Override
    public void deleteLike(long id, long userId, boolean isLike) {
        String sql = "delete from review_likes " +
                "where review_id = ? and user_id = ?";
        jdbcTemplate.update(sql, id, userId);
        updateUseful(id);
    }

    private void updateUseful(long reviewId) {
        String sql = "update reviews set USEFUL = ? where REVIEW_ID = ?";
        jdbcTemplate.update(sql, calculateUseful(reviewId), reviewId);
    }

    private int calculateUseful(long reviewId) {
        String sqlQuery = "SELECT (SELECT COUNT (*) FROM review_likes WHERE review_id = ? AND is_like) " +
                "- (SELECT COUNT (*) FROM review_likes WHERE review_id = ? AND NOT is_like)";
        return jdbcTemplate.queryForObject(sqlQuery, Integer.class, reviewId, reviewId);
    }

    public static Review mapRowToReview(ResultSet resultSet, int rowNum) throws SQLException {
        long id = resultSet.getLong("review_id");
        String content = resultSet.getString("content");
        boolean isPositive = resultSet.getBoolean("is_positive");
        long userId = resultSet.getLong("user_id");
        long filmId = resultSet.getLong("film_id");
        int useful = resultSet.getInt("useful");
        return new Review(id, content, isPositive, userId, filmId, useful);
    }
}
