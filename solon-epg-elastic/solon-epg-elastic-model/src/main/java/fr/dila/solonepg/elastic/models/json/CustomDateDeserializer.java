package fr.dila.solonepg.elastic.models.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import java.io.IOException;
import java.util.Date;

/**
 * La base EPG contient des dates > 9999 qui sont problématiques à la désérialisation
 */
public class CustomDateDeserializer extends JsonDeserializer<Date> {
    @SuppressWarnings("unchecked")
    private JsonDeserializer<Date> deserializer = (JsonDeserializer<Date>) DateDeserializers.find(
        Date.class,
        Date.class.getName()
    );

    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        try {
            return (Date) deserializer.deserialize(p, ctxt);
        } catch (InvalidFormatException e) {
            return null;
        }
    }
}
