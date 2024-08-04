package pl.owolny.identityprovider.federation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.function.BiConsumer;

@Slf4j
public class UserRepositoryOAuth2UserHandler implements BiConsumer<OAuth2User, FederatedProvider> {

    @Override
    public void accept(OAuth2User oAuth2User, FederatedProvider federatedProvider) {
    }
}
