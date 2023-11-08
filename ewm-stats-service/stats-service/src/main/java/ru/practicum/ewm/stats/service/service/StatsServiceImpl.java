package ru.practicum.ewm.stats.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.stats.dto.HitDtoRequest;
import ru.practicum.ewm.stats.dto.ViewStatsResponseDto;
import ru.practicum.ewm.stats.service.mapper.HitMapper;
import ru.practicum.ewm.stats.service.mapper.ViewStatsMapper;
import ru.practicum.ewm.stats.service.model.Hit;
import ru.practicum.ewm.stats.service.model.ViewStats;
import ru.practicum.ewm.stats.service.repository.StatsRepository;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;
    private final HitMapper hitMapper;
    private final ViewStatsMapper viewMapper;

    @Override
    @Transactional
    public HitDtoRequest createStat(HitDtoRequest hitRequestDto) {
        Hit hit = hitMapper.toHitFromRequestDto(hitRequestDto);
        return hitMapper.toHitDtoRequestFromHit(statsRepository.save(hit));
    }

    @Override
    public List<ViewStatsResponseDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {

        if (start != null && start.isAfter(end)) {
            throw new ValidationException("Время начала не может быть позже конечного времени.");
        }
        List<ViewStats> result;
        if (uris != null) {
            result = (unique) ? statsRepository.getUrisWithUniqueIP(start, end, uris) :
                    statsRepository.getUrisStats(start, end, uris);
        } else {
            result = (unique) ? statsRepository.getAllUrisWithUniqueIP(start, end) :
                    statsRepository.getAllUrisStats(start, end);
        }
        return result.stream()
                .map(viewMapper::toDtoFromView)
                .collect(Collectors.toList());
    }
}
