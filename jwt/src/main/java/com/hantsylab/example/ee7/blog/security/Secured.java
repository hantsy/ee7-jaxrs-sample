package com.hantsylab.example.ee7.blog.security;

import com.hantsylab.example.ee7.blog.domain.model.Role;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

import javax.ws.rs.NameBinding;

/**
 *
 * @author hantsy
 */
@NameBinding
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface Secured {
    Role[] value() default {};
}
