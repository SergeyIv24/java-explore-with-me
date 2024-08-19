package ru.practicum.events.model;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.boot.jackson.JsonComponent;
import ru.practicum.common.GeneralConstants;


import java.io.IOException;
import java.time.LocalDateTime;

@JsonComponent
public class CustomDataDeserializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser,
                                     DeserializationContext deserializationContext)
            throws IOException, JacksonException {
        return LocalDateTime.parse(jsonParser.getValueAsString(), GeneralConstants.DATE_FORMATTER);
    }
}
