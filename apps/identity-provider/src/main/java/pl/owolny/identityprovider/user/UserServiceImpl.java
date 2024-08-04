package pl.owolny.identityprovider.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.owolny.identityprovider.federation.FederatedProvider;

import java.util.Optional;

@Service
@Slf4j
class UserServiceImpl implements UserService {

    private final WebClient webClient;
    private static final String USERS_SERVICE_URL = "http://localhost:8080";

    UserServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Optional<UserDto> getUser(String usernameOrEmail) {
        return Optional.empty();
//        return webClient.get()
//                .uri(USERS_SERVICE_URL + "/users/" + usernameOrEmail)
//                .retrieve()
//                .bodyToMono(UserDto.class)
//                .doOnError(e -> log.error("Error during getting user by username or email: {}", e.getMessage()))
//                .blockOptional();
    }

    @Override
    public Optional<UserDto> getUserByFederatedAccount(FederatedProvider federatedProvider, String federatedAccountId) {
        return Optional.empty();
//        return webClient.get()
//                .uri(USERS_SERVICE_URL + "/users/federated/" + federatedProvider.name() + "/" + federatedAccountId)
//                .retrieve()
//                .bodyToMono(UserDto.class)
//                .doOnError(e -> log.error("Error during getting user by federated account: {}", e.getMessage()))
//                .blockOptional();
    }

    @Override
    public UserDto getUserByRefreshToken(String refreshToken) {
        return null;
    }
}
