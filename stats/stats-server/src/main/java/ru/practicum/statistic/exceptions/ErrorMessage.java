package ru.practicum.statistic.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ErrorMessage {
    private final String errorMessage;
    private final String exceptionMessage;
}
