package ru.practicum.ewm.main.service.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private Long id;

    @NotBlank(message = "Наименование не может быть пустым.")
    @Size(min = 1,
            max = 50,
            message = "Поле name должно содержать не менее 1 и не более 50 символов.")
    private String name;
}
