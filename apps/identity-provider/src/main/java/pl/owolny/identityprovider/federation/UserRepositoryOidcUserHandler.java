package pl.owolny.identityprovider.federation;

import com.nimbusds.oauth2.sdk.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import pl.owolny.identityprovider.domain.auth.FederatedIdentityAccount;
import pl.owolny.identityprovider.domain.user.User;
import pl.owolny.identityprovider.domain.user.UserProfile;
import pl.owolny.identityprovider.domain.user.UserService;

import java.util.Collection;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@Slf4j
public class UserRepositoryOidcUserHandler implements BiConsumer<OidcUser, FederatedProvider> {

    private final UserService userService;

    public UserRepositoryOidcUserHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void accept(OidcUser oidcUser, FederatedProvider federatedProvider) {
        FederatedAuth federatedAuth = (FederatedAuth) oidcUser;
        log.info("email: {}", federatedAuth.getEmail());
        log.info("username: {}", federatedAuth.getUsername());
        log.info("authorities: {}", federatedAuth.getAuthorities());
        log.info("id: {}", federatedAuth.getId());
        log.info("isActive: {}", federatedAuth.isActive());
        log.info("createdAt: {}", federatedAuth.getCreatedAt());
        log.info("avatar: {}", federatedAuth.getAvatarUrl());
        log.info("provider: {}", federatedProvider);
        log.info("providerId: {}", oidcUser.getName());

        // check if user already exists
        if (federatedAuth.getId() != null) {
            return;
        }

        User user = this.userService.createFromFederatedAccount(
                federatedAuth.getUsername(),
                federatedAuth.getEmail(),
                federatedAuth.isActive(),
                true, // TODO
                UserProfile.builder()
                        .avatarUrl(federatedAuth.getAvatarUrl())
                        .firstName(null) // TODO
                        .lastName(null) // TODO
                        .phoneNumber(null) // TODO
                        .gender(null) // TODO
                        .countryCode(null) // TODO
                        .build(),
                FederatedIdentityAccount.builder()
                        .username(federatedAuth.getUsername())
                        .federatedIdentityId(oidcUser.getName())
                        .provider(federatedProvider)
                        .email(federatedAuth.getEmail())
                        .isEmailVerified(true) // TODO
                        .build()
        );

        Collection<GrantedAuthority> grantedAuthorities = (Collection<GrantedAuthority>) oidcUser.getAuthorities();

        if (!CollectionUtils.isEmpty(user.getRoles())) {
            Set<? extends GrantedAuthority> authorities = user.getRoles().stream()
                    .flatMap(role -> role.getAuthorities().stream()
                            .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                    )
                    .collect(Collectors.toSet());

            grantedAuthorities.addAll(authorities);
        }

        federatedAuth.setId(user.getId());
    }
}
