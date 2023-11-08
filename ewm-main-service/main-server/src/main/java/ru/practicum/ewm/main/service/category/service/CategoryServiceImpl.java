package ru.practicum.ewm.main.service.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.main.service.category.dto.CategoryDto;
import ru.practicum.ewm.main.service.category.mapper.CategoryMapper;
import ru.practicum.ewm.main.service.category.model.Category;
import ru.practicum.ewm.main.service.category.repository.CategoryRepository;
import ru.practicum.ewm.main.service.event.model.Event;
import ru.practicum.ewm.main.service.event.repository.EventRepository;
import ru.practicum.ewm.main.service.exception.ConflictException;
import ru.practicum.ewm.main.service.exception.EntityNotFoundException;
import ru.practicum.ewm.main.service.util.Pagination;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CategoryDto create(CategoryDto categoryDto) {
        try {
            Category category = categoryMapper.categoryFromDto(categoryDto);
            Category categorySaved = categoryRepository.save(category);
            return categoryMapper.categoryToDto(categorySaved);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Категория с таким именем уже существует.");
        }
    }

    @Override
    @Transactional
    public CategoryDto update(Long catId, CategoryDto categoryDto) {
        Category category = getOrThrow(catId);
        categoryMapper.updateCategoryFromDto(categoryDto, category);

        try {
            category = categoryRepository.saveAndFlush(category);
        } catch (RuntimeException e) {
            throw new ConflictException(e.getMessage());
        }

        return categoryMapper.categoryToDto(category);
    }

    @Override
    @Transactional
    public void delete(Long catId) {
        getOrThrow(catId);

        List<Event> events = eventRepository.findAllByCategoryId(catId);

        if (events.isEmpty()) {
            categoryRepository.deleteById(catId);
        } else {
            throw new ConflictException("Категория не пуста");
        }
    }

    @Override
    public List<CategoryDto> getAllCategories(Integer from, Integer size) {
        Pagination page = new Pagination(from, size);

        return categoryRepository.findAll(page)
                .stream()
                .map(categoryMapper::categoryToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategory(Long catId) {
        return categoryMapper.categoryToDto(getOrThrow(catId));
    }

    private Category getOrThrow(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new EntityNotFoundException(Category.class, String.format("ID: %s", catId)));
    }
}
