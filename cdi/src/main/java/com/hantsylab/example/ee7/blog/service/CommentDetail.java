package com.hantsylab.example.ee7.blog.service;

import java.io.Serializable;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author hantsy
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String content;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private String createdByUsername;

}
