package io.github.floriansw.ondus.api.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateDeserializer extends JsonDeserializer<LocalDateTime> {
    private static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd H:m:s");
    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public LocalDateTime deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String node = jp.readValueAs(String.class);
        if (node.contains(" ")) {
            return LocalDateTime.parse(node, dateTimeFormat);
        }
        return LocalDate.parse(node, dateFormat).atTime(0, 0);
    }
}
