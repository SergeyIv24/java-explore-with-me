package ru.practicum.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.requests.dto.RequestDto;

import java.util.Collection;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
public class RequestsController {

    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public RequestDto addRequest(@RequestParam("eventId") Long eventId,
                                 @PathVariable("userId") long userId) {
        log.info("RequestsController, addRequest. EventId: {}", eventId);
        return requestService.addRequest(eventId, userId);
    }

    @PatchMapping("/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public RequestDto cancelRequest(@PathVariable("requestId") long requestId,
                                    @PathVariable("userId") long userId) {
        log.info("RequestsController, cancelRequest, RequestId: {}, userId: {}", requestId, userId);
        return requestService.cancelRequest(requestId, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<RequestDto> getMyRequests(@PathVariable("userId") long userId) {
        log.info("RequestsController, getMyRequests. UserId: {}", userId);
        return requestService.getMyRequests(userId);
    }
}
