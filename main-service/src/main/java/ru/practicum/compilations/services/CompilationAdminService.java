package ru.practicum.compilations.services;

import ru.practicum.compilations.dto.CompilationRequest;
import ru.practicum.compilations.dto.CompilationResponse;
import ru.practicum.compilations.dto.CompilationUpdate;


public interface CompilationAdminService {

    CompilationResponse addCompilation(CompilationRequest compilationRequest);

    CompilationResponse updateCompilation(int id, CompilationUpdate compilationUpdate);

    void deleteCompilation(int id);


}
