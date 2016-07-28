/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hantsylab.example.ee7.blog.arqtest;

import com.hantsylab.example.ee7.blog.Fixtures;
import com.hantsylab.example.ee7.blog.crypto.Crypto;
import com.hantsylab.example.ee7.blog.crypto.PasswordEncoder;
import com.hantsylab.example.ee7.blog.domain.model.User;
import com.hantsylab.example.ee7.blog.domain.repository.UserRepository;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

/**
 *
 * @author hantsy
 */
@Singleton
@Startup
public class Initializer {
    
    public Initializer() {
    }
    @Inject
    UserRepository users;
    @Inject
    @Crypto(value = Crypto.Type.BCRYPT)
    PasswordEncoder encoder;

    @PostConstruct
    public void init() {
        User user = Fixtures.newUser("Hantsy", "Bai", "testuser", encoder.encode("test123"));
        users.save(user);
    }
    
}
