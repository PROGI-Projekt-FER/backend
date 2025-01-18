package com.ticketswap.model;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    @Getter
    private final User user;
    private final Map<String, Object> attributes;

    public CustomOAuth2User(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    @Override
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (user.getUserRole().equals(UserRole.DEACTIVATED)) {
            return List.of(new SimpleGrantedAuthority(UserRole.DEACTIVATED.name()));
        } else if (user.getUserRole().equals(UserRole.ADMIN)) {
            return List.of(
                    new SimpleGrantedAuthority(UserRole.ADMIN.name()),
                    new SimpleGrantedAuthority(UserRole.REGULAR.name())
            );
        } else {
            return List.of(new SimpleGrantedAuthority(UserRole.REGULAR.name()));
        }
    }

    @Override
    public String getName() {
        return user.getUsername();
    }

}
