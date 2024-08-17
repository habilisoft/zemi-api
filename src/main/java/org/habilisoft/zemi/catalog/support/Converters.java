package org.habilisoft.zemi.catalog.support;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.habilisoft.zemi.catalog.CategoryId;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;


interface Converters {
    @Converter(autoApply = true)
    class CategoryIdConverter implements
            AttributeConverter<CategoryId, Long> {
        @Override
        public Long convertToDatabaseColumn(CategoryId attribute) {
            return attribute.value();
        }

        @Override
        public CategoryId convertToEntityAttribute(Long dbData) {
            return CategoryId.of(dbData);
        }
    }
    @JsonComponent
    class CategoryIdJsonSerializer extends JsonSerializer<CategoryId> {
        @Override
        public void serialize(CategoryId categoryId, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeNumber(categoryId.value());
        }
    }
    @JsonComponent
    class CategoryIdJsonDeserializer extends JsonDeserializer<CategoryId> {
        @Override
        public CategoryId deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            return CategoryId.of(jsonParser.getLongValue());
        }
    }
}
