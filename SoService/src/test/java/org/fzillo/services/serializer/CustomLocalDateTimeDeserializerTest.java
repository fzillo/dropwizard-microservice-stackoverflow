package org.fzillo.services.serializer;

import com.fasterxml.jackson.core.JsonParser;
import org.junit.Test;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CustomLocalDateTimeDeserializerTest {

    public static final String TIMESTAMP = "1581417884";
    public static final String TIMESTAMP_AS_STRING = "2020-02-11T10:44:44";
    public static final String DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

    @Test
    public void deserialize_returnCorrectLocalDateTime() throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIME_PATTERN)
                .withZone(ZoneId.of("UTC"));
        LocalDateTime expectedLocalDateTime = LocalDateTime.parse(TIMESTAMP_AS_STRING, formatter);

        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.getText()).thenReturn(TIMESTAMP);
        CustomLocalDateTimeDeserializer customLocalDateTimeDeserializer = new CustomLocalDateTimeDeserializer();
        LocalDateTime outputLocalDateTime = customLocalDateTimeDeserializer.deserialize(jsonParser,null);

        assertEquals(expectedLocalDateTime,outputLocalDateTime);
    }


    @Test
    public void deserialize_throwAndCatchNumberFormatException() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.getText()).thenReturn("");
        assertNull(new CustomLocalDateTimeDeserializer().deserialize(jsonParser,null));
    }
}