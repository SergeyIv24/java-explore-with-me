package ru.practicum.users;

import ru.practicum.users.dto.UserDto;

import java.util.Collection;

public interface UserService {

    UserDto addUser(UserDto userDto);

    Collection<UserDto> getUsers(Collection<Integer> ids, int from, int size);

    void deleteUserById(long userId);

}
