package com.hantsylab.example.ee7.blog.model;

import java.sql.Date;
import java.time.LocalDate;
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
public class LocalDateConverterTest {

  private static final Logger LOGGER = Logger.getLogger(LocalDateConverterTest.class.getName());

  LocalDateConverter converter;

  @Before
  public void setup() {
    converter = new LocalDateConverter();
  }

  @After
  public void teardown() {
    converter = null;
  }

  @Test
  public void testLocateDateTimeConverter() {

    LocalDate now = LocalDate.now();

    LOGGER.info("now value is @" + now);
    Date date = converter.convertToDatabaseColumn(now);
    LOGGER.info("converted date value is @" + date);

    LocalDate backldt = converter.convertToEntityAttribute(date);
    LOGGER.info("converted LocalDateTime value is @" + backldt);

    assertEquals(now.getYear(), backldt.getYear());
    assertEquals(now.getMonth(), backldt.getMonth());
    assertEquals(now.getDayOfMonth(), backldt.getDayOfMonth());
  }

  @Test
  public void testConvertNullValue() {
    Date date = converter.convertToDatabaseColumn(null);
    assertNull(date);

    LocalDate backldt = converter.convertToEntityAttribute(null);
    assertNull(backldt);
  }

}
