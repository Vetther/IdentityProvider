package pl.owolny.identityprovider.domain.federatedidentity;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.owolny.identityprovider.domain.user.User;
import pl.owolny.identityprovider.federation.FederatedProvider;

import java.util.UUID;

@Service
@Transactional
public class FederatedIdentityAccountService {

    private final FederatedIdentityAccountRepository federatedIdentityAccountRepository;

    public FederatedIdentityAccountService(FederatedIdentityAccountRepository federatedIdentityAccountRepository) {
        this.federatedIdentityAccountRepository = federatedIdentityAccountRepository;
    }

    public void create(UUID userId, String federatedIdentityId, FederatedProvider federatedProvider, String federatedEmail, String federatedUsername) {
        FederatedIdentityAccount federatedIdentityAccount = FederatedIdentityAccount.builder()
                .user(User.builder().id(userId).build())
                .federatedIdentityId(federatedIdentityId)
                .provider(federatedProvider)
                .username(federatedUsername)
                .email(federatedEmail)
                .build();
        federatedIdentityAccountRepository.save(federatedIdentityAccount);
    }
}
