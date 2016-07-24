package com.hantsylab.example.ee7.blog.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author hantsy
 */
@ApplicationScoped
public class PropertiesFileLoader {

    private static final Logger LOG = Logger.getLogger(PropertiesFileLoader.class.getSimpleName());

    private static final String CONFIG_FILE = "/config.properties";

    private final Map<String, Object> props = new HashMap<>();

    @PostConstruct
    public void init() {
        LOG.log(Level.INFO, "initializing the /config.properties file");
        InputStream in = this.getClass().getResourceAsStream(CONFIG_FILE);
        Properties properties = new Properties();
        try {
            properties.load(in);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        Map map = new HashMap(properties);
        this.props.putAll(map);
    }

    public Object getValue(String key) {
        return this.props.get(key);
    }

}
