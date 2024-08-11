package pl.owolny.identityprovider.domain.auth.user.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;
import org.springframework.security.core.GrantedAuthority;
import pl.owolny.identityprovider.domain.auth.user.CustomUserDetails;

import java.io.IOException;
import java.util.List;

public class CustomUserDetailsDeserializer extends JsonDeserializer<CustomUserDetails> {

    @Override
    public CustomUserDetails deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        JsonNode jsonNode = mapper.readTree(jsonParser);

        String id = readJsonNode(jsonNode, "id").asText();
        boolean isActive = readJsonNode(jsonNode, "isActive").asBoolean();
        List<GrantedAuthority> authorities = mapper.readerForListOf(GrantedAuthority.class).readValue(jsonNode.get("authorities"));

        return CustomUserDetails.builder()
                .id(id)
                .authorities(authorities)
                .isActive(isActive)
                .build();
    }

    private JsonNode readJsonNode(JsonNode jsonNode, String field) {
        return jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance();
    }
}