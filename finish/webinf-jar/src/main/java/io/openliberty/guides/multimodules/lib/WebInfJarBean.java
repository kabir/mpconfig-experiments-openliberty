package io.openliberty.guides.multimodules.lib;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.config.Config;

@ApplicationScoped
public class WebInfJarBean {
    @Inject
    Config config;

    public Config getConfig() {
        System.out.println("WebInfJarBean: " + this.getClass().getClassLoader());
        return config;
    }
}
