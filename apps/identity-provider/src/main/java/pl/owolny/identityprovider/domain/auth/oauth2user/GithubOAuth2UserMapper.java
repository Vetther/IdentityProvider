package pl.owolny.identityprovider.domain.auth.oauth2user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import pl.owolny.identityprovider.domain.user.User;
import pl.owolny.identityprovider.federation.FederatedProvider;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component("github")
class GithubOAuth2UserMapper implements OAuth2UserMapper {

    @Override
    public CustomOAuth2User map(OAuth2User oAuth2User) {
        CustomOAuth2User user = new CustomOAuth2User(null, oAuth2User.getAttributes(), "id");
        user.setUsername(oAuth2User.getAttribute("name"));
        user.setEmail(oAuth2User.getAttribute("email"));
        user.setActive(true);
        user.setCreatedAt(Instant.now());
        user.setAvatarUrl(oAuth2User.getAttribute("avatar_url"));
        user.setFederatedProvider(FederatedProvider.GITHUB);
        return user;
    }

//    @Override
//    public CustomOAuth2User map(Map<String, Object> attributes, User user) {
//        Set<GrantedAuthority> authorities = user.getRoles().stream()
//                .flatMap(role -> role.getAuthorities().stream()
//                        .map(authority -> new SimpleGrantedAuthority(authority.getName()))
//                )
//                .collect(Collectors.toSet());
//
//        Map<String, Object> claims = new HashMap<>(attributes);
//        claims.put("sub", user.getId().toString());
//
//        CustomOAuth2User oAuth2User = new CustomOAuth2User(authorities, claims, "id");
//        user.setId(user.getId());
////        user.setUsername(userDto.username());
////        user.setEmail(userDto.email());
////        user.setCreatedAt(Instant.ofEpochSecond(userDto.createdAt()));
////        user.setActive(userDto.isActive());
//        return oAuth2User;
//    }

    @Override
    public OAuth2User map(User user) {
        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .flatMap(role -> role.getAuthorities().stream()
                        .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                )
                .collect(Collectors.toSet());

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(authorities, Map.of("sub", user.getId().toString()), "sub");
        customOAuth2User.setId(user.getId());
        System.out.println("user.getId() = " + user.getId());
        System.out.println("ID SET");
        return customOAuth2User;
    }
}
