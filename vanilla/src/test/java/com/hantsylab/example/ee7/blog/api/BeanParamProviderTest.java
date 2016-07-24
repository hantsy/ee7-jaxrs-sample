package com.hantsylab.example.ee7.blog.api;

import com.hantsylab.example.ee7.blog.api.CustomBeanParamProvider.LocalDateParamConverter;
import com.hantsylab.example.ee7.blog.api.CustomBeanParamProvider.OffsetDateTimeParamConverter;
import java.lang.reflect.Type;
import java.lang.annotation.Annotation;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.ext.ParamConverter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author hantsy
 */
public class BeanParamProviderTest {

    private static final Logger LOG = Logger.getLogger(BeanParamProviderTest.class.getName());

    CustomBeanParamProvider beanParamProvider;

    @Before
    public void setup() {
        beanParamProvider = new CustomBeanParamProvider();
    }

    @After
    public void teardown() {
        beanParamProvider = null;
    }

    @Test
    public void testConvertLocalDate() {
        ParamConverter converter = beanParamProvider.getConverter(LocalDate.class, (Type) null, (Annotation[]) null);

        assertTrue(converter instanceof LocalDateParamConverter);

        LocalDateParamConverter ldConverter = (LocalDateParamConverter) converter;
        final String ldString = "2016-05-13";

        LocalDate ld = ldConverter.fromString(ldString);

        LOG.log(Level.INFO, "converted LocalDate:{0}", ld);

        String convertedString = ldConverter.toString(ld);

        assertEquals(convertedString, ldString);
    }

    @Test
    public void testConvertOffsetDateTime() {
        ParamConverter converter = beanParamProvider.getConverter(OffsetDateTime.class, (Type) null, (Annotation[]) null);

        assertTrue(converter instanceof OffsetDateTimeParamConverter);

        OffsetDateTimeParamConverter ldConverter = (OffsetDateTimeParamConverter) converter;
        final String ldString = "2016-07-05T10:11:11+08:00";

        OffsetDateTime ld = ldConverter.fromString(ldString);

        LOG.log(Level.INFO, "converted OffsetDateTime:{0}", ld);

        String convertedString = ldConverter.toString(ld);

        assertEquals(convertedString, ldString);
    }

}
