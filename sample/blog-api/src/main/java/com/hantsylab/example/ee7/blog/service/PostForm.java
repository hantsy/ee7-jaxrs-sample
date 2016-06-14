package com.hantsylab.example.ee7.blog.service;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author hantsy
 */
@Data
@Builder
public class PostForm implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

}
