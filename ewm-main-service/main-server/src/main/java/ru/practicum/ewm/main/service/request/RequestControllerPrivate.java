package ru.practicum.ewm.main.service.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.service.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.main.service.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.main.service.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.main.service.request.service.RequestService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}")
@RequiredArgsConstructor
@Validated
@Slf4j
public class RequestControllerPrivate {

    private final RequestService requestService;

    @PostMapping("/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createRequest(@PathVariable Long userId,
                                                 @RequestParam Long eventId) {
        log.info("Пришел запрос / эндпоинт: '{} {} с параметром eventId: {}'",
                "POST", "/users/" + userId + "/requests", eventId);
        ParticipationRequestDto participationRequestDto = requestService.create(userId, eventId);
        log.info("Пришел ответ / эндпоинт: '{} {} с телом {}'",
                "POST", "/users/" + userId + "/requests", participationRequestDto);
        return participationRequestDto;
    }

    @GetMapping("/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getRequestsByUser(@PathVariable Long userId) {
        log.info("Пришел запрос / эндпоинт: '{} {}'",
                "GET", "/users/" + userId + "/requests");
        List<ParticipationRequestDto> participationRequestDtoList = requestService.getRequestsByRequester(userId);
        log.info("Пришел ответ / эндпоинт: '{} {} с телом {}'",
                "GET", "/users/" + userId + "/requests", participationRequestDtoList);
        return participationRequestDtoList;
    }

    @PatchMapping("/requests/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public ParticipationRequestDto cancelRequest(@PathVariable Long userId,
                                                 @PathVariable Long requestId) {
        log.info("Пришел запрос / эндпоинт: '{} {}'",
                "PATCH", "/users/" + userId + "/requests/" + requestId + "/cancel");
        ParticipationRequestDto participationRequestDto = requestService.cancelRequest(userId, requestId);
        log.info("Пришел ответ / эндпоинт: '{} {} с телом {}'",
                "PATCH", "/users/" + userId + "/requests/" + requestId + "/cancel", participationRequestDto);
        return participationRequestDto;
    }

    @GetMapping("/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getRequestsForEvent(@PathVariable Long userId,
                                                             @PathVariable Long eventId) {
        log.info("Пришел запрос / эндпоинт: '{} {}'",
                "GET", "/users/" + userId + "/events/" + eventId + "/requests");
        List<ParticipationRequestDto> participationRequestDtoList = requestService.getRequestsForEvent(userId, eventId);
        log.info("Пришел запрос / эндпоинт: '{} {} с телом {}'",
                "GET", "/users/" + userId + "/events/" + eventId + "/requests", participationRequestDtoList);
        return participationRequestDtoList;
    }

    @PatchMapping("/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult updateRequestStatus(@PathVariable Long userId,
                                                              @PathVariable Long eventId,
                                                              @RequestBody @Valid EventRequestStatusUpdateRequest requests) {
        log.info("Пришел запрос / эндпоинт: '{} {} и с телом {}'",
                "PATCH", "/users/" + userId + "/events/" + eventId + "/requests", requests);
        EventRequestStatusUpdateResult eventRequestStatusUpdateResult = requestService.updateRequestStatus(userId, eventId, requests);
        log.info("Пришел ответ / эндпоинт: '{} {} и с телом {}'",
                "PATCH", "/users/" + userId + "/events/" + eventId + "/requests", eventRequestStatusUpdateResult);
        return eventRequestStatusUpdateResult;
    }
}
