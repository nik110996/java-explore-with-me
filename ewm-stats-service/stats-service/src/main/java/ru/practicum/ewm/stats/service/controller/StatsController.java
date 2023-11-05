package ru.practicum.ewm.stats.service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.stats.dto.HitDtoRequest;
import ru.practicum.ewm.stats.dto.ViewStatsResponseDto;
import ru.practicum.ewm.stats.service.service.StatsService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static ru.practicum.ewm.stats.dto.util.Constants.FORMATTER;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class StatsController {
    @Autowired
    private final StatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public HitDtoRequest saveStat(@RequestBody @Valid HitDtoRequest hitDtoRequest) {
        log.info("Пришел запрос / эндпоинт: '{} {} с телом {}'",
                "POST", "/hit", hitDtoRequest);
        HitDtoRequest savedHitDto = statsService.createStat(hitDtoRequest);
        log.info("Пришел ответ / эндпоинт: '{} {} с телом {}'",
                "POST", "/hit", savedHitDto);
        return savedHitDto;
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<ViewStatsResponseDto> getStats(@RequestParam String start,
                                               @RequestParam String end,
                                               @RequestParam(required = false) List<String> uris,
                                               @RequestParam(required = false, defaultValue = "false") Boolean unique,
                                               HttpServletRequest request) {
        log.info("Пришел запрос / эндпоинт: '{} {}'",
                "GET", "/stats");
        LocalDateTime startTime = LocalDateTime.parse(URLDecoder.decode(start, UTF_8), FORMATTER);
        LocalDateTime endTime = LocalDateTime.parse(URLDecoder.decode(end, UTF_8), FORMATTER);
        List<ViewStatsResponseDto> viewStatsResponseDtoList = statsService.getStats(startTime, endTime, uris, unique);
        log.info("Получен ответ / эндпоинт: '{} {}' с телом '{}", "GET", "/stats", viewStatsResponseDtoList);
        return viewStatsResponseDtoList;
    }
}
