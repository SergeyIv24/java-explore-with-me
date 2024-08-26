package ru.practicum.compilations;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.compilations.dto.CompilationRequest;
import ru.practicum.compilations.dto.CompilationResponse;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.events.dto.EventRespShort;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CompilationMapper {

    public static Compilation mapToCompilation(CompilationRequest compilationRequest) {
        return Compilation.builder()
                .id(compilationRequest.getId())
                .title(compilationRequest.getTitle())
                .pinned(compilationRequest.getPinned())
                .build();
    }

    public static CompilationResponse mapToCompilationResponse(Compilation compilation, List<EventRespShort> events) {
        return CompilationResponse
                .builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .events(events)
                .pinned(compilation.getPinned())
                .build();
    }


}
