package io.openliberty.guides.multimodules.lib;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.config.Config;

@ApplicationScoped
public class LibBean {

    @Inject
    Config config;

    public Config getConfig() {
        System.out.println("LibBean: " + this.getClass().getClassLoader());
        return config;
    }

    public Config getConfigQuietly() {
        return config;
    }

}
