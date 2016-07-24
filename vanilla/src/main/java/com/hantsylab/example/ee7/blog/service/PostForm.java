package com.hantsylab.example.ee7.blog.service;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author hantsy
 */
@Data
@Builder
@NoArgsConstructor
@RequiredArgsConstructor
public class PostForm implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @NonNull
    private String title;

    @NotBlank
    @NonNull
    private String content;

}
