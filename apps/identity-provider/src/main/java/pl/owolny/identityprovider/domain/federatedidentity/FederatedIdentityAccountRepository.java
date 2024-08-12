package pl.owolny.identityprovider.domain.federatedidentity;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.owolny.identityprovider.federation.FederatedProvider;

import java.util.UUID;

public interface FederatedIdentityAccountRepository extends JpaRepository<FederatedIdentityAccount, UUID> {
    FederatedIdentityAccount findByFederatedIdentityIdAndProvider(String federatedIdentityId, FederatedProvider provider);
}
