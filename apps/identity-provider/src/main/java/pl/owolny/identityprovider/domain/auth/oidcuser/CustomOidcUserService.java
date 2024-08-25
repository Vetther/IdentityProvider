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
import pl.owolny.identityprovider.exception.OAuth2EmailUnverifiedException;
import pl.owolny.identityprovider.federation.FederatedAuth;
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

        Optional<User> userOpt = this.userRepository.findByFederatedIdentityAccounts_ProviderAndFederatedIdentityAccounts_FederatedIdentityId(
                FederatedProvider.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase()),
                oidcUser.getName()
        );

        if (userOpt.isPresent()) {
            log.info("User linked with that federated account already exists");
            return mapper.map(userOpt.get());
        }

        FederatedAuth federatedAuth = (FederatedAuth) mapper.map(oidcUser);
        String email = federatedAuth.getEmail();
        userOpt = this.userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            log.info("User with email {} does not exist", email);
            return mapper.map(oidcUser);
        }

        log.info("User with email {} already exists but does not have linked account", email);
        User user = userOpt.get();

        if (user.isEmailVerified()) {
            log.info("User with email {} already exists and is verified", email);
        } else {
            log.info("User with email {} already exists but is not verified", email);
        }

        // TODO: Uncomment this block after successful email verification implementation
//        if (federatedAuth.isEmailVerified()) {
//            log.info("Federated account's email {} is verified", federatedAuth.getEmail());
//            log.info("Linking account with federated account");
//            return mapper.map(user);
//        }

        log.info("Federated account's email {} is not verified", federatedAuth.getEmail());
        throw new OAuth2EmailUnverifiedException(federatedAuth, user);
    }

    private OidcUserMapper getMapper(String registrationId) {
        Assert.isTrue(mappers.containsKey(registrationId), "No mapper defined for registrationId " + registrationId);
        return mappers.get(registrationId);
    }
}
