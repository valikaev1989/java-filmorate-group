CREATE TABLE IF NOT EXISTS directors
(
    director_Id   bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    director_name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS genre
(

    genre_id INTEGER,
    name     varchar(50),
    PRIMARY KEY (genre_id)
);

CREATE TABLE IF NOT EXISTS film
(
    film_id       INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    film_name     VARCHAR(50) NOT NULL,
    description   VARCHAR(1000),
    release_date  DATE,
    duration_film INTEGER,
    rate          INTEGER,
    mpa_id        INTEGER
);

CREATE TABLE IF NOT EXISTS mpa
(
    mpa_id   INTEGER,
    mpa_name VARCHAR(50),
    PRIMARY KEY (mpa_id)
);

create table if not exists genre_and_film
(
    film_id  int references film (film_id) ON DELETE CASCADE,
    genre_id int references genre (genre_id) ON DELETE CASCADE,
    PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE if not exists films_directors
(
    film_Id     INTEGER REFERENCES film (film_id) ON DELETE CASCADE,
    director_Id INTEGER REFERENCES directors (director_Id) ON DELETE CASCADE,
    CONSTRAINT films_directors PRIMARY KEY (film_id, director_id)
);

CREATE TABLE IF NOT EXISTS userr
(
    user_id    INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email      VARCHAR(50) NOT NULL,
    login      VARCHAR(50) NOT NULL,
    user_name  VARCHAR(50),
    birth_date DATE
);


CREATE TABLE IF NOT EXISTS events
(
    event_id        BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    event_user_id         BIGINT      NOT NULL,
    event_type      VARCHAR(50) NOT NULL,
    event_operation VARCHAR(50) NOT NULL,
    time_stamp      BIGINT      NOT NULL,
    entity_id       BIGINT      NOT NULL,
    FOREIGN KEY (event_user_id) REFERENCES userr (user_id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS likes
(
    film_id INTEGER REFERENCES film (film_id) ON DELETE CASCADE,
    user_id INTEGER REFERENCES userr (user_id) ON DELETE CASCADE,

    PRIMARY KEY (film_id, user_id)

);

CREATE TABLE IF NOT EXISTS friends
(
    user_id   INTEGER REFERENCES userr (user_id) ON DELETE CASCADE,
    friend_id INTEGER REFERENCES userr (user_id) ON DELETE CASCADE,
    approval  INTEGER,
    PRIMARY KEY (user_id, friend_id)
);

merge into mpa key (mpa_id)
values (1, 'G'),
       (2, 'PG'),
       (3, 'PG-13'),
       (4, 'R'),
       (5, 'NC-17');
merge into GENRE key (GENRE_ID)
values (1, 'Комедия'),
       (2, 'Драма'),
       (3, 'Мультфильм'),
       (4, 'Ужасы'),
       (5, 'Триллер'),
       (6, 'Детектив');

CREATE TABLE IF NOT EXISTS reviews
(
    review_id   INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    content     TEXT,
    is_positive BOOLEAN,
    user_id     INTEGER REFERENCES userr (user_id) on DELETE cascade,
    film_id     INTEGER REFERENCES film (film_id) on delete cascade,
    useful      INTEGER
    /*FOREIGN KEY (user_id) REFERENCES userr (user_id) ON DELETE CASCADE,
    FOREIGN KEY (film_id) REFERENCES film (film_id) ON DELETE CASCADE*/
);

CREATE TABLE IF NOT EXISTS review_likes
(
    review_id INTEGER REFERENCES reviews ON DELETE CASCADE,
    user_id   INTEGER REFERENCES userr ON DELETE CASCADE,
    is_like   BOOLEAN,
    PRIMARY KEY (review_id, user_id)
);
