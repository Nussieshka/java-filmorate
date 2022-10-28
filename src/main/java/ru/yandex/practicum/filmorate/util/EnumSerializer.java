package ru.yandex.practicum.filmorate.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class EnumSerializer<T extends Enum<?>> extends JsonSerializer<T> {
    public void serialize(T value, JsonGenerator generator, SerializerProvider provider) throws IOException {
        generator.writeStartObject();
        generator.writeFieldName("id");
        generator.writeNumber(value.ordinal() + 1);
        generator.writeFieldName("name");
        generator.writeString(value.toString());
        generator.writeEndObject();
    }
}
