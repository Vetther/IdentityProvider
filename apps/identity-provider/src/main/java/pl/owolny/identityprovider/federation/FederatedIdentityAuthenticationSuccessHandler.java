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
import ua_parser.Client;
import ua_parser.Parser;

import java.io.IOException;
import java.util.function.Consumer;

@Slf4j
public final class FederatedIdentityAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final AuthenticationSuccessHandler delegate = new SavedRequestAwareAuthenticationSuccessHandler();

    private Consumer<OAuth2User> oauth2UserHandler = (user) -> {
    };

    private final Consumer<OidcUser> oidcUserHandler = (user) -> this.oauth2UserHandler.accept(user);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication instanceof OAuth2AuthenticationToken) {
            if (authentication.getPrincipal() instanceof OidcUser oidcUser) {
                log.info("OIDC User {} authentication successful", oidcUser.getName());
                this.oidcUserHandler.accept(oidcUser);
            } else if (authentication.getPrincipal() instanceof OAuth2User oAuth2User) {
                log.info("OAuth2 User {} authentication successful", oAuth2User.getName());
                this.oauth2UserHandler.accept(oAuth2User);
            }
        }
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        Client userAgent = new Parser().parse(request.getHeader("User-Agent"));
        log.info("User with IP {} and User-Agent {} authenticated", ipAddress, userAgent.userAgent.family);
        log.info(request.getHeader("User-Agent"));
        this.delegate.onAuthenticationSuccess(request, response, authentication);
    }

    public void setOAuth2UserHandler(Consumer<OAuth2User> oauth2UserHandler) {
        this.oauth2UserHandler = oauth2UserHandler;
    }
}