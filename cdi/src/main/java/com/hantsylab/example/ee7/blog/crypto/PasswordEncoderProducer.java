/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hantsylab.example.ee7.blog.crypto;

import com.hantsylab.example.ee7.blog.crypto.bcrypt.BCryptPasswordEncoder;
import com.hantsylab.example.ee7.blog.crypto.plain.PlainPasswordEncoder;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 *
 * @author hantsy
 */
@Dependent
public class PasswordEncoderProducer {

    @Produces
    @Crypto
    public PasswordEncoder passwordEncoder(InjectionPoint ip) {
        Crypto crypto = ip.getAnnotated().getAnnotation(Crypto.class);
        Crypto.Type type = crypto.value();
        PasswordEncoder encoder;
        switch (type) {
            case PLAIN:
                encoder = new PlainPasswordEncoder();
                break;
            case BCRYPT:
                encoder = new BCryptPasswordEncoder();
                break;
            default:
                encoder = new PlainPasswordEncoder();
                break;
        }

        return encoder;
    }

}
