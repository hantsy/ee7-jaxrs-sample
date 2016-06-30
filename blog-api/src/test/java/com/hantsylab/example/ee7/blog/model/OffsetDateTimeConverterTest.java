package com.hantsylab.example.ee7.blog.model;

import com.hantsylab.example.ee7.blog.domain.convert.OffsetDateTimeConverter;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.logging.Logger;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;


/**
 *
 * @author hantsy
 */
public class OffsetDateTimeConverterTest {

  private static final Logger LOGGER = Logger.getLogger(OffsetDateTimeConverterTest.class.getName());

  OffsetDateTimeConverter converter;

  @Before
  public void setup() {
    converter = new OffsetDateTimeConverter();
  }

  @After
  public void teardown() {
    converter = null;
  }

  @Test
  public void testLocateDateTimeConverter() {

    OffsetDateTime now = OffsetDateTime.now();

    LOGGER.info("now value is @" + now);
    Timestamp date = converter.convertToDatabaseColumn(now);
    LOGGER.info("converted date value is @" + date);

    OffsetDateTime backldt = converter.convertToEntityAttribute(date);
    LOGGER.info("converted LocalDateTime value is @" + backldt);

    assertEquals(now.getYear(), backldt.getYear());
    assertEquals(now.getMonth(), backldt.getMonth());
    assertEquals(now.getDayOfMonth(), backldt.getDayOfMonth());
    assertEquals(now.getMinute(), backldt.getMinute());
    assertEquals(now.getSecond(), backldt.getSecond());
  }

  @Test
  public void testConvertNullValue() {
    Timestamp date = converter.convertToDatabaseColumn(null);
    assertNull(date);

    OffsetDateTime backldt = converter.convertToEntityAttribute(null);
    assertNull(backldt);
  }

}
