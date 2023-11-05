package ru.practicum.ewm.main.service.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;

    @NotBlank(message = "Поле не должно быть пустым.")
    @Size(min = 2,
            max = 250,
            message = "Поле name должно содержать не менее 2 и не более 250 символов.")
    private String name;

    @NotBlank(message = "Поле не должно быть пустым.")
    @Email(message = "Указан некорректный Email")
    @Size(min = 6,
            max = 254,
            message = "Поле email должно содержать не менее 2 и не более 254 символов.")
    private String email;
}
