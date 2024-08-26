package ru.practicum.compilations.services;

import ru.practicum.compilations.dto.CompilationRequest;
import ru.practicum.compilations.dto.CompilationResponse;


public interface CompilationAdminService {

    CompilationResponse addCompilation(CompilationRequest compilationRequest);


    void deleteCompilation(int id);


}