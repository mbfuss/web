package com.example.web.controllers;

import com.example.web.models.Product;
import com.example.web.models.User;
import com.example.web.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

/**
 * Контроллер, отвечающий за обработку запросов связанных с продуктами
 */
@Controller
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    /**
     * Обработчик GET запроса по адресу "/"
     * Отображает список продуктов с возможностью поиска по названию
     * @param title ключевое слово для поиска
     * @param principal объект текущего пользователя
     * @param model объект для передачи данных в представление
     * @return страницу с продуктами
     */
    @GetMapping("/")
    public String products(@RequestParam(name = "searchWord", required = false) String title, Principal principal, Model model) {
        model.addAttribute("products", productService.listProducts(title));
        model.addAttribute("user", productService.getUserByPrincipal(principal));
        model.addAttribute("searchWord", title);
        return "products";
    }

    /**
     * Обработчик GET запроса по адресу "/product/{id}"
     * Отображает информацию о конкретном продукте
     *  id идентификатор продукта
     * model объект для передачи данных в представление
     * principal объект текущего пользователя
     *  страницу с информацией о продукте
     */
    @GetMapping("/product/{id}")
    public String productInfo(@PathVariable Long id, Model model, Principal principal) {
        Product product = productService.getProductById(id);
        model.addAttribute("user", productService.getUserByPrincipal(principal));
        model.addAttribute("product", product);
        model.addAttribute("images", product.getImages());
        model.addAttribute("authorProduct", product.getUser());
        return "product-info";
    }

    /**
     * Обработчик POST запроса по адресу "/product/create"
     * Создает новый продукт с изображениями
     * file1 первое изображение
     * file2 второе изображение
     *file3 третье изображение
     * product создаваемый продукт
     * principal объект текущего пользователя
     *  перенаправление на страницу с продуктами пользователя
     * throws IOException в случае ошибки ввода-вывода
     */
    @PostMapping("/product/create")
    public String createProduct(@RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2,
                                @RequestParam("file3") MultipartFile file3, Product product, Principal principal) throws IOException {
        productService.saveProduct(principal, product, file1, file2, file3);
        return "redirect:/my/products";
    }

    /**
     * Обработчик POST запроса по адресу "/product/delete/{id}"
     * Удаляет продукт по идентификатору
     *  id идентификатор удаляемого продукта
     *  principal объект текущего пользователя
     * @return перенаправление на страницу с продуктами пользователя
     */
    @PostMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id, Principal principal) {
        productService.deleteProduct(productService.getUserByPrincipal(principal), id);
        return "redirect:/my/products";
    }

    /**
     * Обработчик GET запроса по адресу "/my/products"
     * Отображает список продуктов пользователя
     *  principal объект текущего пользователя
     *   model объект для передачи данных в представление
     * return страницу с продуктами пользователя
     */
    @GetMapping("/my/products")
    public String userProducts(Principal principal, Model model) {
        User user = productService.getUserByPrincipal(principal);
        model.addAttribute("user", user);
        model.addAttribute("products", user.getProducts());
        return "my-products";
    }
}

