package com.hantsylab.example.ee7.blog.model;

import java.time.OffsetDateTime;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author hantsy
 */
@MappedSuperclass
@Setter
@Getter
public class AbstractAuditableEntity extends AbstractEntity {

    private static final long serialVersionUID = 1L;


 	@Column(name="created_at")
	private OffsetDateTime createdAt;
	
	@Column(name="updated_at")
	private OffsetDateTime updatedAt;
}
