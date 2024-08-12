package pl.owolny.identityprovider.federation;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import pl.owolny.identityprovider.exception.AccountLinkingRequiredException;

import java.io.IOException;

import static pl.owolny.identityprovider.web.linkaccounts.LinkAccountsController.LINK_ACCOUNTS;
import static pl.owolny.identityprovider.web.login.LoginController.LOGIN_VIEW;

@Component
public class FederatedIdentityFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String redirectUrl = "/" + LOGIN_VIEW + "?error=" + exception.getLocalizedMessage();

        if (exception instanceof AccountLinkingRequiredException accountLinkingRequiredException) {
            request.getSession().setAttribute("federatedAuth", accountLinkingRequiredException.getFederatedAuth());
            request.getSession().setAttribute("linkingUserId", accountLinkingRequiredException.getExistingUser().getId());
            request.getSession().setAttribute("linkingUserEmail", accountLinkingRequiredException.getExistingUser().getEmail());
            redirectUrl = UriComponentsBuilder.fromUriString("/" + LINK_ACCOUNTS).toUriString();
        }

        super.setDefaultFailureUrl(redirectUrl);
        super.onAuthenticationFailure(request, response, exception);
    }
}
