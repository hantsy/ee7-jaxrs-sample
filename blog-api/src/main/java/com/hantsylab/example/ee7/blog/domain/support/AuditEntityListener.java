package com.hantsylab.example.ee7.blog.domain.support;

import java.time.OffsetDateTime;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 *
 * @author hantsy
 */
public class AuditEntityListener {

    @PrePersist
    public void beforePersist(Object entity) {
        if (entity instanceof AbstractAuditableEntity) {
            AbstractAuditableEntity o = (AbstractAuditableEntity) entity;
            final OffsetDateTime now = OffsetDateTime.now();
            o.setCreatedAt(now);
            o.setUpdatedAt(now);
        }
    }

    @PreUpdate
    public void beforeUpdate(Object entity) {
        if (entity instanceof AbstractAuditableEntity) {
            AbstractAuditableEntity o = (AbstractAuditableEntity) entity;
            o.setUpdatedAt(OffsetDateTime.now());
        }
    }
}
