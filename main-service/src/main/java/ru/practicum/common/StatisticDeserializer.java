package ru.practicum.common;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.practicum.dto.StatisticResponse;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StatisticDeserializer extends
        JsonDeserializer<List<StatisticResponse>> {

    @Override
    public List<StatisticResponse> deserialize(JsonParser parser,
                                               DeserializationContext deserializationContext) throws IOException {

        List<StatisticResponse> statisticsList = new ArrayList<>();







        return null;
    }
}
