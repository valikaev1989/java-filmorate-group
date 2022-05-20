# java-filmorate  
![QuickDBD-export](https://user-images.githubusercontent.com/95374153/169587648-34a35dac-7c3a-4618-a211-20781340dc7f.png)  
__Выбрать фильм__  
SELECT film_name  
FROM Film  
WHERE film_id = [требуемый id]  
__Список популярных фильмов__  
SELECT film_id  
FROM Likes  
GROUP BY film_id   
HAVING COUNT(user_id) ORDER BY film_id DESC  
LIMIT [требуемое количество]  
__Выбрать юзера__  
SELECT friend_id  
FROM FRIENDS  
WHERE [требуемый id]  


