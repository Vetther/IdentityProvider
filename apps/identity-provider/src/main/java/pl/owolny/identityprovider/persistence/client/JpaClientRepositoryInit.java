package pl.owolny.identityprovider.persistence.client;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

@Slf4j
@Component
class JpaClientRepositoryInit {

    private final TokenSettings tokenSettings;
    private final JpaRegisteredClientRepository registeredClientRepository;

    public JpaClientRepositoryInit(TokenSettings tokenSettings, JpaRegisteredClientRepository registeredClientRepository) {
        this.tokenSettings = tokenSettings;
        this.registeredClientRepository = registeredClientRepository;
    }

    @PostConstruct
    public void registerClients() {
        Stream.of(
                /*
                  Registered client
                 */
                RegisteredClient.withId(UUID.randomUUID().toString())
                        .clientId("client")
                        .clientSecret("{noop}secret")
                        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                        .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                        .redirectUris(uri -> uri.addAll(List.of("http://localhost:3000", "https://oauth.pstmn.io/v1/callback")))
                        .postLogoutRedirectUri("http://127.0.0.1:9000/login")
                        .scopes(scopes -> scopes.addAll(Set.of(OidcScopes.OPENID, OidcScopes.PROFILE, OidcScopes.EMAIL)))
                        .clientSettings(ClientSettings.builder().requireAuthorizationConsent(false).build())
                        .tokenSettings(this.tokenSettings)
                        .build(),
                /*
                  Registered client for user-service
                 */
                RegisteredClient.withId("user-service-client")
                        .clientId("user-service-client")
                        .clientSecret("{noop}secret")
                        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                        .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                        .scopes(scopes -> scopes.addAll(Set.of(OidcScopes.OPENID, OidcScopes.PROFILE, OidcScopes.EMAIL)))
                        .tokenSettings(this.tokenSettings)
                        .build()
        ).forEach(client -> {
            if (this.registeredClientRepository.findByClientId(client.getClientId()) == null) {
                log.info("Creating client {} in db", client.getClientId());
                this.registeredClientRepository.save(client);
            }
        });
    }
}
