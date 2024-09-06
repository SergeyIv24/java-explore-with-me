package ru.practicum.compilations.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.errors.NotFoundException;
import ru.practicum.compilations.CompilationMapper;
import ru.practicum.compilations.dto.CompilationRequest;
import ru.practicum.compilations.dto.CompilationResponse;
import ru.practicum.compilations.dto.CompilationUpdate;
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

        if (compilationRequest.getEvents() == null) {
            return CompilationMapper.mapToCompilationResponse(savedCompilation, List.of());
        }
        return CompilationMapper
                .mapToCompilationResponse(savedCompilation, addEventByCompilations(compilationRequest, compilationId));
    }

    @Override
    public CompilationResponse updateCompilation(int id, CompilationUpdate compilationUpdate) {
        Compilation updatingCompilation = validateAndGetCompilation(id);

        Compilation updatedCompilation = compilationRepository
                .save(CompilationMapper
                        .updateCompilation(updatingCompilation, CompilationMapper.mapToCompilation(compilationUpdate)));

        if (compilationUpdate.getEvents() == null) {
            return CompilationMapper.mapToCompilationResponse(updatedCompilation, List.of());
        }

        deleteEventsByCompilations(id);

        return CompilationMapper
                .mapToCompilationResponse(updatedCompilation, addEventByCompilations(compilationUpdate, id));
    }

    @Override
    public void deleteCompilation(int id) {
        validateAndGetCompilation(id);
        compilationRepository.deleteById(id);
        deleteEventsByCompilations(id);
    }

    private <T extends CompilationUpdate> List<EventRespShort> addEventByCompilations(T compilation, int id) {

        List<EventsByCompilation> eventsByComp = compilation
                .getEvents()
                .stream()
                .map((EbCId) -> new EventsByCompilation(new CompositeKeyForEventByComp(id, EbCId)))
                .toList();

        eventByCompilationRepository.saveAll(eventsByComp);

        return eventRepository.findByIdIn(compilation.getEvents())
                .stream()
                .map(EventMapper::mapToEventRespShort)
                .toList();
    }

    private void deleteEventsByCompilations(int id) {
        if (!eventByCompilationRepository.findByCompilationId(id).isEmpty()) {
            eventByCompilationRepository.deleteByCompilationId(id);
        }
    }

    private Compilation validateAndGetCompilation(int id) {
        if (!compilationRepository.existsById(id)) {
            log.warn("Compilation with: {} was not found", id);
            throw new NotFoundException("Compilation with = " + id + " was not found");
        }
        return compilationRepository.findById(id).orElseThrow();
    }
}
