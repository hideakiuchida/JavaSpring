package com.valmar.ecommerce.model;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class JwtUserFactory {

    private JwtUserFactory() {
    }

    public static JwtUser create(Usuario user) {
        return new JwtUser(
                user.getId(),
                user.getNombre(),
                user.getApellido(),
                user.getLogin(),
                user.getCorreo(),
                user.getPassword(),
                user.getEstado(),
                mapToGrantedAuthorities(user.getAuthorities())
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<Authority> authorities) {
        return authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName().name()))
                .collect(Collectors.toList());
    }
}
