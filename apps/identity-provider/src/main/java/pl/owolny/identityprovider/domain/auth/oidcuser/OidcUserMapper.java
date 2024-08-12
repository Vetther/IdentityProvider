package pl.owolny.identityprovider.domain.auth.oidcuser;

import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import pl.owolny.identityprovider.domain.user.User;

public interface OidcUserMapper {

    /**
     * Creates new CustomOidcUser which will be saved in database as a new user
     *
     * @return OidcUser
     */
    OidcUser map(OidcUser oidcUser);

    /**
     * Maps existing user to CustomOidcUser due to the fact that user already has linked account
     *
     * @return OidcUser
     */
    OidcUser map(User user);
}