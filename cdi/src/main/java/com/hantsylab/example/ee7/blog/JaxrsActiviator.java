package com.hantsylab.example.ee7.blog;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import org.jboss.resteasy.plugins.interceptors.CorsFilter;

@ApplicationPath("api")
public class JaxrsActiviator extends Application {

//    @Override
//    public Set<Object> getSingletons() {
//        CorsFilter corsFilter = new CorsFilter();
//        corsFilter.getAllowedOrigins().add("http://localhost:3000");
//
//        Set<Object> singletons = new HashSet<>();
//        singletons.add(corsFilter);
//        return singletons;
//    }

}
