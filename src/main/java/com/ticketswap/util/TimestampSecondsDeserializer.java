package com.ticketswap.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.sql.Timestamp;

public class TimestampSecondsDeserializer extends JsonDeserializer<Timestamp> {
    @Override
    public Timestamp deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        long seconds = parser.getLongValue();
        return new Timestamp(seconds * 1000);
    }
}
