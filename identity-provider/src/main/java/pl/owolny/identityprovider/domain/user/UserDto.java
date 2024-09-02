package pl.owolny.identityprovider.domain.user;

import lombok.Builder;

import java.util.Set;
import java.util.UUID;

@Builder
public record UserDto(UUID id, String username, String email, Set<roleDto> rolesDto,
                      Set<FederatedAccountDto> federatedAccountsDto, boolean isActive, Long createdAt) {

    public record FederatedAccountDto(
            String providerName,
            String id,
            String username,
            Long connectedAt
    ) {
    }

    public record roleDto(
            UUID id,
            String name,
            Set<AuthorityDto> authoritiesDto
    ) {
    }

    public record AuthorityDto(
            UUID id,
            String name
    ) {
    }
}
