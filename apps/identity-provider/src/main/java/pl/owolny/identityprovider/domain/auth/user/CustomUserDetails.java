package pl.owolny.identityprovider.domain.auth.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.owolny.identityprovider.domain.authority.Role;
import pl.owolny.identityprovider.domain.user.User;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CustomUserDetails implements UserDetails {

    private UUID id;
    private List<GrantedAuthority> authorities;
    private String password;
    private boolean isActive;

    private CustomUserDetails(UUID id, String password, Collection<? extends Role> roles, boolean isActive) {
        this.id = id;
        this.password = password;
        this.authorities = roles.stream()
                .flatMap(role -> role.getAuthorities().stream()
                        .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                )
                .collect(Collectors.toList());
        this.isActive = isActive;
    }

    public static CustomUserDetails fromUser(User user) {
        return new CustomUserDetails(user.getId(), user.getCredentials().getPasswordHash(), user.getRoles(), user.isActive());
    }

    @Override
    public String getUsername() {
        return this.id.toString();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isActive;
    }
}
