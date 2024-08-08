package pl.owolny.identityprovider.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.client.RestClient;
import pl.owolny.identityprovider.config.interceptor.OAuth2ClientHttpRequestInterceptor;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient(OAuth2AuthorizedClientManager authorizedClientManager,
                                 OAuth2AuthorizedClientRepository authorizedClientRepository) {

        OAuth2ClientHttpRequestInterceptor requestInterceptor =
                new OAuth2ClientHttpRequestInterceptor(authorizedClientManager, "user-service");

        // Configure the interceptor to remove invalid authorized clients
        requestInterceptor.setAuthorizedClientRepository(authorizedClientRepository);

        return RestClient.builder()
                .requestInterceptor(requestInterceptor)
                .build();
    }
}
