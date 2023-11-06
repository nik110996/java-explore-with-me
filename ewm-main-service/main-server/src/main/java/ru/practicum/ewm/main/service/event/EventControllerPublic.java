package ru.practicum.ewm.main.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.service.event.dto.EventFullDto;
import ru.practicum.ewm.main.service.event.dto.EventShortDto;
import ru.practicum.ewm.main.service.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.main.service.util.Constants.FORMATTER;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Validated
@Slf4j
public class EventControllerPublic {
    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getAllEvents(@RequestParam(required = false) String text,
                                            @RequestParam(required = false) List<Long> categories,
                                            @RequestParam(required = false) Boolean paid,
                                            @RequestParam(required = false) String rangeStart,
                                            @RequestParam(required = false) String rangeEnd,
                                            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                            @RequestParam(required = false) String sort,
                                            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                            @Positive @RequestParam(defaultValue = "10") Integer size,
                                            HttpServletRequest request) {
        log.info("Пришел запрос / эндпоинт: '{} {} с параметрами text: {}, categories: {}, paid: {}," +
                        " rangeStart: {}, rangeEnd: {}, onlyAvailable: {}, sort: {}, from: {}, size: {}'",
                "GET", "/events", text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        LocalDateTime start = (rangeStart == null) ? null : LocalDateTime.parse(rangeStart, FORMATTER);
        LocalDateTime end = (rangeEnd == null) ? null : LocalDateTime.parse(rangeEnd, FORMATTER);
        List<EventShortDto> eventShortDtoList = eventService.getAllEventsPublic(text, categories, paid, start,
                end, onlyAvailable, sort, from, size, request);
        log.info("Получен ответ / эндпоинт: '{} {}' с телом '{}", "GET", "/events", eventShortDtoList);
        return eventShortDtoList;
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEvent(@PathVariable Long eventId, HttpServletRequest request) {
        log.info("Пришел запрос / эндпоинт: '{} {}'",
                "GET", "/events/" + eventId);
        EventFullDto eventFullDto = eventService.getEventPublic(eventId, request);
        log.info("Пришел ответ / эндпоинт: '{} {} с телом {}'",
                "GET", "/events/" + eventId, eventFullDto);
        return eventFullDto;
    }
}
