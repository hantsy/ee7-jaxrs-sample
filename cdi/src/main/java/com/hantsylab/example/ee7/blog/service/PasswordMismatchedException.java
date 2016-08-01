/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hantsylab.example.ee7.blog.service;

/**
 *
 * @author hantsy
 */
public class PasswordMismatchedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PasswordMismatchedException(String msg) {
        super(msg);
    }
    
}
