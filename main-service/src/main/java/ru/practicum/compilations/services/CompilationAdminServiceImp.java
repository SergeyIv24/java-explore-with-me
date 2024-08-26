package ru.practicum.compilations.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.Errors.NotFoundException;
import ru.practicum.compilations.CompilationMapper;
import ru.practicum.compilations.dto.CompilationRequest;
import ru.practicum.compilations.dto.CompilationResponse;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.compilations.model.CompositeKeyForEventByComp;
import ru.practicum.compilations.model.EventsByCompilation;
import ru.practicum.compilations.repository.CompilationRepository;
import ru.practicum.compilations.repository.EventByCompilationRepository;
import ru.practicum.events.EventMapper;
import ru.practicum.events.EventRepository;
import ru.practicum.events.dto.EventRespShort;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationAdminServiceImp implements CompilationAdminService {

    private final CompilationRepository compilationRepository;
    private final EventByCompilationRepository eventByCompilationRepository;
    private final EventRepository eventRepository;

    //Data stores normalized in two tables. 1 - compilation (id, title, pinned),
    // 2 - events_by_compilations (compilation_id, event_id)
    @Override
    public CompilationResponse addCompilation(CompilationRequest compilationRequest) {
        if (compilationRequest.getPinned() == null) {
            compilationRequest.setPinned(false);
        }

        //Save to compilation table
        Compilation savedCompilation = compilationRepository
                .save(CompilationMapper.mapToCompilation(compilationRequest));

        int compilationId = savedCompilation.getId(); //returned compilation_id

        //Prepare List<EventsByCompilation> to add in events_by_compilations
        List<EventsByCompilation> eventsByCompilations = compilationRequest.getEvents()
                .stream()
                .map((id) -> EventsByCompilation
                        .builder()
                        .compositeKey(CompositeKeyForEventByComp
                                .builder()
                                .compilationId(compilationId)
                                .eventId(id)
                                .build())
                        .build())
                .toList();

        //Save in events_by_compilations
        eventByCompilationRepository.saveAll(eventsByCompilations);

        List<EventRespShort> events = eventRepository.findByIdIn(compilationRequest.getEvents())
                .stream()
                .map(EventMapper::mapToEventRespShort)
                .toList();
        return CompilationMapper.mapToCompilationResponse(savedCompilation, events);
    }

    @Override
    public void deleteCompilation(int id) {
        validateCompilation(id);
        compilationRepository.deleteById(id);
    }

    private void validateCompilation(int id) {

        if (!compilationRepository.existsById(id)) {
            log.warn("Compilation with: {} was not found", id);
            throw new NotFoundException("Compilation with = " + id + " was not found");
        }
    }


}
