package org.habilisoft.zemi.user.support;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.experimental.UtilityClass;
import org.habilisoft.zemi.user.Username;
import org.habilisoft.zemi.user.domain.PermissionName;
import org.habilisoft.zemi.user.domain.RoleName;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@UtilityClass
class Converters {
    // username
    @Converter(autoApply = true)
    class UsernameJpaConverter implements AttributeConverter<Username, String> {
        @Override
        public String convertToDatabaseColumn(Username attribute) {
            return attribute.value();
        }

        @Override
        public Username convertToEntityAttribute(String dbData) {
            return Username.of(dbData);
        }
    }
    @JsonComponent
    class UsernameJsonDeserializer extends JsonDeserializer<Username> {
        @Override
        public Username deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            return Username.of(jsonParser.getText());
        }
    }
    @JsonComponent
    class UsernameJsonSerializer extends JsonSerializer<Username> {
        @Override
        public void serialize(Username username, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(username.value());
        }
    }

    // role name
    @Converter(autoApply = true)
    class RoleNameJpaConverter implements AttributeConverter<RoleName, String> {
        @Override
        public String convertToDatabaseColumn(RoleName attribute) {
            return attribute.value();
        }

        @Override
        public RoleName convertToEntityAttribute(String dbData) {
            return RoleName.from(dbData);
        }
    }
    @JsonComponent
    class RoleNameJsonDeserializer extends JsonDeserializer<RoleName> {
        @Override
        public RoleName deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            return RoleName.from(jsonParser.getText());
        }
    }
    @JsonComponent
    class RoleNameJsonSerializer extends JsonSerializer<RoleName> {
        @Override
        public void serialize(RoleName roleName, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(roleName.value());
        }
    }

    // permission name
    @Converter(autoApply = true)
    class PermissionNameJpaConverter implements AttributeConverter<PermissionName, String> {
        @Override
        public String convertToDatabaseColumn(PermissionName attribute) {
            return attribute.value();
        }

        @Override
        public PermissionName convertToEntityAttribute(String dbData) {
            return PermissionName.from(dbData);
        }
    }
    @JsonComponent
    class PermissionNameJsonDeserializer extends JsonDeserializer<PermissionName> {
        @Override
        public PermissionName deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            return PermissionName.from(jsonParser.getText());
        }
    }
    @JsonComponent
    class PermissionNameJsonSerializer extends JsonSerializer<PermissionName> {
        @Override
        public void serialize(PermissionName permissionName, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(permissionName.value());
        }
    }
}
