package pl.owolny.identityprovider.domain.auth.oidcuser;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;
import pl.owolny.identityprovider.domain.user.User;
import pl.owolny.identityprovider.federation.FederatedProvider;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component("google")
class GoogleOidcUserMapper implements OidcUserMapper {

    @Override
    public CustomOidcUser map(OidcUser oidcUser) {
        CustomOidcUser user = new CustomOidcUser(oidcUser.getIdToken(), oidcUser.getUserInfo());
        user.setUsername(oidcUser.getAttribute("given_name"));
        user.setEmail(oidcUser.getEmail());
        user.setActive(true);
        user.setCreatedAt(Instant.now());
        user.setAvatarUrl(oidcUser.getAttribute("picture"));
        user.setFederatedProvider(FederatedProvider.GOOGLE);
        return user;
    }

    @Override
    public CustomOidcUser map(OidcIdToken idToken, OidcUserInfo userInfo, User user) {
        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .flatMap(role -> role.getAuthorities().stream()
                        .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                )
                .collect(Collectors.toSet());

        Map<String, Object> claims = new HashMap<>(idToken.getClaims());

        OidcIdToken customIdToken = new OidcIdToken(idToken.getTokenValue(), idToken.getIssuedAt(), idToken.getExpiresAt(), claims);

        CustomOidcUser customOidcUser = new CustomOidcUser(authorities, customIdToken, userInfo);
        customOidcUser.setId(user.getId());
        System.out.println("user.getId() = " + user.getId());
        System.out.println("ID SET");
//        customOidcUser.setUsername(userDto.username());
//        customOidcUser.setEmail(userDto.email());
//        customOidcUser.setCreatedAt(Instant.ofEpochSecond(userDto.createdAt()));
//        customOidcUser.setActive(userDto.isActive());
        return customOidcUser;
    }
}
