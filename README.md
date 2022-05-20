# java-filmorate

![QuickDBD-export](https://user-images.githubusercontent.com/95374153/169535328-d2381829-e9b4-4741-ad27-899b5bc8fca7.png)

1. __Выбрать фильм__  
SELECT film_name  
FROM Film  
WHERE film_id = [требуемый id]  
2. __Список популярных фильмов__  
SELECT film_id  
FROM Likes  
GROUP BY film_id  
HAVING COUNT(user_id)
ORDER BY film_id DESC  
LIMIT [требуемое количество]  
3. __Выбрать юзера__  
SELECT friend_id  
FROM FRIENDS  
WHERE [требуемый id]
