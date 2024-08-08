package pl.owolny.identityprovider.domain.auth.oauth2user;

import org.springframework.security.oauth2.core.user.OAuth2User;
import pl.owolny.identityprovider.domain.user.User;

public interface OAuth2UserMapper {

    /**
     * Creates new CustomOAuth2User which will be saved in database as a new user
     *
     * @return OAuth2User
     */
    OAuth2User map(OAuth2User oAuth2User);

    /**
     * Maps existing user to CustomOAuth2User due to the fact that user already has linked account
     *
     * @return OAuth2User
     */
//    OAuth2User map(Map<String, Object> attributes, User user);

    OAuth2User map(User user);
}