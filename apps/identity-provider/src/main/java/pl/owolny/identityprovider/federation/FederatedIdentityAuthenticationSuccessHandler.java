package pl.owolny.identityprovider.federation;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;
import java.util.function.BiConsumer;

@Slf4j
public final class FederatedIdentityAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final AuthenticationSuccessHandler delegate = new SavedRequestAwareAuthenticationSuccessHandler();
    private final BiConsumer<OidcUser, FederatedProvider> oidcUserHandler = (user, federatedProvider) -> this.oauth2UserHandler.accept(user, federatedProvider);
    private BiConsumer<OAuth2User, FederatedProvider> oauth2UserHandler;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication instanceof OAuth2AuthenticationToken auth && authentication.getPrincipal() instanceof OidcUser oidcUser) {
            log.info("OIDC User {} authentication successful", oidcUser.getName());
//            this.oidcUserHandler.accept(oidcUser, resolveFederatedProvider(auth));
        } else if (authentication instanceof OAuth2AuthenticationToken auth && authentication.getPrincipal() instanceof OAuth2User oAuth2User) {
            log.info("OAuth2 User {} authentication successful", oAuth2User.getName());
//            this.oauth2UserHandler.accept(oAuth2User, resolveFederatedProvider(auth));
        }
        this.delegate.onAuthenticationSuccess(request, response, authentication);
    }

    private FederatedProvider resolveFederatedProvider(OAuth2AuthenticationToken authentication) {
        return FederatedProvider.valueOf(authentication.getAuthorizedClientRegistrationId().toUpperCase());
    }

    public void setOAuth2UserHandler(BiConsumer<OAuth2User, FederatedProvider> oauth2UserHandler) {
        this.oauth2UserHandler = oauth2UserHandler;
    }

//    String ipAddress = request.getHeader("X-Forwarded-For");
//        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
//        ipAddress = request.getRemoteAddr();
//    }
//    Client userAgent = new Parser().parse(request.getHeader("User-Agent"));
//    log.info("User with IP {} and User-Agent {} authenticated", ipAddress, userAgent.userAgent.family);
//    log.info(request.getHeader("User-Agent"));

}