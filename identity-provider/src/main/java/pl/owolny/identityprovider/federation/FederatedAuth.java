package pl.owolny.identityprovider.federation;

import org.springframework.security.core.GrantedAuthority;

import java.time.Instant;
import java.util.Collection;
import java.util.UUID;

public interface FederatedAuth {

    UUID getId();

    void setId(UUID id);

    String getFederatedIdentityId();

    FederatedProvider getFederatedProvider();

    String getAvatarUrl();

    String getEmail();

    Collection<? extends GrantedAuthority> getAuthorities();

    boolean isActive();

    boolean isEmailVerified();

    String getUsername();

    Instant getCreatedAt();

}
