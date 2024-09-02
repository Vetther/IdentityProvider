package pl.owolny.identityprovider.federation;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import pl.owolny.identityprovider.exception.OAuth2EmailUnverifiedException;

import java.io.IOException;

import static pl.owolny.identityprovider.web.linkaccounts.LinkAccountsController.LINK_ACCOUNTS_VIEW;
import static pl.owolny.identityprovider.web.login.LoginController.LOGIN_VIEW;

@Component
public class FederatedIdentityFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String redirectUrl = UriComponentsBuilder.fromUriString("/" + LOGIN_VIEW)
                .queryParam("error", "Invalid username or password")
                .build()
                .toUriString();

        if (exception instanceof OAuth2EmailUnverifiedException OAuth2EmailUnverifiedException) {
            request.getSession().setAttribute("federatedAuth", OAuth2EmailUnverifiedException.getFederatedAuth());
            request.getSession().setAttribute("userId", OAuth2EmailUnverifiedException.getExistingUser().getId());
            request.getSession().setAttribute("userName", OAuth2EmailUnverifiedException.getExistingUser().getUsername());
            request.getSession().setAttribute("userEmail", OAuth2EmailUnverifiedException.getExistingUser().getEmail());
            redirectUrl = UriComponentsBuilder.fromUriString("/" + LINK_ACCOUNTS_VIEW).toUriString();
        }

        super.setDefaultFailureUrl(redirectUrl);
        super.onAuthenticationFailure(request, response, exception);
    }
}
