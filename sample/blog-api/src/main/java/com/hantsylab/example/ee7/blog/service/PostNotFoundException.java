package com.hantsylab.example.ee7.blog.service;

/**
 *
 * @author hantsy
 */
public class PostNotFoundException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public PostNotFoundException() {
    }

    public PostNotFoundException(String message) {
        super(message);
    }
    
}
