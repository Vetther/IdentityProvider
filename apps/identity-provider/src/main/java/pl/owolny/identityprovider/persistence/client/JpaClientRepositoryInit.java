package pl.owolny.identityprovider.persistence.client;

import jakarta.annotation.PostConstruct;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Component
public class JpaClientRepositoryInit {

    private final ClientRepository clientRepository;

    public JpaClientRepositoryInit(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @PostConstruct
    void init(){
        clientCreation();
    }

    private void clientCreation() {
        if (clientRepository.findByClientId("client").isPresent()) {
            return;
        }

        Client client = Client.builder()
                .id(UUID.randomUUID().toString())
                .clientId("client")
                .clientSecret("{noop}secret")
                .clientAuthenticationMethods(convert(ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue()))
                .authorizationGrantTypes(convert(AuthorizationGrantType.AUTHORIZATION_CODE.getValue(), AuthorizationGrantType.REFRESH_TOKEN.getValue()))
                .redirectUris(convert("http://localhost:3000", "https://oauth.pstmn.io/v1/callback"))
                .scopes(convert(OidcScopes.OPENID))
                .clientSettings(null)
                .build();

        clientRepository.save(client);
    }

    private <T> String convert(T... elements) {
        return StringUtils.collectionToCommaDelimitedString(List.of(elements));
    }
}
