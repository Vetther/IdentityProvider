package pl.owolny.identityprovider.domain.auth.oidcuser;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;
import pl.owolny.identityprovider.domain.user.User;
import pl.owolny.identityprovider.federation.FederatedProvider;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component("google")
class GoogleOidcUserMapper implements OidcUserMapper {

    @Override
    public CustomOidcUser map(OidcUser oidcUser) {
        CustomOidcUser user = new CustomOidcUser(null);
        user.setId(UUID.randomUUID());
        user.setUsername(oidcUser.getAttribute("given_name"));
        user.setEmail(oidcUser.getEmail());
        user.setActive(oidcUser.getEmailVerified());
        user.setEmailVerified(oidcUser.getEmailVerified());
        user.setCreatedAt(Instant.now());
        user.setAvatarUrl(oidcUser.getAttribute("picture"));
        user.setFederatedProvider(FederatedProvider.GOOGLE);
        user.setFederatedIdentityId(oidcUser.getSubject());
        return user;
    }

    @Override
    public CustomOidcUser map(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .flatMap(role -> role.getAuthorities().stream()
                        .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                )
                .collect(Collectors.toList());

        CustomOidcUser customOidcUser = new CustomOidcUser(authorities);
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
