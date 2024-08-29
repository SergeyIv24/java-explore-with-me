package ru.practicum.compilations;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.compilations.dto.CompilationRequest;
import ru.practicum.compilations.dto.CompilationResponse;
import ru.practicum.compilations.dto.CompilationUpdate;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.events.dto.EventRespShort;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CompilationMapper {

    public static Compilation mapToCompilation(CompilationRequest compilationRequest) {
        return new Compilation(compilationRequest.getId(),
                compilationRequest.getTitle(), compilationRequest.getPinned());
    }

    public static Compilation mapToCompilation(CompilationUpdate compilationUpdate) {
        return new Compilation(compilationUpdate.getId(),
                compilationUpdate.getTitle(), compilationUpdate.getPinned());
    }

    public static CompilationResponse mapToCompilationResponse(Compilation compilation, List<EventRespShort> events) {
        return new CompilationResponse(compilation.getId(),
                compilation.getTitle(), compilation.getPinned(), events);
    }

    public static Compilation updateCompilation(Compilation updatingCompilation, Compilation newCompilation) {
        if (newCompilation.getTitle() != null) {
            updatingCompilation.setTitle(newCompilation.getTitle());
        }
        if (newCompilation.getPinned() != null) {
            updatingCompilation.setPinned(newCompilation.getPinned());
        }
        return updatingCompilation;
    }
}
