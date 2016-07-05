package com.hantsylab.example.ee7.blog.security;

import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author hantsy
 */
@ApplicationScoped
public class PasswordEncoder {

    public String encode(CharSequence raw) {
        return null;
    }

    public boolean match(CharSequence rawPassword, CharSequence encoded) {
        return false;
    }

}
