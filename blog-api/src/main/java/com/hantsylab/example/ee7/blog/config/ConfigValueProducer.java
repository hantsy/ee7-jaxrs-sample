/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hantsylab.example.ee7.blog.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

@Dependent
public class ConfigValueProducer {

    private static final Logger LOG = Logger.getLogger(ConfigValueProducer.class.getSimpleName());

    private static final String CONFIG_FILE = "/config.properties";

    /**
     * Main producer method - tries to find a property value using following
     * keys:
     *
     * <ol>
     * <li><code>key</code> property of the {@link ConfigValue} annotation (if
     * defined but no key is found - returns null),</li>
     * <li>fully qualified field class name, e.g.
     * <code>eu.awaketech.MyBean.myField</code> (if value is null, go along with
     * the last resort),</li>
     * <li>field name, e.g. <code>myField</code> for the example above (if the
     * value is null, no can do - return null)</li>
     * </ol>
     *
     * @param ip
     * @return value of the injected property or null if no value could be
     * found.
     */
    @Produces
    @ConfigValue
    public String getStringConfigValue(InjectionPoint ip) {

        String fqn = ip.getMember().getDeclaringClass().getName() + "." + ip.getMember().getName();

        // Trying with explicit key defined on the field
        String key = ip.getAnnotated().getAnnotation(ConfigValue.class).value();
        boolean isKeyDefined = !key.trim().isEmpty();

        boolean valueRequired = ip.getAnnotated().getAnnotation(ConfigValue.class).required();

        if (isKeyDefined) {
            return getValue(key);
        }

        // Falling back to fully-qualified field name resolving.
        key = fqn;
        String value = getValue(fqn);

        // No luck... so perhaps just the field name?
        if (value == null) {
            key = ip.getMember().getName();
            value = getValue(key);
        }

        // No can do - no value found but you've said it's required.
        if (value == null && valueRequired) {
            throw new IllegalStateException("No value defined for field: " + fqn
                + " but field was marked as required.");
        }

        return value;
    }

    public String getValue(String key) {
        InputStream in = this.getClass().getResourceAsStream(CONFIG_FILE);
        Properties properties = new Properties();
        try {
            properties.load(in);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        Object value = properties.get(key);

        return (value != null) ? String.valueOf(value) : null;
    }

    /**
     * Produces {@link Double} type of property from {@link String} type.
     *
     * <p>
     * Will throw {@link NumberFormatException} if the value cannot be parsed
     * into a {@link Double}
     * </p>
     *
     * @param ip
     * @return value of the injected property or null if no value could be
     * found.
     *
     * @see ConfigValueProducer#getStringConfigValue(InjectionPoint)
     */
    @Produces
    @ConfigValue
    public Double getDoubleConfigValue(InjectionPoint ip) {
        String value = getStringConfigValue(ip);

        return (value != null) ? Double.valueOf(value) : null;
    }

    /**
     * Produces {@link Integer} type of property from {@link String} type.
     *
     * <p>
     * Will throw {@link NumberFormatException} if the value cannot be parsed
     * into an {@link Integer}
     * </p>
     *
     * @param ip
     * @return value of the injected property or null if no value could be
     * found.
     *
     * @see ConfigValueProducer#getStringConfigValue(InjectionPoint)
     */
    @Produces
    @ConfigValue
    public Integer getIntegerConfigValue(InjectionPoint ip) {
        String value = getStringConfigValue(ip);
        return (value != null) ? Integer.valueOf(value) : null;
    }
}
