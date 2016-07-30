package com.hantsylab.example.ee7.blog.security;

import com.hantsylab.example.ee7.blog.domain.model.User;
import com.hantsylab.example.ee7.blog.domain.repository.UserRepository;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

/**
 *
 * @author hantsy
 */
@RequestScoped
public class AuthenticatedUserProducer {

    @Produces
    @RequestScoped
    @AuthenticatedUser
    private User authenticatedUser;

    @Inject
    UserRepository users;

    public void handleAuthenticationEvent(@Observes @AuthenticatedUser String username) {
        this.authenticatedUser = users.findByUsername(username);
    }

}
