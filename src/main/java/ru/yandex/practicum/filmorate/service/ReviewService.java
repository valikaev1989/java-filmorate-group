package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ModelNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    ReviewStorage reviewStorage;

    UserStorage userStorage;

    FilmStorage filmStorage;

    @Autowired
    public ReviewService(ReviewStorage reviewStorage, UserStorage userStorage, FilmStorage filmStorage) {
        this.reviewStorage = reviewStorage;
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
    }

    public Review addReview(Review review) {
        if (isExistFilm(review.getFilmId()) && isExistUser(review.getUserId())) {
            long id = reviewStorage.addReview(review);
            review.setReviewId(id);
            return review;
        } else {
            throw new ModelNotFoundException("Model not found");
        }
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
        List<Review> allReviews;
        if (filmId.isPresent()) {
            allReviews = reviewStorage.getReviewByFilmId(filmId.get(), count);
        } else {
            allReviews = reviewStorage.getCountReview(count);
        }
        allReviews.sort((o1, o2) -> Integer.compare(o2.getUseful(), o1.getUseful()));
        return allReviews;
    }

    public void addLike(long id, long userId, boolean islike) {
        reviewStorage.addLike(id, userId, islike);
    }

    public void deleteLike(long id, long userId, boolean isLike) {
        reviewStorage.deleteLike(id, userId, isLike);
    }

    private boolean isExistUser(long id) {
        Optional<User> user = Optional.ofNullable(userStorage.findUserById(id));
        return user.isPresent();
    }

    private boolean isExistFilm(long id) {
        Optional<Film> film = Optional.ofNullable(filmStorage.getFilmById(id));
        return film.isPresent();
    }
}
