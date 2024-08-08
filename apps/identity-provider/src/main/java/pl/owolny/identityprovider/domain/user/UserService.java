package pl.owolny.identityprovider.domain.user;

import pl.owolny.identityprovider.domain.auth.FederatedIdentityAccount;

public interface UserService {

    User createFromFederatedAccount(String username, String email, boolean isActive, boolean isEmailVerified, UserProfile profile, FederatedIdentityAccount federatedIdentityAccount);

    User getByUsername(String username);

    User getByEmail(String email);
}
