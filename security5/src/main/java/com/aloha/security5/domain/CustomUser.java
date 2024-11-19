package com.aloha.security5.domain;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CustomUser implements UserDetails {

    private Users user;

    public CustomUser(Users user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // List<UserAuth>   ➡  Collection<SimpleGrantedAuthority>
        return user.getAuthList().stream()
                                 .map( (auth) -> new SimpleGrantedAuthority( auth.getAuth() ) )
                                 .collect(Collectors.toList());

    }

    @Override
    public String getPassword() {
        return user.getPassword();
        // return "PROTECTED";
    }

    @Override
    public String getUsername() {
        return user.getUsername();
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
        return user.getEnabled() == 0 ? false : true;
    }


    
}
