package de.scriptsdk.core.model.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Crome696
 * @version 1.0
 */
public final class BaseJsonMapper extends ObjectMapper {

    public BaseJsonMapper() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDateTime.class,
                new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME));
        this.registerModule(javaTimeModule);
        this.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }
}
