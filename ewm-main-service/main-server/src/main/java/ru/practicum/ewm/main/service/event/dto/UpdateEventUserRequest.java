package ru.practicum.ewm.main.service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import ru.practicum.ewm.main.service.event.model.StateAction;
import ru.practicum.ewm.main.service.location.dto.LocationDto;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.ewm.main.service.util.Constants.DATE_FORMAT;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventUserRequest {

    @Nullable
    @Size(min = 20,
            max = 2000,
            message = "Поле annotation должно содержать не менее 20 и не более 2000 символов.")
    private String annotation;

    private Long category;

    @Nullable
    @Size(min = 20,
            max = 7000,
            message = "Поле description должно содержать не менее 20 и не более 7000 символов.")
    private String description;

    @Nullable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    private LocalDateTime eventDate;

    private LocationDto location;

    private Boolean paid;

    private Long participantLimit;

    private Boolean requestModeration;

    private StateAction stateAction;

    @Nullable
    @Size(min = 3,
            max = 120,
            message = "Поле title должно содержать не менее 3 и не более 120 символов.")
    private String title;

}
