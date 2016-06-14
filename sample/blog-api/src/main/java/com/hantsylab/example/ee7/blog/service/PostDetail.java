package com.hantsylab.example.ee7.blog.service;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

/**
 *
 * @author hantsy
 */
@Data
@Builder
public class PostDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    
    private String title;
    
    private String content;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;

}
