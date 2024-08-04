package pl.owolny.identityprovider.user.federation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@JsonDeserialize
@JsonSerialize
public class CustomOidcUser extends DefaultOidcUser {

    private UUID userId;

    public CustomOidcUser(Collection<? extends GrantedAuthority> authorities,
                          OidcIdToken oidcIdToken,
                          OidcUserInfo oidcUserInfo,
                          UUID userId) {
        super(authorities, oidcIdToken, oidcUserInfo, "sub");
        this.userId = userId;
    }

    @JsonCreator
    public CustomOidcUser(@JsonProperty("authorities") Collection<? extends GrantedAuthority> authorities,
                          @JsonProperty("idToken") OidcIdToken oidcIdToken,
                          @JsonProperty("oidcUserInfo") OidcUserInfo oidcUserInfo) {
        super(authorities, oidcIdToken, oidcUserInfo, "sub");
    }

    @Override
    public Map<String, Object> getAttributes() {
        Map<String, Object> attributes = new HashMap<>(super.getAttributes());
        attributes.put("sub", this.userId.toString());
        return attributes;
    }

    @Override
    public String getName() {
        return this.userId.toString();
    }
}