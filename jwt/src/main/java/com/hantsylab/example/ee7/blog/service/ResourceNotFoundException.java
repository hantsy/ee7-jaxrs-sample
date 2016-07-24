package com.hantsylab.example.ee7.blog.service;

/**
 *
 * @author hantsy
 */
public class ResourceNotFoundException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
    
}
