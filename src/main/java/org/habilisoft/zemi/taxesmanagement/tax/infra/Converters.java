package org.habilisoft.zemi.taxesmanagement.tax.infra;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.habilisoft.zemi.taxesmanagement.tax.domain.TaxId;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;


interface Converters {
    @JsonComponent
    class TaxIdJsonSerializer extends JsonSerializer<TaxId> {
        @Override
        public void serialize(TaxId taxId, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeNumber(taxId.value());
        }
    }

    @JsonComponent
    class TaxIdJsonDeserializer extends JsonDeserializer<TaxId> {
        @Override
        public TaxId deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            return TaxId.of(jsonParser.getLongValue());
        }
    }
}
