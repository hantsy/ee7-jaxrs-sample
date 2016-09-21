package com.hantsylab.example.ee7.blog.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author hantsy
 */
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class IdToken implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("id_token")
    private String token;

    private UserDetail user;
}
