package pl.owolny.identityprovider.domain.user;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.owolny.identityprovider.domain.auth.FederatedIdentityAccount;
import pl.owolny.identityprovider.domain.authority.Authority;
import pl.owolny.identityprovider.domain.authority.AuthorityRepository;
import pl.owolny.identityprovider.domain.authority.Role;
import pl.owolny.identityprovider.domain.authority.RoleRepository;
import pl.owolny.identityprovider.domain.credentials.Credentials;
import pl.owolny.identityprovider.federation.FederatedProvider;
import pl.owolny.identityprovider.utils.countrycode.CountryCode;

import java.time.LocalDateTime;
import java.util.Set;

@Slf4j
@Component
class UserRepositoryInit {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;

    UserRepositoryInit(UserRepository userRepository, RoleRepository roleRepository, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
    }

    @PostConstruct
    public void register() {
        log.info("Registering default roles and authorities");
        var userAuthority = authorityRepository.save(Authority.builder().name("AUTHORITY_USER_1").build());
        var userAuthority2 = authorityRepository.save(Authority.builder().name("AUTHORITY_USER_2").build());
        var adminAuthority = authorityRepository.save(Authority.builder().name("AUTHORITY_ADMIN_1").build());
        var adminAuthority2 = authorityRepository.save(Authority.builder().name("AUTHORITY_ADMIN_2").build());

        Role userRole = roleRepository.save(
                Role.builder()
                        .name("USER")
                        .authorities(Set.of(userAuthority, userAuthority2))
                        .build()
        );
        Role adminRole = roleRepository.save(
                Role.builder()
                        .name("ADMIN")
                        .authorities(Set.of(adminAuthority, adminAuthority2))
                        .build()
        );
        Credentials credentials = Credentials.builder().passwordHash("test").build();

        log.info("Roles and authorities registered");
        log.info("Registering default user");
        // Tworzymy obiekt UserProfile
        UserProfile userProfile = UserProfile.builder()
                .gender("MALE")
                .avatarUrl("https://www.google.com")
                .countryCode(new CountryCode("PL"))
                .build();

        // Tworzymy obiekt ExternalIdentityAccount
        FederatedIdentityAccount externalAccount = FederatedIdentityAccount.builder()
                .federatedIdentityId("googleid-123123")
                .username("test-google")
                .provider(FederatedProvider.GOOGLE)
                .connectedAt(LocalDateTime.now())
                .isEmailVerified(true)
                .build();

        // Tworzymy obiekt User
        User user = User.builder()
                .email("test@email.com")
                .username("test")
                .credentials(credentials)
                .profile(userProfile)
                .roles(Set.of(userRole, adminRole))
                .createdAt(LocalDateTime.now())
                .isActive(true)
                .build();

        // Ustawiamy relację dwukierunkową
        userProfile.setUser(user);
        externalAccount.setUser(user);
        credentials.setUser(user);
        user.setFederatedIdentityAccounts(Set.of(externalAccount));

        // Zapisujemy użytkownika
        userRepository.save(user);
    }
}
