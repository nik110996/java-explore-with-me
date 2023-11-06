package ru.practicum.ewm.main.service.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.main.service.event.model.Event;
import ru.practicum.ewm.main.service.event.model.State;
import ru.practicum.ewm.main.service.event.service.EventService;
import ru.practicum.ewm.main.service.exception.ConflictException;
import ru.practicum.ewm.main.service.exception.EntityNotFoundException;
import ru.practicum.ewm.main.service.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.main.service.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.main.service.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.main.service.request.mapper.ParticipationRequestMapper;
import ru.practicum.ewm.main.service.request.model.ParticipationRequest;
import ru.practicum.ewm.main.service.request.model.RequestStatus;
import ru.practicum.ewm.main.service.request.repository.RequestRepository;
import ru.practicum.ewm.main.service.user.model.User;
import ru.practicum.ewm.main.service.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.ewm.main.service.request.model.RequestStatus.*;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final UserService userService;
    private final EventService eventService;
    private final RequestRepository requestRepository;
    private final ParticipationRequestMapper requestMapper;

    @Override
    @Transactional
    public ParticipationRequestDto create(Long userId, Long eventId) {
        Optional<ParticipationRequest> requestOptional = requestRepository.findByRequesterIdAndEventId(userId, eventId);

        if (requestOptional.isPresent()) {
            throw new ConflictException("Запрос с такими User и Event уже существует.");
        }

        User user = userService.getOrThrow(userId);
        Event event = eventService.getOrThrow(eventId);
        if (event.getInitiator().equals(user) || !event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Integrity constraint violation");
        }

        if (event.getConfirmedRequests() >= event.getParticipantLimit() && event.getParticipantLimit() > 0) {
            throw new ConflictException("Integrity constraint violation");
        }

        ParticipationRequest request = ParticipationRequest.builder()
                .created(LocalDateTime.now())
                .requester(user)
                .event(event).build();
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(CONFIRMED);
            eventService.increaseConfirmedRequests(event);
        } else {
            request.setStatus(PENDING);
        }
        ParticipationRequestDto requestDto = requestMapper.requestToDto(requestRepository.saveAndFlush(request));
        return requestDto;
    }

    @Override
    public List<ParticipationRequestDto> getRequestsByRequester(Long userId) {
        userService.getOrThrow(userId);
        List<ParticipationRequest> requests = requestRepository.findAllByRequesterId(userId);
        return requests.stream()
                .map(requestMapper::requestToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        userService.getOrThrow(userId);
        ParticipationRequest request = getOrThrow(requestId);

        if (request.getStatus().equals(CANCELED)) {
            return requestMapper.requestToDto(request);
        }

        request.setStatus(CANCELED);
        eventService.decreaseConfirmedRequests(request.getEvent());
        return requestMapper.requestToDto(request);
    }

    @Override
    public List<ParticipationRequestDto> getRequestsForEvent(Long userId, Long eventId) {
        User user = userService.getOrThrow(userId);
        Event event = eventService.getOrThrow(eventId);
        if (!event.getInitiator().equals(user)) {
            throw new ConflictException("Integrity constraint has been violated");
        }

        List<ParticipationRequest> requests = requestRepository.findAllByEventId(eventId);

        return requests.stream()
                .map(requestMapper::requestToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest requestsUpdating) {
        User user = userService.getOrThrow(userId);
        Event event = eventService.getOrThrow(eventId);

        EventRequestStatusUpdateResult result = EventRequestStatusUpdateResult.builder()
                .confirmedRequests(Collections.emptyList())
                .rejectedRequests(Collections.emptyList()).build();

        validateEventForUpdateRequestStatus(event, user);

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            return result;
        }

        List<ParticipationRequest> requests = requestRepository.findAllByIdIn(requestsUpdating.getRequestIds());
        result = updateStatus(requests, requestsUpdating.getStatus(), event);
        return result;
    }

    private ParticipationRequest getOrThrow(Long requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException(ParticipationRequest.class, String.format("ID: %s", requestId)));
    }

    private void validateEventForUpdateRequestStatus(Event event, User user) {
        if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new ConflictException("Participation limit exceeded");
        }

        if (!event.getInitiator().equals(user)) {
            throw new ConflictException(String.format("User %s is not the initiator of the event %s.", user.getId(), event.getId()));
        }

    }

    private EventRequestStatusUpdateResult updateStatus(List<ParticipationRequest> requests, RequestStatus status, Event event) {
        EventRequestStatusUpdateResult result = EventRequestStatusUpdateResult.builder()
                .confirmedRequests(Collections.emptyList())
                .rejectedRequests(Collections.emptyList()).build();

        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();

        Long vacantPlace = event.getParticipantLimit() - event.getConfirmedRequests();

        for (ParticipationRequest request : requests) {
            if (!request.getStatus().equals(PENDING)) {
                throw new ConflictException("У запроса должен быть статус PENDING");
            }

            if (status.equals(CONFIRMED) && vacantPlace > 0) {
                request.setStatus(CONFIRMED);
                confirmedRequests.add(requestMapper.requestToDto(request));
                requestRepository.saveAndFlush(request);
                vacantPlace--;
            } else {
                request.setStatus(REJECTED);
                rejectedRequests.add(requestMapper.requestToDto(request));
                requestRepository.saveAndFlush(request);
            }
        }
        result.setConfirmedRequests(confirmedRequests);
        result.setRejectedRequests(rejectedRequests);
        event.setConfirmedRequests(event.getParticipantLimit() - vacantPlace);
        eventService.updateEvent(event);
        return result;
    }
}
