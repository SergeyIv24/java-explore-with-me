package ru.practicum.compilations.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.Errors.NotFoundException;
import ru.practicum.compilations.CompilationMapper;
import ru.practicum.compilations.dto.CompilationResponse;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.compilations.model.EventsByCompilation;
import ru.practicum.compilations.repository.CompilationRepository;
import ru.practicum.compilations.repository.EventByCompilationRepository;
import ru.practicum.events.EventMapper;
import ru.practicum.events.EventRepository;
import ru.practicum.events.dto.EventRespShort;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationPublicServiceImp implements CompilationPublicService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final EventByCompilationRepository eventByCompilationRepository;

    @Override
    public CompilationResponse getCompilationById(int compId) {
        Compilation compilation = validateAndCompilation(compId);

        List<Long> eventIds = eventByCompilationRepository.findByCompilationId(compId);

        List<EventRespShort> events = eventRepository.findByIdIn(eventIds)
                .stream()
                .map(EventMapper::mapToEventRespShort)
                .toList();
        return CompilationMapper.mapToCompilationResponse(compilation, events);
    }

    @Override
    public List<CompilationResponse> getCompilations(boolean pinned, int from, int size) {
        int startPage = from > 0 ? (from / size) : 0;
        Pageable pageable = PageRequest.of(startPage, size);

        //Find all compilations
        Map<Integer, Compilation> compilationMap = compilationRepository.findAll(pageable)
                .stream()
                .collect(Collectors.toMap(Compilation::getId, Function.identity()));

        //Find all event ids for compilations
        List<EventsByCompilation> eventsByCompilation = eventByCompilationRepository
                .findByCompilationIdIn(compilationMap.keySet());

        //Every compilation id and event ids list
        Map<Integer, List<Long>> compByEventsIdsList = new HashMap<>();

        for (EventsByCompilation item : eventsByCompilation) {
            if (!compByEventsIdsList.containsKey(item.getCompositeKey().getCompilationId())) {
                List<Long> eventsId = new ArrayList<>();
                eventsId.add(item.getCompositeKey().getEventId());
                compByEventsIdsList.put(item.getCompositeKey().getCompilationId(), eventsId);
            }
            compByEventsIdsList.get(item.getCompositeKey().getCompilationId()).add(item.getCompositeKey().getEventId());
        }

        //All events id needs to get all events objects from database
        List<Long> allEventsIds = new ArrayList<>();

        for (List<Long> events : compByEventsIdsList.values()) {
            allEventsIds.addAll(events);
        }

        Map<Long, EventRespShort> events = eventRepository.findByIdIn(allEventsIds)
                .stream()
                .map(EventMapper::mapToEventRespShort)
                .collect(Collectors.toMap(EventRespShort::getId, Function.identity()));

        //Map compId and list of events object
        Map<Integer, List<EventRespShort>> compByEventsObj = new HashMap<>();

        //n^2 ((
        for (Integer compId : compByEventsIdsList.keySet()) {
            List<Long> eventsIds = compByEventsIdsList.get(compId);
            List<EventRespShort> eventsObj = new ArrayList<>();

            for (Long eventsId: eventsIds) {
                eventsObj.add(events.get(eventsId));
            }
            compByEventsObj.put(compId, eventsObj);
        }

        return compilationMap
                .values()
                .stream()
                .map((compilation) -> CompilationMapper
                        .mapToCompilationResponse(compilation, compByEventsObj.get(compilation.getId())))
                .collect(Collectors.toList());
    }

    private Compilation validateAndCompilation(int compId) {
        Optional<Compilation> compilation = compilationRepository.findById(compId);

        if (compilation.isEmpty()) {
            log.warn("Compilation with id {} was not found", compId);
            throw new NotFoundException("Compilation with id = " + compId + " was not found");
        }
        return compilation.get();
    }
}
