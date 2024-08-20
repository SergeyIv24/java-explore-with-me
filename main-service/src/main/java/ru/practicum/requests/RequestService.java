package ru.practicum.requests;

import ru.practicum.requests.dto.RequestDto;

import java.util.Collection;

public interface RequestService {

    RequestDto addRequest(long eventId, long userId);

    RequestDto cancelRequest(long requestId, long userId);

    Collection<RequestDto> getMyRequests(long userId);

}
