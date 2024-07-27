package pl.owolny.identityprovider.federation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;
import java.util.function.Consumer;

@Slf4j
public class UserRepositoryOAuth2UserHandler implements Consumer<OAuth2User> {

    @Override
    public void accept(OAuth2User oAuth2User) {
        log.info("User {} created in db", oAuth2User.getName());
    }
}
