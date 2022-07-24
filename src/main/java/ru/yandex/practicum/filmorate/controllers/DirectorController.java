package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/directors")
public class DirectorController {

    private final DirectorService directorService;

    @Autowired
    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    @GetMapping
    public List<Director> getAllDirectors() {
        log.info("Получен запрос к эндпоинту /directors. Метод GET");
        return directorService.getAllDirectors();
    }

    @PostMapping
    public Director addDirector(@RequestBody Director director) {
        log.info("Получен запрос к эндпоинту /directors. Метод POST");
        return directorService.addDirector(director);
    }

    @GetMapping("/{id}")
    public Director getDirector(@PathVariable("id") Long directorId) {
        log.info("Получен запрос к эндпоинту /directors/id. Метод GET");
        return directorService.getDirector(directorId);
    }

    @PutMapping
    public Director updateDirector(@RequestBody Director director) {
        log.info("Получен запрос к эндпоинту /directors. Метод PUT");
        return directorService.updateDirector(director);
    }

    @DeleteMapping("/{id}")
    public void deleteDirector(@PathVariable("id") Long directorId) {
        log.info("Получен запрос к эндпоинту /directors/id. Метод DELETE");
        directorService.deleteDirector(directorId);
    }
}
