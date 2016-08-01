/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hantsylab.example.ee7.blog.security;

import javax.enterprise.util.AnnotationLiteral;

/**
 *
 * @author hantsy
 */
public class AuthenticatedUserLiteral
    extends AnnotationLiteral<AuthenticatedUser>
    implements AuthenticatedUser {

    private static final long serialVersionUID = 1L;

}
