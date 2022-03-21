package mil.army.usace.hec.cumulus.client.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public final class CumulusObjectMapper {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
        .registerModule(new JavaTimeModule()
            .addDeserializer(ZonedDateTime.class, new CumulusDateTimeDeSerializer()))
        .configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true)
        .configure(JsonReadFeature.ALLOW_MISSING_VALUES.mappedFeature(), true);

    private CumulusObjectMapper() {
        throw new AssertionError("Utility class");
    }

    public static <T> T mapJsonToObject(String json, Class<T> classObject) throws IOException {
        return OBJECT_MAPPER.readValue(json, classObject);
    }

    public static <T> List<T> mapJsonToListOfObjects(String json, Class<T> classObject) throws IOException {
        return OBJECT_MAPPER.readValue(json, OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, classObject));
    }

    private static class CumulusDateTimeDeSerializer extends JsonDeserializer<ZonedDateTime> {

        private final DateTimeFormatter fmt = DateTimeFormatter.ISO_ZONED_DATE_TIME;

        @Override
        public ZonedDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return ZonedDateTime.parse(p.getText(), fmt);
        }
    }

}

