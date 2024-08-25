package pl.owolny.identityprovider.domain.user;

import pl.owolny.identityprovider.domain.federatedidentity.FederatedIdentityAccount;

import java.util.UUID;

public interface UserService {

    User createFromFederatedAccount(UUID id, String username, String email, boolean isActive, boolean isEmailVerified, UserProfile profile, FederatedIdentityAccount federatedIdentityAccount);

    User getByUsername(String username);

    User getByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsById(UUID id);

    boolean existsByUsernameOrEmail(String username, String email);
}
