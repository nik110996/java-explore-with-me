package ru.practicum.ewm.stats.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.stats.dto.HitDtoRequest;
import ru.practicum.ewm.stats.dto.ViewStatsResponseDto;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.stats.dto.util.Constants.FORMAT_OF_DATES;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Validated
@Slf4j
public class StatsClientController {
    @Autowired
    private StatsClient statsClient;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveStat(@RequestBody @Valid HitDtoRequest hitRequestDto) {
        log.info("Пришел запрос / эндпоинт: '{} {} с телом {}'",
                "POST", "/hit", hitRequestDto);
        statsClient.createStat(hitRequestDto);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<ViewStatsResponseDto> getStats(
            @RequestParam(name = "start", required = true)
            @DateTimeFormat(pattern = FORMAT_OF_DATES) LocalDateTime start,
            @RequestParam(name = "end", required = true)
            @DateTimeFormat(pattern = FORMAT_OF_DATES) LocalDateTime end,
            @RequestParam(name = "uris", required = false) List<String> uris,
            @RequestParam(name = "unique", defaultValue = "false") boolean unique) {
        log.info("Пришел запрос / эндпоинт: '{} {}'",
                "GET", "/stats");
        return statsClient.getStatistics(start, end, uris, unique);
    }

}
