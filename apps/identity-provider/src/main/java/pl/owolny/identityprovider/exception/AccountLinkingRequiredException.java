package pl.owolny.identityprovider.exception;


import lombok.Getter;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import pl.owolny.identityprovider.domain.user.User;
import pl.owolny.identityprovider.federation.FederatedAuth;

@Getter
public class AccountLinkingRequiredException extends OAuth2AuthenticationException {

    public static String ERROR_CODE = "account_linking_required";
    private final FederatedAuth federatedAuth;
    private final User existingUser;

    public AccountLinkingRequiredException(FederatedAuth federatedAuth, User existingUser) {
        super(ERROR_CODE);
        this.federatedAuth = federatedAuth;
        this.existingUser = existingUser;
    }
}
