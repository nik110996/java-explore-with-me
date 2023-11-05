package ru.practicum.ewm.stats.service.mapper;

import org.mapstruct.Mapper;
import ru.practicum.ewm.stats.dto.HitDtoRequest;
import ru.practicum.ewm.stats.service.model.Hit;

@Mapper(componentModel = "spring")
public interface HitMapper {
    Hit toHitFromRequestDto(HitDtoRequest hitRequestDto);

    HitDtoRequest toHitDtoRequestFromHit(Hit hit);
}
