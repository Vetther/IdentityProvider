package pl.owolny.identityprovider.domain.authority;

import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RoleService {

    public final static String DEFAULT_ROLE_NAME = "USER";
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role getDefaulRole() {
        return roleRepository.findByName(DEFAULT_ROLE_NAME)
                .orElseGet(this::createDefaultRole);
    }

    private Role createDefaultRole() {
        Authority authority_read = Authority.builder().name("USER:READ").build();
        Authority authority_write = Authority.builder().name("USER:WRITE").build();
        Role role = Role.builder()
                .name(DEFAULT_ROLE_NAME)
                .authorities(Set.of(authority_read, authority_write))
                .build();
        authority_read.setRoles(Set.of(role));
        authority_write.setRoles(Set.of(role));
        return roleRepository.save(role);
    }
}
