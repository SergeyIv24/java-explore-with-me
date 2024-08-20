/*
package ru.practicum.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.Errors.NotFoundException;
import ru.practicum.categories.model.Category;
import ru.practicum.events.model.Event;
import ru.practicum.users.model.User;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public class DataValidators {

    public static Event validateEvent(long eventId) {
        Optional<Event> event = eventRepository.findById(eventId);

        if (event.isEmpty()) {
            log.warn("Attempt to get unknown event");
            throw new NotFoundException("Event with id = " + eventId + "was not found");
        }
        return event.get();
    }

    private User validateUser(long userId) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            log.warn("Attempt to delete unknown user");
            throw new NotFoundException("User with id = " + userId + " was not found");
        }
        return user.get();
    }

    private Category validateCategory(int categoryId) {
        Optional<Category> category = categoriesRepository.findById(categoryId);

        if (category.isEmpty()) {
            log.warn("Attempt to delete unknown category with categoryId: {}", categoryId);
            throw new NotFoundException("Category with id = " + categoryId + " was not found");
        }
        return category.get();
    }


}
*/
