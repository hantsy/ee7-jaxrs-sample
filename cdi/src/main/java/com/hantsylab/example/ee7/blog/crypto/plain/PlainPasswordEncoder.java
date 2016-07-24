package com.hantsylab.example.ee7.blog.crypto.plain;

import com.hantsylab.example.ee7.blog.crypto.PasswordEncoder;

/**
 *
 * @author hantsy
 */
public class PlainPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        return rawPassword.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return rawPassword.equals(encodedPassword);
    }

}
