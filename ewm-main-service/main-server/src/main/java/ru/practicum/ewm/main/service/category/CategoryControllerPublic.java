package ru.practicum.ewm.main.service.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.service.category.dto.CategoryDto;
import ru.practicum.ewm.main.service.category.service.CategoryService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Validated
@Slf4j
public class CategoryControllerPublic {

    private final CategoryService categoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto> getAllCategories(@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                              @Positive @RequestParam(defaultValue = "10") Integer size,
                                              HttpServletRequest request) {
        log.info("Пришел запрос / эндпоинт: '{} {} с параметром from: {} и с параметром size: {}'",
                "GET", "/categories", from, size);
        List<CategoryDto> categoryDtoList = categoryService.getAllCategories(from, size);
        log.info("Получен ответ / эндпоинт: '{} {}' с телом '{}", "GET", "/categories", categoryDtoList);
        return categoryDtoList;
    }

    @GetMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto getCategory(@PathVariable Long catId) {
        log.info("Пришел запрос / эндпоинт: '{} {}'",
                "GET", "/categories/" + catId);
        CategoryDto categoryDto = categoryService.getCategory(catId);
        log.info("Получен ответ / эндпоинт: '{} {}' с телом '{}", "GET", "/categories/" + catId, categoryDto);
        return categoryDto;
    }
}
