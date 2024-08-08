package pl.owolny.identityprovider.domain.auth.oidcuser;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import pl.owolny.identityprovider.federation.FederatedAuth;
import pl.owolny.identityprovider.federation.FederatedProvider;

import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

@Getter
@Setter
@JsonDeserialize
@JsonSerialize
class CustomOidcUser extends DefaultOidcUser implements FederatedAuth {

    private UUID id;
    private boolean isActive;
    private Instant createdAt;
    private String username;
    private String email;
    private String avatarUrl;
    private FederatedProvider federatedProvider;
    private Collection<? extends GrantedAuthority> authorities = new HashSet<>();

    @JsonCreator
    public CustomOidcUser(@JsonProperty("authorities") Collection<? extends GrantedAuthority> authorities,
                          @JsonProperty("idToken") OidcIdToken oidcIdToken,
                          @JsonProperty("oidcUserInfo") OidcUserInfo oidcUserInfo) {
        super(AuthorityUtils.NO_AUTHORITIES, oidcIdToken, oidcUserInfo, IdTokenClaimNames.SUB);

        if (authorities != null) {
            this.authorities = authorities;
        }
    }

    public CustomOidcUser(OidcIdToken oidcIdToken, OidcUserInfo oidcUserInfo) {
        super(AuthorityUtils.NO_AUTHORITIES, oidcIdToken, oidcUserInfo);
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