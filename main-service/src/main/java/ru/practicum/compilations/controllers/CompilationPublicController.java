package ru.practicum.compilations.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilations.dto.CompilationResponse;
import ru.practicum.compilations.services.CompilationPublicService;

import java.util.List;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
@Slf4j
public class CompilationPublicController {

    private final CompilationPublicService compilationService;

    @GetMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationResponse getCompilationById(@PathVariable("compId") int compId) {
        log.info("CompilationPublicController, getCompilationById, compId: {}", compId);
        return compilationService.getCompilationById(compId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CompilationResponse> getCompilations(@RequestParam(value = "pinned", required = false) boolean pinned,
                                                     @RequestParam(value = "from", required = false, defaultValue = "0")
                                                     int from,
                                                     @RequestParam(value = "size", required = false, defaultValue = "10")
                                                     int size) {
        log.info("CompilationPublicController, getCompilations, pinned: {}, from: {}, size: {}", pinned, from, size);
        return compilationService.getCompilations(pinned, from, size);
    }
}
