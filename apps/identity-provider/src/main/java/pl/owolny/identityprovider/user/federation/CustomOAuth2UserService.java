package pl.owolny.identityprovider.user.federation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import pl.owolny.identityprovider.federation.FederatedProvider;
import pl.owolny.identityprovider.user.UserService;

import java.util.UUID;

@Slf4j
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserService userService;

    public CustomOAuth2UserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("User {} loaded: {}", oAuth2User.getName(), userRequest.getClientRegistration().getRegistrationId());
        if (this.userService.getUserByFederatedAccount(FederatedProvider.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase()), oAuth2User.getName()).isPresent()) {
            log.info("User {} already has linked account with provider {}", oAuth2User.getName(), userRequest.getClientRegistration().getRegistrationId());
            // TODO: user already has linked account
            //  get user id
            //  return CustomOAuth2User with user id
        }
        // user does not have linked account
        if (this.userService.getUser(oAuth2User.getAttribute("email")).isPresent()) {
            log.info("User {} already has account with the same email but does not have linked account", oAuth2User.getName());
            // TODO: user already has account with the same email but does not have linked account
            //  add federated account to existing user
            //  return CustomOAuth2User with user id
        } else {
            log.info("User {} does not have account", oAuth2User.getName());
            // TODO: user does not have account with the same email
            //  create new user
            //  return CustomOAuth2User with user id
        }
        return new CustomOAuth2User(oAuth2User.getAuthorities(), oAuth2User.getAttributes(), UUID.randomUUID());
    }
}
