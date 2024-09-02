package pl.owolny.identityprovider.domain.auth.oidcuser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;
import org.springframework.security.core.GrantedAuthority;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class CustomOidcUserJacksonDeserializer extends JsonDeserializer<CustomOidcUser> {

    @Override
    public CustomOidcUser deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        JsonNode jsonNode = mapper.readTree(jsonParser);

        String id = readJsonNode(jsonNode, "id").asText();
        boolean isActive = readJsonNode(jsonNode, "isActive").asBoolean();
        List<GrantedAuthority> authorities = mapper.readerForListOf(GrantedAuthority.class).readValue(jsonNode.get("authorities"));

        return CustomOidcUser.builder()
                .id(UUID.fromString(id))
                .authorities(authorities)
                .isActive(isActive)
                .build();
    }

    private JsonNode readJsonNode(JsonNode jsonNode, String field) {
        return jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance();
    }
}