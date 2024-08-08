package pl.owolny.identityprovider.domain.auth.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.owolny.identityprovider.domain.authority.Role;
import pl.owolny.identityprovider.domain.user.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private final String username;
    private final List<GrantedAuthority> authorities;
    private final boolean isActive;

    public CustomUserDetails(String username, Collection<? extends Role> roles, boolean isActive) {
        this.username = username;
        this.authorities = roles.stream()
                .flatMap(role -> role.getAuthorities().stream()
                        .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                )
                .collect(Collectors.toList());
        this.isActive = isActive;
    }

    public static CustomUserDetails fromUser(User user) {
        return new CustomUserDetails(user.getUsername(), user.getRoles(), user.isActive());
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !this.isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.isActive;
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
