package pl.owolny.identityprovider.domain.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.owolny.identityprovider.domain.auth.FederatedIdentityAccount;
import pl.owolny.identityprovider.domain.authority.RoleService;

import java.util.Set;

@Service
@Slf4j
class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    UserServiceImpl(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    @Override
    public User createFromFederatedAccount(String username, String email, boolean isActive, boolean isEmailVerified, UserProfile profile, FederatedIdentityAccount federatedIdentityAccount) {
        User user = User.builder()
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
}