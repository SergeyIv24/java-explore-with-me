package ru.practicum.users;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.model.User;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto addUser(UserDto userDto) {
        return UserMapper.mapToUserDto(userRepository.save(UserMapper.mapToUser(userDto)));
    }

    @Override
    public Collection<UserDto> getUsers(Collection<Integer> ids, int from, int size) {
        return null;
    }

    @Override
    public void deleteUserById(long userId) {
        isUserExisted(userId);
        userRepository.deleteById(userId);
    }

    private void isUserExisted(long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            log.warn("Attempt to delete unknown user");
            throw new NotFoundException("Unknown user");
        }
    }
}
