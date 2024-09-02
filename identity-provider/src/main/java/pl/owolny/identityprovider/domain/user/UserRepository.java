package pl.owolny.identityprovider.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.owolny.identityprovider.federation.FederatedProvider;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameOrEmail(String username, String email);

    Optional<User> findByFederatedIdentityAccounts_ProviderAndFederatedIdentityAccounts_FederatedIdentityId(FederatedProvider provider, String federatedIdentityId);

}
