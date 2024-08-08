package pl.owolny.identityprovider.domain.auth.oauth2user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import pl.owolny.identityprovider.domain.user.User;
import pl.owolny.identityprovider.federation.FederatedAuth;
import pl.owolny.identityprovider.federation.FederatedProvider;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@JsonDeserialize
@JsonSerialize
public class CustomOAuth2User extends DefaultOAuth2User implements FederatedAuth {

    private UUID id;
    private boolean isActive;
    private Instant createdAt;
    private String username;
    private String email;
    private String avatarUrl;
    private FederatedProvider federatedProvider;
    private Collection<? extends GrantedAuthority> authorities = new HashSet<>();

    @JsonCreator
    public CustomOAuth2User(@JsonProperty("authorities") Collection<? extends GrantedAuthority> authorities,
                            @JsonProperty("attributes") Map<String, Object> attributes) {
        super(AuthorityUtils.NO_AUTHORITIES, attributes, "sub");

        if (authorities != null) {
            this.authorities = authorities;
        }
    }

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes,
                            String attributeNameKey) {
        super(AuthorityUtils.NO_AUTHORITIES, createAttributes(attributes, attributeNameKey), "sub");

        if (authorities != null) {
            this.authorities = authorities;
        }
    }

    public static CustomOAuth2User fromUser(User user) {
        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .flatMap(role -> role.getAuthorities().stream()
                        .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                )
                .collect(Collectors.toSet());

        return new CustomOAuth2User(authorities, Map.of("sub", user.getId().toString()), "sub");
    }

    private static Map<String, Object> createAttributes(Map<String, Object> attributes, String attributeNameKey) {
        if (attributes == null) {
            return new HashMap<>();
        }
        HashMap<String, Object> attributesMap = new HashMap<>(attributes);
        attributesMap.put("sub", attributes.get(attributeNameKey));
        return attributesMap;
    }

    @Override
    public String getName() {
        return this.id != null ? this.id.toString() : super.getName();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }
}