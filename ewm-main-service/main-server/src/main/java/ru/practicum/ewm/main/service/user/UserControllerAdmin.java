package ru.practicum.ewm.main.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.service.user.dto.UserDto;
import ru.practicum.ewm.main.service.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserControllerAdmin {
    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getAll(@RequestParam(required = false) Long[] ids,
                                @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                @Positive @RequestParam(defaultValue = "10") Integer size,
                                HttpServletRequest request) {
        log.info("Пришел запрос / эндпоинт: '{} {} с параметрами ids: {}, from: {}, size: {}'",
                "GET", "/admin/users", ids, from, size);
        List<UserDto> userDtoList = userService.getAllUsers(ids, from, size);
        log.info("Пришел ответ / эндпоинт: '{} {} с телом {}'",
                "GET", "/admin/users", userDtoList);
        return userDtoList;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody @Valid UserDto userDto) {

        log.info("Пришел запрос / эндпоинт: '{} {} с телом {}'",
                "POST", "/admin/users", userDto);
        UserDto createdUser = userService.create(userDto);
        log.info("Пришел ответ / эндпоинт: '{} {} с телом {}'",
                "POST", "/admin/users", createdUser);
        return createdUser;
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long userId) {
        log.info("Пришел запрос / эндпоинт: '{} {}'",
                "DELETE", "/admin/users/" + userId);
        userService.delete(userId);
        log.info("Пришел ответ / эндпоинт: '{} {}'",
                "DELETE", "/admin/users/" + userId);
    }
}