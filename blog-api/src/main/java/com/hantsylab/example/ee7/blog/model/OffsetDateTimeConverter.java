package com.hantsylab.example.ee7.blog.model;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 *
 * @author hantsy
 */
@Converter(autoApply = true)
public class OffsetDateTimeConverter implements AttributeConverter<OffsetDateTime, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(OffsetDateTime attribute) {
        return attribute == null ? null : Timestamp.from(attribute.toInstant());
    }

    @Override
    public OffsetDateTime convertToEntityAttribute(Timestamp dbData) {
        return dbData == null ? null : OffsetDateTime.ofInstant(dbData.toInstant(), ZoneId.systemDefault());
    }

}
