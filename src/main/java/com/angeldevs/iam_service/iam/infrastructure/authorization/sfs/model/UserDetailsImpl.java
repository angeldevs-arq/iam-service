package com.angeldevs.iam_service.iam.infrastructure.authorization.sfs.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.angeldevs.iam_service.iam.domain.model.aggregates.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class UserDetailsImpl implements UserDetails {
    private final String username;
    @JsonIgnore
    private final String password;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
    }

    public static UserDetailsImpl build(User user) {
        var authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        return new UserDetailsImpl(user.getUsername(), user.getPassword(), authorities);
    }

}
