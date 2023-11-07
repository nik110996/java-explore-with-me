package ru.practicum.ewm.main.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.service.event.dto.*;
import ru.practicum.ewm.main.service.event.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Validated
@Slf4j
public class EventControllerPrivate {
    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getAllEvents(@PathVariable Long userId,
                                            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Пришел запрос / эндпоинт: '{} {} с параметром from: {} и с параметром size: {}'",
                "GET", "/users/" + userId + "/events", from, size);
        List<EventShortDto> eventShortDtoList = eventService.getAll(userId, from, size);
        log.info("Получен ответ / эндпоинт: '{} {}' с телом '{}", "GET", "/users/" + userId + "/events", eventShortDtoList);
        return eventShortDtoList;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable Long userId,
                                    @RequestBody @Valid NewEventDto newEventDto) {
        log.info("Пришел запрос / эндпоинт: '{} {} с телом {}'",
                "POST", "/users/" + userId + "/events", newEventDto);
        EventFullDto eventFullDto = eventService.create(userId, newEventDto);
        log.info("Пришел ответ / эндпоинт: '{} {} с телом {}'",
                "POST", "/users/" + userId + "/events", eventFullDto);
        return eventFullDto;
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEvent(@PathVariable Long userId,
                                 @PathVariable Long eventId) {
        log.info("Пришел запрос / эндпоинт: '{} {}'",
                "GET", "/users/" + userId + "/events/" + eventId);
        EventFullDto eventFullDto = eventService.getEvent(userId, eventId);
        log.info("Пришел ответ / эндпоинт: '{} {} с телом {}'",
                "GET", "/users/" + userId + "/events/" + eventId, eventFullDto);
        return eventFullDto;
    }


    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEvent(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @RequestBody @Valid UpdateEventUserRequest updateEventUserRequest) {
        log.info("Пришел запрос / эндпоинт: '{} {} и с телом {}'",
                "PATCH", "/users/" + userId + "/events/" + eventId, updateEventUserRequest);
        EventFullDto eventFullDto = eventService.updateEvent(userId, eventId, updateEventUserRequest);
        log.info("Пришел ответ / эндпоинт: '{} {} и с телом {}'",
                "PATCH", "/users/" + userId + "/events/" + eventId, eventFullDto);
        return eventFullDto;
    }

    @PostMapping("/{eventId}/rate")
    @ResponseStatus(HttpStatus.CREATED)
    public RateEventDto addLike(@PathVariable Long userId,
                                @PathVariable Long eventId,
                                @RequestParam(required = true) Boolean isLike) {
        log.info("Пришел запрос / эндпоинт: '{} {} с параметром {}'",
                "POST", "/users/" + userId + "/events/" + eventId, isLike);
        RateEventDto rateEventDto = eventService.addLike(userId, eventId, isLike);
        log.info("Пришел ответ / эндпоинт: '{} {} с телом {}'",
                "POST", "/users/" + userId + "/events/" + eventId, rateEventDto);
        return rateEventDto;
    }

    @DeleteMapping("/{eventId}/rate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLike(@PathVariable Long userId,
                           @PathVariable Long eventId) {
        log.info("Пришел запрос / эндпоинт: '{} {}'",
                "DELETE", "/users/" + userId + "/events/" + eventId);
        eventService.deleteLike(userId, eventId);
    }
}
