package org.fzillo.services.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.fzillo.services.db.TagDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class CustomLocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {

    private static final Logger log = LoggerFactory.getLogger(CustomLocalDateTimeDeserializer.class);

    public CustomLocalDateTimeDeserializer() {
        super(LocalDateTime.class);
    }

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String timestamp = jsonParser.getText().trim();
        try {
            long longValue = Long.valueOf(timestamp);
            return LocalDateTime.ofInstant(Instant.ofEpochSecond(longValue), ZoneId.of("UTC"));
        } catch (NumberFormatException e) {
            log.warn("Unable to deserialize timestamp: " + timestamp, e) ;
            return null;
        }
    }
}
