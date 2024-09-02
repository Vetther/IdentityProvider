package pl.owolny.identityprovider.domain.user;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.owolny.identityprovider.domain.authority.RoleService;
import pl.owolny.identityprovider.domain.federatedidentity.FederatedIdentityAccount;

import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
@Transactional
class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    UserServiceImpl(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    @Override
    public User createFromFederatedAccount(UUID id, String username, String email, boolean isActive, boolean isEmailVerified, UserProfile profile, FederatedIdentityAccount federatedIdentityAccount) {
        User user = User.builder()
                .id(id)
                .username(username)
                .email(email)
                .roles(Set.of(this.roleService.getDefaulRole()))
                .federatedIdentityAccounts(Set.of(federatedIdentityAccount))
                .profile(profile)
                .isActive(isActive)
                .isEmailVerified(isEmailVerified)
                .build();

        profile.setUser(user);
        federatedIdentityAccount.setUser(user);

        return this.userRepository.save(user);
    }

    @Override
    public User getByUsername(String username) {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Override
    public User getByEmail(String email) {
        return this.userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Override
    public boolean existsByUsername(String username) {
        return this.userRepository.findByUsername(username).isPresent();
    }

    @Override
    public boolean existsByEmail(String email) {
        return this.userRepository.findByEmail(email).isPresent();
    }

    @Override
    public boolean existsById(UUID id) {
        return this.userRepository.findById(id).isPresent();
    }

    @Override
    public boolean existsByUsernameOrEmail(String username, String email) {
        return this.userRepository.findByUsernameOrEmail(username, email).isPresent();
    }
}
