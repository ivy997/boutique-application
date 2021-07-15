package boutique.config;

import boutique.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {
    private Collection<? extends GrantedAuthority> roles;
    private User user;

    public UserDetailsImpl(User user, Collection<? extends GrantedAuthority> roles) {

        this.user = user;
        this.roles = roles;
    }

    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> roles = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                user,
                roles);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String userRoles = StringUtils.collectionToCommaDelimitedString(this.roles);
        return AuthorityUtils.commaSeparatedStringToAuthorityList(userRoles);
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return null;
    }

    public User getUser() {
        return this.user;
    }
}
