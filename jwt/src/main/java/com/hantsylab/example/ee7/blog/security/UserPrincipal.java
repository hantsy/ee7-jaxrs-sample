package com.hantsylab.example.ee7.blog.security;

import java.security.Principal;
import java.util.List;

/**
 *
 * @author hantsy
 */
public interface UserPrincipal extends Principal {

    List<String> getRoles();

}
