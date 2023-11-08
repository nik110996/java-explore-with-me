package ru.practicum.ewm.main.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.service.event.dto.EventFullDto;
import ru.practicum.ewm.main.service.event.dto.UpdateEventUserRequest;
import ru.practicum.ewm.main.service.event.model.State;
import ru.practicum.ewm.main.service.event.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.main.service.util.Constants.FORMATTER;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Validated
@Slf4j
public class EventControllerAdmin {

    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getAll(@RequestParam(required = false) List<Long> users,
                                     @RequestParam(required = false) List<State> states,
                                     @RequestParam(required = false) List<Long> categories,
                                     @RequestParam(required = false) String rangeStart,
                                     @RequestParam(required = false) String rangeEnd,
                                     @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                     @Positive @RequestParam(defaultValue = "10") Integer size) {
        LocalDateTime start = (rangeStart == null) ? null : LocalDateTime.parse(rangeStart, FORMATTER);
        LocalDateTime end = (rangeEnd == null) ? null : LocalDateTime.parse(rangeEnd, FORMATTER);
        log.info("Пришел запрос / эндпоинт: '{} {} с параметрами users: {}, states: {}, categories: {}," +
                        " rangeStart: {}, rangeEnd: {}, from: {}, size: {}'",
                "GET", "/admin/events", users, states, categories, rangeStart, rangeEnd, from, size);
        List<EventFullDto> eventFullDtoList = eventService.getAllEventsAdmin(users, states, categories, start,
                end, from, size);
        log.info("Получен ответ / эндпоинт: '{} {}' с телом '{}", "GET", "/admin/events", eventFullDtoList);
        return eventFullDtoList;
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto update(@PathVariable Long eventId,
                               @RequestBody @Valid UpdateEventUserRequest updateEventUserRequest) {
        log.info("Пришел запрос / эндпоинт: '{} {}'",
                "GET", "/admin/events/" + eventId);
        EventFullDto eventFullDto = eventService.updateEventAdmin(eventId, updateEventUserRequest);
        log.info("Получен ответ / эндпоинт: '{} {}' с телом '{}", "GET", "/admin/events/" + eventId, eventFullDto);
        return eventFullDto;
    }
}
