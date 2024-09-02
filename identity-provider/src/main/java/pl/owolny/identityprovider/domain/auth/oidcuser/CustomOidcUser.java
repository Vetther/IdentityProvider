package pl.owolny.identityprovider.domain.auth.oidcuser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import pl.owolny.identityprovider.federation.FederatedAuth;
import pl.owolny.identityprovider.federation.FederatedProvider;

import java.time.Instant;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CustomOidcUser implements OidcUser, FederatedAuth {

    private UUID id;
    private boolean isActive;
    private boolean isEmailVerified;
    private Instant createdAt;
    private String username;
    private String email;
    private String avatarUrl;
    private String federatedIdentityId;
    private FederatedProvider federatedProvider;
    private Collection<? extends GrantedAuthority> authorities = new ArrayList<>();
    private Map<String, Object> attributes = new HashMap<>();

    public CustomOidcUser(Collection<? extends GrantedAuthority> authorities) {

        if (authorities != null) {
            this.authorities = authorities;
        }
    }

    @Override
    public String getName() {
        return this.id != null ? this.id.toString() : null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public Map<String, Object> getClaims() {
        return this.attributes;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return null;
    }

    @Override
    public OidcIdToken getIdToken() {
        return null;
    }
}