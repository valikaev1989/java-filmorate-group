
/*INSERT INTO USERR (EMAIL, LOGIN, USER_NAME, BIRTH_DATE)
VALUES ('user@gmail.com', 'user', 'firstuser', '1999-12-05');*/

-- INSERT INTO DIRECTORS (DIRECTOR_ID, DIRECTOR_NAME)
-- VALUES ('1', 'DIRECTOR1');
-- INSERT INTO DIRECTORS (DIRECTOR_ID, DIRECTOR_NAME)
-- VALUES ('2', 'DIRECTOR2');
-- INSERT INTO FILM (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION_FILM)
-- VALUES ('Сияние', 'Фильм Стивена Кинга', '1980-05-23', 144);
-- INSERT INTO FILM (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION_FILM)
-- VALUES ('Второй', 'Описание второго фильма', '1990-05-23', 150);
-- INSERT INTO FILMS_DIRECTORS (FILM_ID, DIRECTOR_ID)
-- VALUES ('1', '1');
-- MERGE INTO FILMS_DIRECTORS (FILM_ID, DIRECTOR_ID)
-- VALUES ('2', '1');
-- MERGE INTO FILMS_DIRECTORS (FILM_ID, DIRECTOR_ID)
--     VALUES ('1', '2');
-- INSERT INTO USERR (USER_ID, EMAIL, LOGIN, USER_NAME, BIRTH_DATE)
-- VALUES ('1', 'user@gmail.com', 'user', 'firstuser', '1999-12-05');
-- INSERT INTO USERR (USER_ID, EMAIL, LOGIN, USER_NAME, BIRTH_DATE)
-- VALUES ('2', 'user2@gmail.com', 'user2', 'seconduser', '1990-12-05');
-- INSERT INTO USERR (USER_ID, EMAIL, LOGIN, USER_NAME, BIRTH_DATE)
-- VALUES ('3', 'user3@gmail.com', 'user3', 'thirduser', '1990-12-05');
-- INSERT INTO LIKES (FILM_ID, USER_ID)
-- VALUES ('1', '1');
-- MERGE INTO LIKES (FILM_ID, USER_ID)
--     VALUES ('1', '2');
-- MERGE INTO LIKES (FILM_ID, USER_ID)
--     VALUES ('1', '3');
-- INSERT INTO LIKES (FILM_ID, USER_ID)
-- VALUES ('2', '2');
-- SELECT COUNT(DISTINCT User_ID)
-- FROM LIKES;
--
-- SELECT F.FILM_ID,F.film_name, F.description, F.release_date, F.duration_film, F.mpa_id
-- FROM FILMS_DIRECTORS FD
-- LEFT JOIN FILM F on  fd.FILM_ID=F.FILM_ID
-- LEFT JOIN MPA M on F.MPA_ID = M.MPA_ID
-- WHERE fd.director_id = 1
-- ORDER BY RELEASE_DATE DESC;
-- --
-- SELECT F.FILM_ID,F.film_name, F.description, F.release_date, F.duration_film, F.mpa_id
-- FROM FILMS_DIRECTORS FD
-- LEFT JOIN FILM F on  fd.FILM_ID=F.FILM_ID
-- LEFT JOIN MPA M on F.MPA_ID = M.MPA_ID
-- LEFT JOIN LIKES L on F.FILM_ID = L.FILM_ID
-- WHERE fd.director_id = 2
-- GROUP BY F.FILM_ID
-- ORDER BY COUNT(L.USER_ID) DESC;

-- SELECT *, count(*) CountLikes
-- FROM FILM F
--          LEFT JOIN LIKES L on F.FILM_ID = L.FILM_ID
-- WHERE F.FILM_ID IN (
--     SELECT FILM_ID
--     FROM FILMS_DIRECTORS
--     WHERE DIRECTOR_ID = 1
--     )
-- group by F.FILM_ID
-- ORDER BY CountLikes desc;
-- INSERT INTO likes(film_id, user_id)
-- VALUES(2, 1);
-- INSERT INTO likes(film_id, user_id)
-- VALUES(1, 2);
--
-- SELECT * FROM film
-- LEFT JOIN (SELECT film_id, user_id, COUNT(*) likes_count FROM LIKES GROUP BY user_id, film_id)
-- l ON film.film_id = l.film_id
-- WHERE l.user_id = 1
-- ORDER BY l.likes_count DESC;



/*INSERT INTO userr (EMAIL, LOGIN, USER_NAME, BIRTH_DATE)
VALUES ( 'user@gmail.com', 'friend', 'seconduser', '1958-12-05' );


INSERT INTO GENRE (NAME)
VALUES ( 'Боевик' );
INSERT INTO GENRE (NAME)
VALUES ( 'Мелодрама' );
INSERT INTO GENRE (NAME)
VALUES ( 'Детектив' );434
INSERT INTO GENRE (NAME)
VALUES ( 'Триллер' );
INSERT INTO GENRE (NAME)
VALUES ( 'Ужасы' );


INSERT INTO FILM (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION_FILM)
VALUES ( 'Сияние', 'Фильм Стивена Кинга', '1980-05-23', 144);
INSERT INTO FILM (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION_FILM)
VALUES ( 'Второй', 'Описание второго фильма', '1990-05-23', 150);*/


/*INSERT INTO RATING(rating_id, rating_name) VALUES ( 1, 'G' );
INSERT INTO RATING(rating_id, rating_name) VALUES ( 2, 'PG' );
INSERT INTO RATING(rating_id, rating_name) VALUES ( 3, 'PG-13' );
INSERT INTO RATING(rating_id, rating_name) VALUES ( 4, 'R' );
INSERT INTO RATING(rating_id, rating_name) VALUES ( 5, 'NC-17' );*/


