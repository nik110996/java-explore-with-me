package ru.practicum.ewm.main.service.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.service.category.dto.CategoryDto;
import ru.practicum.ewm.main.service.category.service.CategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Validated
@Slf4j
public class CategoryControllerAdmin {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(@RequestBody @Valid CategoryDto categoryDto) {
        log.info("Пришел запрос / эндпоинт: '{} {} с телом {}'",
                "POST", "/admin/categories", categoryDto);
        CategoryDto savedCategory = categoryService.create(categoryDto);
        log.info("Получен ответ / эндпоинт: '{} {}' с телом '{}", "POST", "/admin/categories", savedCategory);
        return savedCategory;
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long catId) {
        log.info("Пришел запрос / эндпоинт: '{} {}'",
                "DELETE", "/admin/categories/" + catId);
        categoryService.delete(catId);
    }

    @PatchMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto update(@PathVariable Long catId,
                              @RequestBody @Valid CategoryDto categoryDto) {
        log.info("Пришел запрос / эндпоинт: '{} {}'",
                "PATCH", "/admin/categories/" + catId);
        CategoryDto updatedCategory = categoryService.update(catId, categoryDto);
        log.info("Получен ответ / эндпоинт: '{} {}' с телом '{}", "PATCH", "/admin/categories", updatedCategory);
        return updatedCategory;
    }
}
