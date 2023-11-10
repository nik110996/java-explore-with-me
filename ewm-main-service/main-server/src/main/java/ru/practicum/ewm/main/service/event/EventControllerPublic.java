package ru.practicum.ewm.main.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.service.event.dto.EventFullDto;
import ru.practicum.ewm.main.service.event.dto.EventShortDto;
import ru.practicum.ewm.main.service.event.model.RateEventResponse;
import ru.practicum.ewm.main.service.event.model.RateInitiatorResponse;
import ru.practicum.ewm.main.service.event.service.EventService;
import ru.practicum.ewm.main.service.util.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

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
                                            @RequestParam(required = false) @DateTimeFormat(pattern = Constants.DATE_FORMAT) LocalDateTime rangeStart,
                                            @RequestParam(required = false) @DateTimeFormat(pattern = Constants.DATE_FORMAT) LocalDateTime rangeEnd,
                                            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                            @RequestParam(required = false) String sort,
                                            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                            @Positive @RequestParam(defaultValue = "10") Integer size,
                                            HttpServletRequest request) {
        log.info("Пришел запрос / эндпоинт: '{} {} с параметрами text: {}, categories: {}, paid: {}," +
                        " rangeStart: {}, rangeEnd: {}, onlyAvailable: {}, sort: {}, from: {}, size: {}'",
                "GET", "/events", text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);

        List<EventShortDto> eventShortDtoList = eventService.getAllEventsPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request);
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

    @GetMapping("/ratings")
    @ResponseStatus(HttpStatus.OK)
    public List<RateEventResponse> getEventRatings(@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                   @Positive @RequestParam(defaultValue = "10") Integer size) {

        log.info("Пришел запрос / эндпоинт: '{} {} с параметрами from: {} и size: {}'",
                "GET", "/events/ratings", from, size);
        List<RateEventResponse> rateEventResponseList = eventService.getEventsRatings(from, size);
        log.info("Пришел ответ / эндпоинт: '{} {} с телом {}'",
                "GET", "/events/ratings", rateEventResponseList);
        return rateEventResponseList;
    }

    @GetMapping("/users/ratings")
    @ResponseStatus(HttpStatus.OK)
    public List<RateInitiatorResponse> getUsersRatings(@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                       @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Пришел запрос / эндпоинт: '{} {} с параметрами from: {} и size: {}'",
                "GET", "/events/users/ratings", from, size);
        List<RateInitiatorResponse> rateInitiatorResponseList = eventService.getUsersRatings(from, size);
        log.info("Пришел ответ / эндпоинт: '{} {} с телом {}'",
                "GET", "/events/users/ratings", rateInitiatorResponseList);
        return rateInitiatorResponseList;
    }
}
