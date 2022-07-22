package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewStorage {
    long addReview(Review review);

    void changeReview(Review review);

    void deleteReview(long id);

    Review getReviewById(long id);

    List<Review> getReviewByFilmId(Optional<Long> filmId, int count);

    void addLike(long id, long userId, boolean isLike);

    void deleteLike(long id, long userId, boolean isLike);
}
