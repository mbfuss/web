package com.example.web.models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, представляющий сущность продукта
 */
@Entity
@Table(name = "products")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Идентификатор продукта

    private String title; // Название продукта

    private String description; // Описание продукта

    private Integer price; // Цена продукта

    private String city; // Город, где находится продукт

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
            mappedBy = "product")
    private List<Image> images = new ArrayList<>(); // Список изображений продукта

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn
    private User user; // Пользователь, разместивший продукт

    private Long previewImageId; // Идентификатор предпросмотра изображения

    private LocalDateTime dateOfCreated; // Дата создания продукта

    /**
     * Метод, вызываемый перед сохранением в базу данных.
     * Устанавливает дату создания продукта на текущее время.
     */
    @PrePersist
    private void onCreate() {
        dateOfCreated = LocalDateTime.now();
    }

    /**
     * Метод для добавления изображения к продукту.
     * Устанавливает связь между изображением и продуктом.
     * Добавляет изображение в список изображений продукта.
     * @param image Изображение, которое нужно добавить к продукту
     */
    public void addImageToProduct(Image image) {
        image.setProduct(this);
        images.add(image);
    }
}
