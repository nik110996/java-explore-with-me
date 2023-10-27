package ru.practicum.ewm.stats.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

import static ru.practicum.ewm.stats.dto.util.Constants.FORMAT_OF_DATES;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HitDtoRequest {
    @NotBlank(message = "Поле app должно быть заполнено")
    private String app;
    @NotBlank(message = "Поле uri должно быть заполнено")
    private String uri;
    @NotBlank(message = "Поле ip должно быть заполнено.")
    private String ip;
    @JsonFormat(pattern = FORMAT_OF_DATES)
    private LocalDateTime timestamp;
}
