package pl.owolny.identityprovider.domain.auth.oidcuser;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import pl.owolny.identityprovider.domain.user.User;
import pl.owolny.identityprovider.domain.user.UserRepository;
import pl.owolny.identityprovider.federation.FederatedProvider;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class CustomOidcUserService extends OidcUserService {

    private final UserRepository userRepository;
    private final Map<String, OidcUserMapper> mappers;

    public CustomOidcUserService(UserRepository userRepository, Map<String, OidcUserMapper> mappers) {
        this.userRepository = userRepository;
        this.mappers = mappers;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        OidcUserMapper mapper = getMapper(userRequest.getClientRegistration().getRegistrationId());

        Optional<User> user = this.userRepository.findByFederatedIdentityAccounts_ProviderAndFederatedIdentityAccounts_FederatedIdentityId(
                FederatedProvider.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase()),
                oidcUser.getName()
        );

//        Optional<UserDto> user = Optional.of(UserDto.builder()
//                .id(UUID.randomUUID())
//                .email("test@test.com")
//                .username("testuser")
//                .createdAt(Instant.now().getEpochSecond())
//                .isActive(true)
//                .rolesDto(new HashSet<>(Set.of(new UserDto.roleDto(UUID.randomUUID(), "USER", Set.of(new UserDto.AuthorityDto(UUID.randomUUID(), "AUTHORITY_SMF"))))))
//                .build()
//        );

        if (user.isPresent()) {
            log.info("Account linked with that federated account already exists");
            return mapper.map(oidcUser.getIdToken(), oidcUser.getUserInfo(), user.get());
        }

        String email = userRequest.getIdToken().getEmail();
        user = this.userRepository.findByEmail(email);

        if (user.isPresent()) {
            log.info("Account with email {} already exists but does not have linked account", email);
            return mapper.map(oidcUser.getIdToken(), oidcUser.getUserInfo(), user.get());
        }

        log.info("Account with email {} does not exist", email);
        return mapper.map(oidcUser);
    }

    private OidcUserMapper getMapper(String registrationId) {
        Assert.isTrue(mappers.containsKey(registrationId), "No mapper defined for registrationId " + registrationId);
        return mappers.get(registrationId);
    }
}
