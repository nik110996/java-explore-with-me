package ru.practicum.ewm.main.service.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.service.compilation.dto.CompilationDto;
import ru.practicum.ewm.main.service.compilation.dto.CompilationNewDto;
import ru.practicum.ewm.main.service.compilation.dto.CompilationUpdateDto;
import ru.practicum.ewm.main.service.compilation.service.CompilationService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CompilationControllerAdmin {

    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto create(@RequestBody @Valid CompilationNewDto compilationNewDto) {

        log.info("Пришел запрос / эндпоинт: '{} {} с телом {}'",
                "POST", "/admin/compilations", compilationNewDto);
        CompilationDto createdCompilation = compilationService.create(compilationNewDto);
        log.info("Получен ответ / эндпоинт: '{} {}' с телом '{}", "POST", "/admin/compilations", createdCompilation);
        return createdCompilation;
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long compId) {
        log.info("Пришел запрос / эндпоинт: '{} {}'",
                "DELETE", "/admin/compilations/" + compId);
        compilationService.delete(compId);
        log.info("Пришел ответ / эндпоинт: '{} {}'",
                "DELETE", "/admin/compilations/" + compId);
    }

    @PatchMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto update(@PathVariable Long compId,
                                 @RequestBody @Valid CompilationUpdateDto compilationUpdateDto) {
        log.info("Пришел запрос / эндпоинт: '{} {} с телом {}'",
                "PATCH", "/admin/compilations/" + compId, compilationUpdateDto);
        CompilationDto updatedCompilation = compilationService.update(compId, compilationUpdateDto);
        log.info("Получен ответ / эндпоинт: '{} {}' с телом '{}", "PATCH", "/admin/compilations/", updatedCompilation);
        return updatedCompilation;
    }
}
