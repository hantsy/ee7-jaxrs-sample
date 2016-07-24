/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hantsylab.example.ee7.blog.api;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author hantsy
 */
@Getter
@Setter
@ToString
public class ValidationError {

    private String invalidValue;

    private String message;

    private String messageTemplate;

    private String path;

    public ValidationError() {

    }

    public ValidationError(
        final String invalidValue,
        final String message,
        final String messageTemplate,
        final String path) {
        this.invalidValue = invalidValue;
        this.message = message;
        this.messageTemplate = messageTemplate;
        this.path = path;
    }
}
