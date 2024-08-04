package pl.owolny.identityprovider.user;

import pl.owolny.identityprovider.federation.FederatedProvider;

import java.util.Optional;

public interface UserService {

    Optional<UserDto> getUser(String usernameOrEmail);

    Optional<UserDto> getUserByFederatedAccount(FederatedProvider federatedProvider, String federatedAccountId);

    UserDto getUserByRefreshToken(String refreshToken);
}
