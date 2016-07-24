package com.hantsylab.example.ee7.blog.crypto;

/**
 *
 * @author hantsy
 */
public interface PasswordEncoder {

    public String encode(CharSequence rawPassword);

    public boolean matches(CharSequence rawPassword, String encodedPassword);

}
