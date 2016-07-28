package com.hantsylab.example.ee7.blog.security;

import com.hantsylab.example.ee7.blog.domain.model.Role;
import java.security.Principal;
import java.util.List;

/**
 *
 * @author hantsy
 */
public interface UserPrincipal extends Principal {

    List<Role> getRoles();

}
