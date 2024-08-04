package pl.owolny.identityprovider.user;

import pl.owolny.identityprovider.federation.FederatedProvider;

import java.util.Set;
import java.util.UUID;

public record UserDto(UUID id, String username, String email, Set<FederatedAccountDto> federatedAccountsDto) {

    public record FederatedAccountDto(
            String providerName,
            String id,
            String username,
            String email,
            Long connectedAt
    ) {
        public FederatedProvider getAccountProviderEnum() {
            return FederatedProvider.valueOf(providerName.toUpperCase());
        }
    }
}
