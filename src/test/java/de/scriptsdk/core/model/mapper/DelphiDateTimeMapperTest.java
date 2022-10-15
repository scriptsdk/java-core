package de.scriptsdk.core.model.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class DelphiDateTimeMapperTest {
    @Test
    void testConverter() {

        LocalDateTime now = LocalDateTime.now();

        DelphiDateTimeMapper stamp1 = new DelphiDateTimeMapper(now);

        Double value = stamp1.getValue();

        DelphiDateTimeMapper stamp2 = new DelphiDateTimeMapper(value);

        Assertions.assertEquals(stamp1.getDateTime(), stamp2.getDateTime());
        Assertions.assertEquals(stamp1.getValue(), stamp2.getValue());
    }
}