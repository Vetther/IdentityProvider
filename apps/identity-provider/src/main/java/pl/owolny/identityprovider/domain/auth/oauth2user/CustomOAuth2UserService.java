package pl.owolny.identityprovider.domain.auth.oauth2user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import pl.owolny.identityprovider.domain.user.User;
import pl.owolny.identityprovider.domain.user.UserRepository;
import pl.owolny.identityprovider.federation.FederatedProvider;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final Map<String, OAuth2UserMapper> mappers;

    public CustomOAuth2UserService(UserRepository userRepository, Map<String, OAuth2UserMapper> mappers) {
        this.userRepository = userRepository;
        this.mappers = mappers;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        OAuth2UserMapper mapper = getMapper(userRequest.getClientRegistration().getRegistrationId());

        Optional<User> user = this.userRepository.findByFederatedIdentityAccounts_ProviderAndFederatedIdentityAccounts_FederatedIdentityId(
                FederatedProvider.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase()),
                oAuth2User.getName()
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
            return mapper.map(user.get());
        }

        String email = oAuth2User.getAttribute("email");
        user = this.userRepository.findByEmail(email);

        if (user.isPresent()) {
            log.info("Account with email {} already exists but does not have linked account", email);
            return mapper.map(user.get());
        }

        log.info("Account with email {} does not exist", email);
        return mapper.map(oAuth2User);
    }

    private OAuth2UserMapper getMapper(String registrationId) {
        Assert.isTrue(mappers.containsKey(registrationId), "No mapper defined for registrationId " + registrationId);
        return mappers.get(registrationId);
    }
}
