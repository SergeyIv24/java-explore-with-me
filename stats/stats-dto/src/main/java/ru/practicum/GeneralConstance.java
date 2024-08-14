package ru.practicum;

import java.time.format.DateTimeFormatter;

public class GeneralConstance {
    public static final String DATA_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATA_PATTERN);
}
