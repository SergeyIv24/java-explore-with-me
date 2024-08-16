package ru.practicum.users;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.Errors.NotFoundException;
import ru.practicum.Errors.ValidationException;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        validateFromAndSize(from, size);
        Sort sortDyId = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(from, size, sortDyId);

        if (ids == null) {
            return getUsersWithoutIds(pageable);
        }
        return getUsersWithIds(ids, pageable);
    }

    @Override
    public void deleteUserById(long userId) {
        isUserExisted(userId);
        userRepository.deleteById(userId);
    }

    private Collection<UserDto> getUsersWithoutIds(Pageable pageable) {
        return userRepository.findAll(pageable)
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    private Collection<UserDto> getUsersWithIds(Collection<Integer> ids, Pageable pageable) {
        return userRepository.findByIdIn(List.copyOf(ids), pageable)
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    private void validateFromAndSize(int from, int size) {
        if (from < 0 || size < 0) {
            log.warn("Incorrect from or size. From: {}, size: {}", from, size);
            throw new ValidationException("Bad from or size");
        }
    }


    private void isUserExisted(long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            log.warn("Attempt to delete unknown user");
            throw new NotFoundException("User with id = " + userId + " was not found");
        }
    }
}
