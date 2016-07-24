package com.hantsylab.example.ee7.blog;

import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;


/**
 * @author hantsy
 */
@Dependent
public class LoggerProducer {

  @Produces
  public Logger getLogger(InjectionPoint p) {
    return Logger.getLogger(p.getMember().getDeclaringClass().getName());
  }

}
