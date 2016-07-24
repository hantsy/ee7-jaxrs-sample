package com.hantsylab.example.ee7.blog.service;

/**
 *
 * @author hantsy
 */
public class UsernameWasTakenException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UsernameWasTakenException(String msg) {
        super(msg);
    }

}
