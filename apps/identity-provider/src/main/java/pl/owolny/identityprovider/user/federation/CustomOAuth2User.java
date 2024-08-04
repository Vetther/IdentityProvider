package pl.owolny.identityprovider.user.federation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@JsonDeserialize
@JsonSerialize
public class CustomOAuth2User extends DefaultOAuth2User {

    private UUID userId;

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes,
                            UUID userId) {
        super(authorities, createAttributes(attributes, userId), "sub");
        this.userId = userId;
    }

    @JsonCreator
    public CustomOAuth2User(@JsonProperty("authorities") Collection<? extends GrantedAuthority> authorities,
                            @JsonProperty("attributes") Map<String, Object> attributes) {
        super(authorities, attributes, "sub");
    }

    @Override
    public String getName() {
        return this.userId.toString();
    }

    private static Map<String, Object> createAttributes(Map<String, Object> attributes, UUID userId) {
        if (attributes == null) {
            attributes = new HashMap<>();
        } else {
            attributes = new HashMap<>(attributes);
        }
        attributes.put("sub", userId.toString());
        return attributes;
    }
}