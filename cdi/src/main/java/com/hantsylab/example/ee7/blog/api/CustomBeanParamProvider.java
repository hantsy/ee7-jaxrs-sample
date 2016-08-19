package com.hantsylab.example.ee7.blog.api;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author hantsy
 */
@Provider
public class CustomBeanParamProvider implements ParamConverterProvider {

    @Override
    public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
        if (rawType.getName().equals(OffsetDateTime.class.getName())) {
            return (ParamConverter<T>) new OffsetDateTimeParamConverter();
        } else if (rawType.getName().equals(LocalDateTime.class.getName())) {
            return (ParamConverter<T>) new LocalDateTimeParamConverter();
        } else if (rawType.getName().equals(LocalDate.class.getName())) {
            return (ParamConverter<T>) new LocalDateParamConverter();
        }
        return null;
    }

    public static class LocalDateParamConverter implements ParamConverter<LocalDate> {

        public LocalDateParamConverter() {
        }

        @Override
        public LocalDate fromString(String value) {
            return value == null ? null : LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE);
        }

        @Override
        public String toString(LocalDate value) {
            return value == null ? null : value.format(DateTimeFormatter.ISO_LOCAL_DATE);
        }
    }

    /**
     * datetime format:2016-08-19T10:45:33.100Z
     */
    public static class LocalDateTimeParamConverter implements ParamConverter<LocalDateTime> {

        public LocalDateTimeParamConverter() {
        }

        @Override
        public LocalDateTime fromString(String value) {
            return value == null ? null : LocalDateTime.ofInstant(Instant.parse(value), ZoneId.systemDefault());
        }

        @Override
        public String toString(LocalDateTime value) {
            return value == null ? null : value.atZone(ZoneId.systemDefault()).toInstant().toString();
        }
    }

    public static class OffsetDateTimeParamConverter implements ParamConverter<OffsetDateTime> {

        public OffsetDateTimeParamConverter() {
        }

        @Override
        public OffsetDateTime fromString(String value) {
            return value == null ? null : OffsetDateTime.parse(value, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        }

        @Override
        public String toString(OffsetDateTime value) {
            return value == null ? null : value.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        }
    }

}
