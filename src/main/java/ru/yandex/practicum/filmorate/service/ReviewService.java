package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    ReviewStorage reviewStorage;

    @Autowired
    public ReviewService(ReviewStorage reviewStorage) {
        this.reviewStorage = reviewStorage;
    }

    public Review addReview(Review review) {
        long id = reviewStorage.addReview(review);
        review.setReviewId(id);
        return review;
    }

    public Review changeReview(Review review) {
        reviewStorage.changeReview(review);
        return review;
    }

    public void deleteReview(long id) {
        reviewStorage.deleteReview(id);
    }

    public Review getReviewById(long id) {
        return reviewStorage.getReviewById(id);
    }

    public List<Review> getReviewByFilmId(Optional<Long> filmId, int count) {
        List<Review> allReviews = reviewStorage.getReviewByFilmId(filmId, count);
        allReviews.sort((o1, o2) -> o2.getUseful() - o1.getUseful());
        return allReviews;
    }

    public void addLike(long id, long userId, boolean islike) {
        reviewStorage.addLike(id, userId, islike);
    }

    public void deleteLike(long id, long userId, boolean isLike) {
        reviewStorage.deleteLike(id, userId, isLike);
    }
}
