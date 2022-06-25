# java-filmorate  
![QuickDBD-export](https://user-images.githubusercontent.com/95374153/169667199-baf6b561-1407-4d54-81af-6968c32e962a.png)
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


