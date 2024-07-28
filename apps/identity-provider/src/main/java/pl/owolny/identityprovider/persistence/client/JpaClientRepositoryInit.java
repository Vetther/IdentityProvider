package pl.owolny.identityprovider.persistence.client;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Component
public class JpaClientRepositoryInit {

    private static final Logger log = LoggerFactory.getLogger(JpaClientRepositoryInit.class);
    private final ClientRepository clientRepository;

    public JpaClientRepositoryInit(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @PostConstruct
    void init() {
        clientCreation();
    }

    private void clientCreation() {
        Client client = Client.builder()
                .id(UUID.randomUUID().toString())
                .clientId("client")
                .clientSecret("{noop}secret")
                .clientAuthenticationMethods(convert(ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue()))
                .authorizationGrantTypes(convert(AuthorizationGrantType.AUTHORIZATION_CODE.getValue(), AuthorizationGrantType.REFRESH_TOKEN.getValue()))
                .redirectUris(convert("http://localhost:3000", "https://oauth.pstmn.io/v1/callback"))
                .scopes(convert(OidcScopes.OPENID, OidcScopes.PROFILE, OidcScopes.EMAIL))
                .clientSettings(null)
                .build();

        Client idpClient = Client.builder()
                .id(UUID.randomUUID().toString())
                .clientId("idp-client")
                .clientSecret("{noop}secret")
                .clientAuthenticationMethods(convert(ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue()))
                .authorizationGrantTypes(convert(AuthorizationGrantType.CLIENT_CREDENTIALS.getValue()))
                .redirectUris(null)
                .clientSettings(null)
                .build();


        if (clientRepository.findByClientId("client").isEmpty()) {
            log.info("Creating client");
            clientRepository.save(client);
        }
        if (clientRepository.findByClientId("idp-client").isEmpty()) {
            log.info("Creating idp-client");
            clientRepository.save(idpClient);
        }
    }

    @SafeVarargs
    private <T> String convert(T... elements) {
        return StringUtils.collectionToCommaDelimitedString(List.of(elements));
    }
}
