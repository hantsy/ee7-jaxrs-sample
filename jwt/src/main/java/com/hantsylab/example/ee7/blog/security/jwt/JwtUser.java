package com.hantsylab.example.ee7.blog.security.jwt;

import com.hantsylab.example.ee7.blog.security.UserPrincipal;
import java.util.List;

/**
 *
 * @author hantsy
 */
public class JwtUser implements UserPrincipal {

    private final String username;
    private final List<String> roles;

    public JwtUser(String username, List<String> roles) {
        this.username = username;
        this.roles = roles;
    }

    @Override
    public List<String> getRoles() {
        return this.roles;
    }

    @Override
    public String getName() {
        return this.username;
    }

}
