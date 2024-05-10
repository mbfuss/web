package com.example.web.controllers;

import com.example.web.models.Image;
import com.example.web.repositories.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;

/**
 * Контроллер, отвечающий за обработку запросов изображений
 */
@RestController
@RequiredArgsConstructor
public class ImageController {
    private final ImageRepository imageRepository; // Репозиторий для работы с изображениями

    /**
     * Обработчик GET запроса по адресу /images/{id}
     * Возвращает изображение по идентификатору
     * id идентификатор изображения
     *  ResponseEntity содержащий информацию и само изображение
     */
    @GetMapping("/images/{id}")
    private ResponseEntity<?> getImageById(@PathVariable Long id) {
        Image image = imageRepository.findById(id).orElse(null); // Получение изображения по идентификатору из репозитория

        return ResponseEntity.ok()
                .header("fileName", image.getOriginalFileName()) // Добавление имени файла в заголовок ответа
                .contentType(MediaType.valueOf(image.getContentType())) // Установка типа контента
                .contentLength(image.getSize()) // Установка длины контента
                .body(new InputStreamResource(new ByteArrayInputStream(image.getBytes()))); // Установка тела ответа как потокового ресурса из байтов изображения
    }
}
