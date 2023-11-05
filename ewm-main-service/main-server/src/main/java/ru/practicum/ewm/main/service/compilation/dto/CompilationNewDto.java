package ru.practicum.ewm.main.service.compilation.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Builder
public class CompilationNewDto {

    private Set<Long> events;
    private Boolean pinned;
    @NotBlank
    @Size(min = 1,
            max = 50,
            message = "Поле title должно содержать не менее 1 и не более 50 символов.")
    private String title;
}
