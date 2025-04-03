package io.openliberty.guides.multimodules.lib;

import jakarta.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

@ApplicationScoped
public class LibBean {
    public Config getConfig() {
        System.out.println("LibBean: " + this.getClass().getClassLoader());
        return ConfigProvider.getConfig();
    }
}
