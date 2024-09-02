//package pl.owolny.identityprovider.domain.user;
//
//import jakarta.annotation.PostConstruct;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//import pl.owolny.identityprovider.domain.authority.Authority;
//import pl.owolny.identityprovider.domain.authority.AuthorityRepository;
//import pl.owolny.identityprovider.domain.authority.Role;
//import pl.owolny.identityprovider.domain.authority.RoleRepository;
//import pl.owolny.identityprovider.domain.credentials.Credentials;
//import pl.owolny.identityprovider.domain.federatedidentity.FederatedIdentityAccount;
//import pl.owolny.identityprovider.federation.FederatedProvider;
//import pl.owolny.identityprovider.utils.countrycode.CountryCode;
//
//import java.time.LocalDateTime;
//import java.util.Set;
//
//@Slf4j
//@Component
//class UserRepositoryInit {
//
//    private final UserRepository userRepository;
//    private final RoleRepository roleRepository;
//    private final AuthorityRepository authorityRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    UserRepositoryInit(UserRepository userRepository, RoleRepository roleRepository, AuthorityRepository authorityRepository, PasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.roleRepository = roleRepository;
//        this.authorityRepository = authorityRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @PostConstruct
//    public void register() {
//        log.info("Registering default roles and authorities");
//        var userAuthority = authorityRepository.save(Authority.builder().name("AUTHORITY_USER_1").build());
//        var userAuthority2 = authorityRepository.save(Authority.builder().name("AUTHORITY_USER_2").build());
//        var adminAuthority = authorityRepository.save(Authority.builder().name("AUTHORITY_ADMIN_1").build());
//        var adminAuthority2 = authorityRepository.save(Authority.builder().name("AUTHORITY_ADMIN_2").build());
//
//        Role userRole = roleRepository.save(
//                Role.builder()
//                        .name("USER")
//                        .authorities(Set.of(userAuthority, userAuthority2))
//                        .build()
//        );
//        Role adminRole = roleRepository.save(
//                Role.builder()
//                        .name("ADMIN")
//                        .authorities(Set.of(adminAuthority, adminAuthority2))
//                        .build()
//        );
//        Credentials credentials = Credentials.builder().passwordHash(passwordEncoder.encode("test")).build();
//
//        log.info("Roles and authorities registered");
//        log.info("Registering default user");
//        UserProfile userProfile = UserProfile.builder()
//                .gender("MALE")
//                .avatarUrl("https://www.google.com")
//                .countryCode(new CountryCode("PL"))
//                .build();
//
//        FederatedIdentityAccount externalAccount = FederatedIdentityAccount.builder()
//                .federatedIdentityId("googleid-123123")
//                .username("test-google")
//                .provider(FederatedProvider.GOOGLE)
//                .connectedAt(LocalDateTime.now())
//                .isEmailVerified(true)
//                .build();
//
//        User user = User.builder()
//                .email("osk4r.wolny@gmail.com")
//                .username("test")
//                .credentials(credentials)
//                .profile(userProfile)
//                .roles(Set.of(userRole, adminRole))
//                .createdAt(LocalDateTime.now())
//                .isActive(true)
//                .isEmailVerified(false)
//                .build();
//
//        userProfile.setUser(user);
//        externalAccount.setUser(user);
//        credentials.setUser(user);
//        user.setFederatedIdentityAccounts(Set.of(externalAccount));
//
//        userRepository.save(user);
//    }
//}