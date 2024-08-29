package ru.practicum.users;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.users.dto.UserDto;


import java.util.Collection;
import java.util.Set;

@RestController
@RequestMapping("/admin/users")
@Validated
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@Valid @RequestBody UserDto userDto) {
        log.info("UserController, addUser, userName: {}, userEmail {}", userDto.getName(), userDto.getEmail());
        return userService.addUser(userDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<UserDto> getUsers(@RequestParam(value = "ids", required = false) Set<Integer> ids,
                                        @Min(0) @RequestParam(value = "from", defaultValue = "0") int from,
                                        @Min(0) @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("UserController, getUser, ids: {}, from: {}, size: {}", ids, from, size);
        return userService.getUsers(ids, from, size);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable("userId") long userId) {
        log.info("UserController, deleteUserById, userId: {}", userId);
        userService.deleteUserById(userId);
    }

}
