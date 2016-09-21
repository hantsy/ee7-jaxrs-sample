/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hantsylab.example.ee7.blog.service;

import com.hantsylab.example.ee7.blog.crypto.Crypto;
import com.hantsylab.example.ee7.blog.crypto.PasswordEncoder;
import com.hantsylab.example.ee7.blog.domain.model.Post;
import com.hantsylab.example.ee7.blog.domain.model.Role;
import com.hantsylab.example.ee7.blog.domain.model.User;
import com.hantsylab.example.ee7.blog.domain.repository.PostRepository;
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
    PostRepository posts;

    @Inject
    @Crypto(value = Crypto.Type.BCRYPT)
    PasswordEncoder encoder;

    @PostConstruct
    public void init() {

        User user = User.builder()
            .firstName("Hantsy")
            .lastName("Bai")
            .username("test")
            .password(encoder.encode("test123"))
            .role(Role.USER)
            .build();
        users.save(user);

        User admin = User.builder()
            .firstName("Foo")
            .lastName("Bar")
            .username("admin")
            .password(encoder.encode("admin123"))
            .role(Role.ADMIN)
            .build();

        users.save(admin);

        Post post1 = Post.builder()
            .title("Getting started with REST")
            .content("Content of Getting started with REST")
            .build();
        post1.setCreatedBy("test");
        posts.save(post1);

        Post post2 = Post.builder()
            .title("Getting started with AngularJS 1.x")
            .content("Content of Getting started with AngularJS 1.x")
            .build();
        post2.setCreatedBy("test");
        posts.save(post2);
        
        Post post3 = Post.builder()
            .title("Getting started with Angular2")
            .content("Content of Getting started with Angular2")
            .build();
        post3.setCreatedBy("test");
        posts.save(post3);

    }

}
