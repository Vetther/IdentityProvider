package pl.owolny.identityprovider.user.federation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomOidcUserService extends OidcUserService {

    private final CustomOAuth2UserService customOAuth2UserService;

    public CustomOidcUserService(CustomOAuth2UserService customOAuth2UserService) {
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        CustomOAuth2User oAuth2User = (CustomOAuth2User) this.customOAuth2UserService.loadUser(userRequest);
        return new CustomOidcUser(oAuth2User.getAuthorities(), oidcUser.getIdToken(), oidcUser.getUserInfo(), oAuth2User.getUserId());
    }
}
