package org.habilisoft.zemi.sales.customer.infra;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.habilisoft.zemi.sales.customer.domain.CustomerId;
import org.habilisoft.zemi.sales.customer.domain.CustomerType;
import org.habilisoft.zemi.sales.customer.domain.EmailAddress;
import org.habilisoft.zemi.sales.customer.domain.PhoneNumber;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;


interface Converters {
    @Converter(autoApply = true)
    class CustomerIdConverter implements
            AttributeConverter<CustomerId, Long> {
        @Override
        public Long convertToDatabaseColumn(CustomerId attribute) {
            return attribute.value();
        }

        @Override
        public CustomerId convertToEntityAttribute(Long dbData) {
            return CustomerId.of(dbData);
        }
    }

    @JsonComponent
    class CustomerIdJsonSerializer extends JsonSerializer<CustomerId> {
        @Override
        public void serialize(CustomerId customerId, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeNumber(customerId.value());
        }
    }

    @JsonComponent
    class CustomerTypeJsonDeserializer extends JsonDeserializer<CustomerType> {
        @Override
        public CustomerType deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            return CustomerType.valueOf(jsonParser.getText().toUpperCase());
        }
    }

    @JsonComponent
    class PhoneNumberJsonDeserializer extends JsonDeserializer<PhoneNumber> {
        @Override
        public PhoneNumber deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            return PhoneNumber.of(jsonParser.getText());
        }
    }

    @JsonComponent
    class EmailAddressJsonDeserializer extends JsonDeserializer<EmailAddress> {
        @Override
        public EmailAddress deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            return EmailAddress.of(jsonParser.getText());
        }
    }
}
