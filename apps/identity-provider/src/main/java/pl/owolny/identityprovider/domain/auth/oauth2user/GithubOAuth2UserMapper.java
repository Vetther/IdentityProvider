package pl.owolny.identityprovider.domain.auth.oauth2user;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import pl.owolny.identityprovider.domain.user.User;
import pl.owolny.identityprovider.federation.FederatedProvider;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Component("github")
class GithubOAuth2UserMapper implements OAuth2UserMapper {

    @Override
    public CustomOAuth2User map(OAuth2User oAuth2User, OAuth2UserRequest userRequest) {
        CustomOAuth2User user = new CustomOAuth2User(null);
        user.setId(UUID.randomUUID());
        user.setUsername(oAuth2User.getAttribute("name"));
        user.setCreatedAt(Instant.now());
        user.setAvatarUrl(oAuth2User.getAttribute("avatar_url"));
        user.setFederatedProvider(FederatedProvider.GITHUB);
        user.setFederatedIdentityId(Objects.requireNonNull(oAuth2User.getAttribute("id")).toString());

        List<Map<String, Object>> emails = getEmails(userRequest);
        String email = (String) emails.stream()
                .filter(m -> (Boolean) m.get("primary"))
                .findFirst()
                .map(m -> m.get("email"))
                .orElse(emails.getFirst().get("email"));
        boolean isEmailVerified = (Boolean) emails.stream()
                .filter(m -> (Boolean) m.get("primary"))
                .findFirst()
                .map(m -> m.get("verified"))
                .orElse(emails.getFirst().get("verified"));
        user.setEmail(email);
        user.setActive(isEmailVerified);
        user.setEmailVerified(isEmailVerified);

        return user;
    }

    private List<Map<String, Object>> getEmails(OAuth2UserRequest userRequest) {
        String userInfoEndpointUri = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUri();

        RestClient restClient = RestClient.builder()
                .baseUrl(userInfoEndpointUri)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "token " + userRequest.getAccessToken().getTokenValue())
                .build();

        List<Map<String, Object>> userEmails = restClient.get()
                .uri("/emails")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
        return userEmails;
    }

    @Override
    public OAuth2User map(User user, OAuth2UserRequest userRequest) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .flatMap(role -> role.getAuthorities().stream()
                        .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                )
                .collect(Collectors.toList());

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(authorities);
        customOAuth2User.setId(user.getId());
        System.out.println("user.getId() = " + user.getId());
        System.out.println("ID SET");
        return customOAuth2User;
    }
}
